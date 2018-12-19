package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.management.admin.entity.RoleEntity;
import cn.kt.mall.management.admin.service.AuthorityService;
import cn.kt.mall.management.admin.vo.AddOperationsVo;
import cn.kt.mall.management.admin.vo.AddRoleVO;
import cn.kt.mall.management.admin.vo.AdminResourcesVO;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Created by Administrator on 2018/6/4.
 */
@Api(description = "权限", tags = "authority")
@RequestMapping("/manage/authority")
@RestController
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;


    @ApiOperation(value = "添加角色")
    @PostMapping("addRole")
    public Success addRole(@RequestBody AddRoleVO addRoleVOS) {
        int roleId = authorityService.addRole(addRoleVOS);
        authorityService.loadResourceCache(roleId);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "获取角色列表")
    @GetMapping("roleList")
    public List<RoleEntity> getRoleList() {
        return authorityService.getRoleList();
    }


    @ApiOperation(value = "编辑角色")
    @GetMapping("updateRole")
    public Success updateRole(@ApiParam(value = "角色id") @RequestParam Integer roleId,
                              @ApiParam(value = "角色名称") @RequestParam String name,
                              @ApiParam(value = "角色描述") @RequestParam String des,
                              @ApiParam(value = "角色状态(0:正常  1:禁用)") @RequestParam Integer status) {
        authorityService.updateRole(name,des,roleId,status);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "编辑权限")
    @PostMapping("updateRoleList/{roleId}")
    public Success updateRoleList(@PathVariable(value = "roleId") Integer roleId,@RequestBody AddRoleVO addRoleVO){
        A.check(roleId < 0,"请输入权限ID");
        List<Integer> childResIds = addRoleVO.getChildResIds();
        List<AddOperationsVo> addOperationsVos = addRoleVO.getMenuOperations();
        A.check(Collections.isEmpty(childResIds), "资源列表为空");
        authorityService.updateRoleList(roleId,addRoleVO);
        authorityService.loadResourceCache(roleId);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("delRole")
    public Success delRole(@ApiParam(value = "角色id") @RequestParam Integer roleId) {
         authorityService.delRole(roleId);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "获取当前用户的权限列表")
    @GetMapping("visibleResources")
    public AdminResourcesVO getVisibleResourceList() {
        AdminResourcesVO adminResourcesVO = new AdminResourcesVO();
        adminResourcesVO.setResId(0);
        adminResourcesVO.setChildMenus(authorityService.getVisibleResourceList());
        return adminResourcesVO;
    }

    @ApiOperation(value = "获取当前用户的权限列表")
    @GetMapping("visibleSelectedResources")
    public AdminResourcesVO visibleSelectedResources(@RequestParam("roleId") Integer roleId) {
        return authorityService.visibleSelectedResources(roleId);
    }
    @ApiOperation(value = "手动删除删除当前用户的权限列表缓存")
    @DeleteMapping("visibleResourcesCache")
    public Success deleteVisibleResourceListCache() {
        return authorityService.deleteVisibleResourceListCache();
    }
}
