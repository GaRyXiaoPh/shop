package cn.kt.mall.management.admin.service;


import cn.kt.mall.common.http.Success;
import cn.kt.mall.management.admin.entity.RoleEntity;
import cn.kt.mall.management.admin.vo.AddRoleVO;
import cn.kt.mall.management.admin.vo.AdminResourceVO;
import cn.kt.mall.management.admin.vo.AdminResourcesVO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/4.
 */
public interface AuthorityService {


    /**
     * 添加角色
     * @param addRoleVOS 添加权限资源列表
     */
    int addRole(AddRoleVO addRoleVOS);

    /**
     * 获取角色列表
     *
     * @return
     */
    List<RoleEntity> getRoleList();

    /**
     * 更新角色
     *
     * @param name
     * @param des
     * @param roleId
     * @param status
     */
    void updateRole(String name,String des,Integer roleId,Integer status);

    /**
     * 更新角色权限
     *
     * @param roleId
     * @param addRoleVO
     */
    void updateRoleList(Integer roleId,AddRoleVO addRoleVO);

    /**
     * 删除角色
     *
     * @param roleId
     */
    void delRole(Integer roleId);


    /**
     * 获取当前角色的资源列表
     * @return
     */
    List<AdminResourcesVO> getVisibleResourceList();

    /**
     * 获取修改页面的资源列表
     * @return
     */
    AdminResourcesVO visibleSelectedResources(Integer roleId);

    /**
     * 删除当前用户的角色权限缓存
     */
    Success deleteVisibleResourceListCache();

     void loadResourceCache(Integer roleId);
}
