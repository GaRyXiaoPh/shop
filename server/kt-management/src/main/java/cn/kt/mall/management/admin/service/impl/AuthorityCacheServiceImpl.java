package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.management.admin.dao.AuthorityDao;
import cn.kt.mall.management.admin.entity.AdminOperationEntity;
import cn.kt.mall.management.admin.entity.AdminResourcesEntity;
import cn.kt.mall.management.admin.entity.AdminResourcesOperationEntity;
import cn.kt.mall.management.admin.service.AuthorityCacheService;
import cn.kt.mall.management.admin.vo.*;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AuthorityCacheServiceImpl implements AuthorityCacheService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String CACHE_VALUE = "adminResourcesVOList";
    public static final String CACHE_KEY= "#roleId";

    @Autowired
    private AuthorityDao authorityDao;


    @Cacheable(value = AuthorityCacheServiceImpl.CACHE_VALUE,key = AuthorityCacheServiceImpl.CACHE_KEY)
    public List<AdminResourcesVO> getMenuListCache(Integer roleId){
        return getMenuList(roleId,0);
    }

    @Override
    @CacheEvict(value = AuthorityCacheServiceImpl.CACHE_VALUE,key = AuthorityCacheServiceImpl.CACHE_KEY)
    public Success deleteVisibleResourceListCache(Integer roleId) {
        logger.info("当前用户ID：{}删除了权限为{}的缓存信息",SubjectUtil.getCurrent().getId(),roleId);
        return Response.SUCCESS;
    }

    @Override
    @CacheEvict(value = AuthorityCacheServiceImpl.CACHE_VALUE,key = AuthorityCacheServiceImpl.CACHE_KEY)
    public void addRoleResourceAndOperationCache(Integer roleId, AddRoleVO addRoleVO) {
        //添加资源和角色的关联关系
        List<Integer> childResIds = addRoleVO.getChildResIds();
        List<AdminResourcesOperationEntity> adminResourcesOperationEntities = new LinkedList<>();
        for (Integer resId: childResIds) {
            adminResourcesOperationEntities.add(AdminResourcesOperationEntity.builder().roleId(roleId).resId(resId).build());
        }
        A.check(Collections.isEmpty(adminResourcesOperationEntities),"请至少勾选一个菜单");
        int i = authorityDao.addResourceRoles(adminResourcesOperationEntities);
        A.check(i < 1,"权限添加失败");
        //添加资源，角色，操作的关系
        List<AdminResourcesOperationEntity> adminResourcesOperationEntities1 = new LinkedList<>();
        List<AddOperationsVo> menuOperations = addRoleVO.getMenuOperations();
        for (AddOperationsVo resourceOperations : menuOperations) {
            List<Integer> operations = resourceOperations.getOperations();
            for (Integer opId : operations) {
                adminResourcesOperationEntities1.add(AdminResourcesOperationEntity.builder().resId(resourceOperations.getResId()).roleId(roleId).opId(opId).build());
            }

        }
        if(!Collections.isEmpty(adminResourcesOperationEntities1)){
            int count = authorityDao.addResOpRole(adminResourcesOperationEntities1);
            A.check(count < 1,"权限添加失败");
        }
    }

    //递归获取当前用户的所有菜单和每个菜单下的权限。
    private List<AdminResourcesVO> getMenuList(Integer roleId, Integer preId){
        List<AdminResourcesVO> adminResourcesVOS = new ArrayList<>();
        //获取当前父ID的菜单
        List<AdminResourcesEntity> adminResourcesEntities = authorityDao.getMenuListByRoleIdAndPreID(roleId,preId);
        for (AdminResourcesEntity adminResourcesEntity:adminResourcesEntities) {
            AdminResourcesVO adminResourcesVO = new AdminResourcesVO();
            BeanUtils.copyProperties(adminResourcesEntity,adminResourcesVO);
            //获取当前菜单的子菜单
            List<AdminResourcesVO> adminResourcesVOList = getMenuList(roleId, adminResourcesVO.getResId());
            if(adminResourcesVOList.isEmpty()){
                //获取当前菜单的权限列表
                List<AdminOperationVO> adminOperationVOS = new ArrayList<>();
                List<AdminOperationEntity> adminOperationEntities = authorityDao.getOperationEntityByResIdAndRoleId(adminResourcesVO.getResId(),roleId);
                adminOperationEntities.forEach(adminOperationEntity -> {
                    AdminOperationVO adminOperationVO = new AdminOperationVO();
                    BeanUtils.copyProperties(adminOperationEntity,adminOperationVO);
                    adminOperationVOS.add(adminOperationVO);
                });
                adminResourcesVO.setMenuOperations(adminOperationVOS);
            }else{
                adminResourcesVO.setChildMenus(adminResourcesVOList);
            }
            adminResourcesVOS.add(adminResourcesVO);
        }
        return adminResourcesVOS;
    }
}
