package cn.kt.mall.management.admin.dao;

import cn.kt.mall.management.admin.entity.*;
import cn.kt.mall.management.admin.vo.AdminResourceVO;
import cn.kt.mall.management.admin.vo.RoleResVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/6/4.
 */
@Mapper
public interface AuthorityDao {

    /**
     * 获取权限列表
     *
     * @param resLevel
     * @param preId
     * @return
     */
    List<AuthorityEntity> getAuthority(@Param("resLevel") Integer resLevel,@Param("preId") Integer preId);

    /**
     * 添加角色
     *
     * @param name
     * @param creator
     * @param des
     * @return
     */
    int addRole(@Param("name") String name,@Param("creator") String creator,@Param("des") String des);

    /**
     * 获取角色列表
     *
     * @return
     */
    List<RoleEntity> getRoleList();

    RoleEntity getRoleByName(@Param("name")  String name);

    /**
     * 编辑角色
     *
     * @param name
     * @param des
     * @param roleId
     * @return
     */
    int  updateRole(@Param("name") String name,
                    @Param("des") String des,
                    @Param("roleId") Integer roleId,
                    @Param("status") Integer status);


    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    int delRole(@Param("roleId") Integer roleId);

    /**
     * 添加角色与资源的关系
     *
     * @return
     */
    int addRoleRes(@Param("roleId") Integer roleId,@Param("resId") Integer resId);

    /**
     * 添加角色，资源，操作的关系
     *
     * @return
     */
    int addResOpRole(@Param("list")List<AdminResourcesOperationEntity> adminResourcesOperationEntitys);

    /**
     * 获取角色与资源的关系列表
     *
     * @param roleId
     * @return
     */
    List<RoleResVO> getRoleResList(@Param("roleId") Integer roleId);

    /**
     * 删除角色与资源的关系
     *
     * @param roleId
     * @return
     */
    int delRoleRes(@Param("roleId") Integer roleId);

    /**
     * 批量删除用户权限
     * @param list
     * @return
     */
    int delUserRoles(@Param("list")List<String> list);

    /**
     * 添加用户与角色关系
     *
     * @param userId
     * @param roleId
     * @return
     */
    int addAdminRole(@Param("userId") String userId,@Param("roleId") Integer roleId);

    /**
     * 批量添加用户与角色关系
     *
     * @param list
     * @return
     */
    int addResourceRoles(@Param("list") List<AdminResourcesOperationEntity> list);

    /**
     * 删除角色与资源的关系
     *
     * @param roleId
     * @return
     */
    int delRoleResOp(@Param("roleId") Integer roleId);

    /**
     * 添加角色与资源的关系
     *
     * @return
     */
    List<AdminResourcesEntity> getRoleRes(@Param("roleId") Integer roleId,@Param("resId") Integer resId);

    List<AdminResourceVO> getResourceList();

    List<RoleResVO> getAllRoleResList();

    List<AdminResourcesEntity> getMenuListByRoleIdAndPreID(@Param("roleId") Integer roleId,@Param("preId")Integer preId);

    List<RoleEntity> getUserRoleId(@Param("userId") String userId);

    List<AdminResourcesOperationEntity> getResourcesOperationEntitys();

    List<AdminOperationEntity> getOperationEntitys();

    List<AdminOperationEntity> getOperationEntityByResIdAndRoleId(@Param("resId") Integer resId, @Param("roleId") Integer roleId);

    List<AdminOperationEntity> getOperationEntityByResIdAndRoleIdAndOpId(@Param("resId") Integer resId, @Param("roleId") Integer roleId,@Param("opId")Integer opId);

}
