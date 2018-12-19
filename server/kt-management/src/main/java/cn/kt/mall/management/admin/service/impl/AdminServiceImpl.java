package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.exception.UnauthorizedException;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.*;
import cn.kt.mall.common.user.vo.PasswordVO;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.util.PasswordUtil;
import cn.kt.mall.management.admin.dao.AuthorityDao;
import cn.kt.mall.management.admin.entity.AdminEntity;
import cn.kt.mall.management.admin.entity.AdminOperationEntity;
import cn.kt.mall.management.admin.entity.AdminResourcesOperationEntity;
import cn.kt.mall.management.admin.service.AuthorityService;
import cn.kt.mall.management.admin.vo.*;
import cn.kt.mall.management.admin.dao.AdminDAO;
import cn.kt.mall.management.admin.dao.AdminLoginDAO;
import cn.kt.mall.management.admin.entity.UserLoginEntity;
import cn.kt.mall.common.admin.model.AdminInfo;
import cn.kt.mall.common.admin.model.CurrentAdmin;
import cn.kt.mall.management.admin.service.AdminService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户业务实现类
 * Created by jerry on 2017/12/21.
 */
@Service
public class AdminServiceImpl implements AdminService {
    private static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private AdminDAO adminDAO;

    private AdminLoginDAO loginDAO;

    private JWTRSAHelper jwtHelper;

    @Autowired
    private AuthorityDao authorityDao;
    @Autowired
    private AuthorityService authorityService;

    @Autowired
    public AdminServiceImpl(AdminDAO adminDAO, AdminLoginDAO loginDAO, JWTRSAHelper jwtHelper) {
        this.adminDAO = adminDAO;
        this.loginDAO = loginDAO;
        this.jwtHelper = jwtHelper;
    }

    @Override
    @Transactional
    public void add(AddAdminVO adminVO) {
        A.checkParam(adminVO.getAccount() == null, "账号不能为空");
        A.checkParam(adminVO.getPassword() == null, "密码不能为空");
        A.check(this.adminDAO.getByAccount(adminVO.getAccount()) != null, "用户名已经被占用，请重新输入");
        String id = IDUtil.getUUID();

        //添加用户
        this.adminDAO.add(adminVO.getUserEntity(SubjectUtil.getCurrent().getId(),id));
        //添加用户与角色关系
        authorityDao.addAdminRole(id,adminVO.getRoleId());
    }

    @Override
    public void updatePassword(CurrentAdmin currentAdmin, PasswordVO passwordVO) {
        A.checkParam(passwordVO.getOldPassword() == null, "原密码不能为空");
        A.checkParam(passwordVO.getPassword() == null, "新密码不能为空");

        AdminEntity entity = this.adminDAO.getById(currentAdmin.getId());

        A.check(!PasswordUtil.check(passwordVO.getOldPassword(), entity.getPassword(), entity.getSalt()), "原密码输入错误");

        String[] pwd = PasswordUtil.getEncryptPassword(passwordVO.getPassword());
        this.adminDAO.updatePassword(entity.getId(), pwd[0], pwd[1]);
    }

    @Override
    public LoginInfoVO login(LoginVO loginVO) {
        String account = loginVO.getAccount();
        String password = loginVO.getPassword();
        A.checkParam(account == null, "用户名不能为空");
        A.checkParam(password == null, "密码不能为空");

        AdminEntity entity = this.adminDAO.getByAccount(account);
        A.check(entity == null, "账号不存在");
        A.check(!PasswordUtil.check(password, entity.getPassword(), entity.getSalt()), "用户名或密码错误，请重新输入");
        A.check("1".equals(entity.getStatus()), "账号已禁用,联系管理员启用后再登录");

        String userId= entity.getId();

        long start = System.currentTimeMillis();
        String accessToken = jwtHelper.generate(AccessToken.get(JWTConstant.Type.USER, userId));
        logger.debug("JWT RSA generate user time " + (System.currentTimeMillis() - start) + "ms");

        if(this.loginDAO.updateLogin(userId, accessToken, jwtHelper.getExpire()) == 0) {
            this.loginDAO.add(IDUtil.getUUID(), userId, accessToken, jwtHelper.getExpire());
        }

        return new LoginInfoVO(entity, accessToken);
    }

    @Override
    public boolean check(String accessToken) {
        long start = System.currentTimeMillis();
        AccessToken tokenInfo = this.jwtHelper.parserToken(accessToken);
        logger.debug("JWT RSA parserToken user time " + (System.currentTimeMillis() - start) + "ms");
        if(tokenInfo == null) {
            return false;
        }
        UserLoginEntity entity = this.loginDAO.getByUserId(tokenInfo.getId());

        if(!"0".equals(entity.getStatus()) || !accessToken.equals(entity.getToken())) {
            return false;
        }

        return new Date().compareTo(entity.getExpireTime()) < 0;
    }

    @Override
    public void logout(String accessToken) {
        AccessToken tokenInfo = null;
        try {
            tokenInfo = jwtHelper.parserToken(accessToken);
        } catch (Exception e) {
            return;
        }
        if(tokenInfo == null) {
            return;
        }
        this.loginDAO.updateLogout(tokenInfo.getId());
    }

    @Override
    public CurrentAdmin getCurrent(String accessToken) {
        long start = System.currentTimeMillis();
        AccessToken tokenInfo = jwtHelper.parserToken(accessToken);
        logger.info("JWT RSA parserToken user time " + (System.currentTimeMillis() - start) + "ms");
        AdminEntity entity = this.adminDAO.getById(tokenInfo.getId());
        if(entity == null) {
            return null;
        }
        return getCurrentUser(accessToken, entity);
    }

    private CurrentAdmin getCurrentUser(String accessToken, AdminEntity entity) {
        CurrentAdmin current = new CurrentAdmin();
        BeanUtils.copyProperties(entity, current);
        current.setToken(accessToken);
        return current;
    }

    @Override
    public AdminInfo getUserById(String id) {
        AdminEntity entity = this.adminDAO.getById(id);
        if(entity == null) {
            return null;
        }
        AdminInfo user = new AdminInfo();
        BeanUtils.copyProperties(entity, user);
        return user;
    }

    @Override
    public AdminInfo getUserByUsername(String username) {
        AdminEntity entity = this.adminDAO.getByAccount(username);
        if(entity == null) {
            return null;
        }

        AdminInfo admin = new AdminInfo();
        BeanUtils.copyProperties(entity, admin);
        return admin;
    }

    @Override
    public CurrentAdmin verify(String jwt) throws UnauthorizedException {
        A.checkAuth(StringUtils.isEmpty(jwt), "非法的客户端请求");
        long start = System.currentTimeMillis();
        AccessToken accessToken = this.jwtHelper.parserToken(jwt);
        logger.info("JWT RSA parserToken user time " + (System.currentTimeMillis() - start) + "ms");
        UserLoginEntity entity = this.loginDAO.getByUserId(accessToken.getId());

        A.checkAuth(entity == null, "用户登录状态异常");
        A.checkAuth(!"0".equals(entity.getStatus()) || !jwt.equals(entity.getToken()) || new Date().compareTo(entity.getExpireTime()) >= 0, "用户登录状态过期，请重新登录");

        AdminEntity adminEntity = this.adminDAO.getById(entity.getUserId());
        A.checkAuth(adminEntity == null, "用户未注册，请先注册");

        return getCurrentUser(jwt, adminEntity);
    }

    @Override
    public SubjectInstance verifyShop(String jwt) throws UnauthorizedException {
        return null;
    }

    @Override
    public void check(String userId, String[] roles, String[] privileges){
        String sss = userId;
    }

    @Override
    public List<AdminInfo> getUserList() {
        List<AdminInfo> adminList = adminDAO.getUserList();
        if (adminList.isEmpty()) {
            return new ArrayList<>();
        }

        //Map<String, AdminInfo> stringAdminInfoMap = adminList.stream().collect(Collectors.toMap(AdminInfo::getId, Function.identity());

        List<RoleResVO> roleResVOList = authorityDao.getAllRoleResList();

        ArrayListMultimap<Integer, Integer> roleId1ResourceIdListMap = ArrayListMultimap.create();
        for (RoleResVO r : roleResVOList) {
            roleId1ResourceIdListMap.put(r.getRoleId(), r.getResId());
        }

        List<AdminResourcesOperationEntity> resourcesOperationEntitys = authorityDao.getResourcesOperationEntitys();
        ArrayListMultimap<Integer, Integer> roleId2ResourceIdListMap = ArrayListMultimap.create();
        for (AdminResourcesOperationEntity r : resourcesOperationEntitys) {
            roleId2ResourceIdListMap.put(r.getRoleId(), r.getOpId());
        }


        List<AdminResourceVO> resourceList = authorityDao.getResourceList();
        List<AdminOperationEntity> adminOperationEntities = authorityDao.getOperationEntitys();
        resourceList.sort(new Comparator<AdminResourceVO>() {
            @Override
            public int compare(AdminResourceVO o1, AdminResourceVO o2) {
                if (o1.getParentId() > o2.getParentId()) {
                    return 1;
                } else if (o1.getParentId() < o2.getParentId()) {
                    return -1;
                }

                return o1.getId() - o2.getId();
            }
        });

        for (AdminInfo a : adminList) {
            List<Integer> resourceIdList = roleId1ResourceIdListMap.get(a.getRoleId());
            List<Integer> operationIdList = roleId2ResourceIdListMap.get(a.getRoleId());
            if (resourceIdList != null) {
                String resourceNames = resourceNames(resourceIdList, resourceList);
                String operationNames = operationNames(operationIdList,adminOperationEntities);
                String names = resourceNames+","+operationNames;
                if(names.endsWith(",")){
                    names = names.substring(0,names.length() - 1);
                }
                a.setResName(names);
            }
        }
        return adminList;

    }


    private String resourceNames(List<Integer> resourceIdList, List<AdminResourceVO> resourceList) {

        String names = "";
        for(AdminResourceVO v : resourceList) {
            if (resourceIdList.contains(v.getId())) {
                names += v.getName() + ",";
            }
        }

        return names.length() > 0 ? names.substring(0,names.length() - 1) : "";
    }
    private String operationNames(List<Integer> operationIdList,List<AdminOperationEntity> adminOperationEntities) {

        String names = "";
        for(AdminOperationEntity v : adminOperationEntities) {
            if (operationIdList.contains(v.getOpId())) {
                names += v.getName() + ",";
            }
        }

        return names.length() > 0 ? names.substring(0,names.length() - 1) : "";
    }

    @Override
    @Transactional
    public void deleteUser(String[] userIds) {
        for (String userId : userIds) {
            int i = adminDAO.delete(userId);
            A.check(i < 1,"删除用户失败");
        }
        int count = authorityDao.delUserRoles(Arrays.asList(userIds));
        A.check(count < 1,"删除用户失败");
    }
}
