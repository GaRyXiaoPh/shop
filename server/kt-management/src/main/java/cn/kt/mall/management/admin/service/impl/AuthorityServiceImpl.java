package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.management.admin.dao.AuthorityDao;
import cn.kt.mall.management.admin.entity.AdminOperationEntity;
import cn.kt.mall.management.admin.entity.AdminResourcesEntity;
import cn.kt.mall.management.admin.entity.AdminResourcesOperationEntity;
import cn.kt.mall.management.admin.entity.RoleEntity;
import cn.kt.mall.management.admin.service.AuthorityCacheService;
import cn.kt.mall.management.admin.service.AuthorityService;
import cn.kt.mall.management.admin.vo.*;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/6/4.
 */
@Service("authorityService")
public class AuthorityServiceImpl implements AuthorityService {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private AuthorityCacheService authorityCacheService;

    @Autowired
    private AuthorityDao authorityDao;


    @Override
    @Transactional
    public int addRole(AddRoleVO addRoleVO) {
        String name = addRoleVO.getName();
        RoleEntity oldEntity = authorityDao.getRoleByName(name);
        A.check(oldEntity != null ,"同名权限已经存在");
        String  userId = SubjectUtil.getCurrent().getId();
        int count = authorityDao.addRole(name, userId,"");
        A.check(count<1,"添加角色失败!");
        oldEntity = authorityDao.getRoleByName(name);
        authorityCacheService.addRoleResourceAndOperationCache(oldEntity.getRoleId(),addRoleVO);
        return oldEntity.getRoleId();
        //加载缓存
    }

    @Override
    public List<RoleEntity> getRoleList() {
        return authorityDao.getRoleList();
    }

    @Override
    @Transactional
    public void updateRole(String name, String des, Integer roleId, Integer status) {
        int  count = authorityDao.updateRole(name,des,roleId,status);
        A.check(count<1,"更新角色失败!");
    }
    @Transactional
    @Override
    public void updateRoleList(Integer roleId,AddRoleVO addRoleVO) {
        //删除权限对应的资源
        int roleResCount = authorityDao.delRoleRes(roleId);
        A.check(roleResCount < 1,"  编辑角色失败!");
        //删除权限对应的操作
        int roleResOpCount = authorityDao.delRoleResOp(roleId);
        A.check(roleResOpCount < 1,"编辑角色失败!");
        //创建新的关联关系
        authorityCacheService.addRoleResourceAndOperationCache(roleId,addRoleVO);
    }

    @Override
    @Transactional
    @CacheEvict(value = AuthorityCacheServiceImpl.CACHE_VALUE,key = AuthorityCacheServiceImpl.CACHE_KEY)
    public void delRole(Integer roleId) {
        //删除当前权限表
        int  roleCount = authorityDao.delRole(roleId);
        A.check(roleCount < 1,"删除角色失败!");
        //删除权限对应的资源
        int roleResCount = authorityDao.delRoleRes(roleId);
        A.check(roleResCount < 1,"删除角色失败!");
        //删除权限对应的操作
        int roleResOpCount = authorityDao.delRoleResOp(roleId);
        A.check(roleResOpCount < 1,"删除角色失败!");
    }

    @Override
    public List<AdminResourcesVO> getVisibleResourceList() {
        //获取当前用户信息
        String userId = SubjectUtil.getCurrent().getId();
        //获取当前用户的角色ID
        List<RoleEntity> roleIds = authorityDao.getUserRoleId(userId);
        Integer roleId = 0;
        if(roleIds != null && !roleIds.isEmpty()){
            roleId = roleIds.get(0).getRoleId();
        }
        return authorityCacheService.getMenuListCache(roleId);
    }

    @Override
    public AdminResourcesVO visibleSelectedResources(Integer roleId) {
        String userId = SubjectUtil.getCurrent().getId();
        //获取当前用户的角色ID
        List<RoleEntity> roleIds = authorityDao.getUserRoleId(userId);
        Integer currentRoleId = 0;
        if(roleIds != null && !roleIds.isEmpty()){
            currentRoleId = roleIds.get(0).getRoleId();
        }
        //获取当前操作用户所有权限列表
        List<AdminResourcesVO> adminResourcesVOS = authorityCacheService.getMenuListCache(currentRoleId);
        AdminResourcesVO adminResourcesVO = new AdminResourcesVO();
        adminResourcesVO.setResId(0);
        adminResourcesVO.setChildMenus(adminResourcesVOS);
        adminResourcesVO.setSelected(true);
        return filterSelectedResourceList(adminResourcesVO,roleId);
    }

    @Override
    public Success deleteVisibleResourceListCache() {
        //获取当前用户的角色ID
        List<RoleEntity> roleIds = authorityDao.getUserRoleId(SubjectUtil.getCurrent().getId());
        Integer roleId = 0;
        if(roleIds != null && !roleIds.isEmpty()){
            roleId = roleIds.get(0).getRoleId();
        }
        Success success = authorityCacheService.deleteVisibleResourceListCache(roleId);
        loadResourceCache(roleId);
        return success;
    }
    private AdminResourcesVO filterSelectedResourceList(AdminResourcesVO adminResourcesVO,Integer roleId){
        List<AdminResourcesVO> adminResourcesVOS = adminResourcesVO.getChildMenus();
        List<AdminOperationVO> adminOperationVOS = adminResourcesVO.getMenuOperations();
        //查询当前是否拥有当前菜单权限
        List<AdminResourcesEntity> adminResourcesEntities = authorityDao.getRoleRes(roleId,adminResourcesVO.getResId());
        if(!Collections.isEmpty(adminResourcesEntities)){
            adminResourcesVO.setSelected(true);
        }
        //如果子菜单不为空则继续循环子菜单
        if(!Collections.isEmpty(adminResourcesVOS)){
            adminResourcesVO.setChildMenus(adminResourcesVOS);
            adminResourcesVOS.forEach(adminResourcesVO1 -> filterSelectedResourceList(adminResourcesVO1,roleId));
        }else if(!Collections.isEmpty(adminOperationVOS)){
            //循环查询当前操作是否拥有权限
            adminOperationVOS.forEach(adminOperationVO -> {
                if(adminResourcesVO.getSelected()){
                    List<AdminOperationEntity> adminOperationEntityList= authorityDao.getOperationEntityByResIdAndRoleIdAndOpId(adminResourcesVO.getResId(),roleId,adminOperationVO.getOpId());
                    if(!Collections.isEmpty(adminOperationEntityList)){
                        adminOperationVO.setSelected(true);
                    }
                }
                adminResourcesVO.setMenuOperations(adminOperationVOS);
            });
        }
        return adminResourcesVO;
    }

    public void loadResourceCache(Integer roleId){
        executorService.execute(() -> authorityCacheService.getMenuListCache(roleId));
    }
}
