package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.admin.model.AdminInfo;
import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.user.vo.PasswordVO;
import cn.kt.mall.management.admin.vo.AddAdminVO;
import cn.kt.mall.management.jwt.Subject;
import cn.kt.mall.management.admin.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户模块 Controller
 * Created by jerry on 2017/12/21.
 */
@Api(description = "管理员-信息管理", tags = "management-info")
@RequestMapping("/admin")
@RestController
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation(value = "根据用户Id获取用户信息", notes = "返回:用户信息")
    @GetMapping("{id}")
    public AdminInfo getUser(@PathVariable("id") String id) {
        return this.adminService.getUserById(id);
    }

   /* @ApiOperation(value = "忘记密码")
    @PostMapping("password")
    public void editPassword(@RequestBody PasswordVO passwordVO) {
        this.adminService.updatePassword(Subject.getCurrent(), passwordVO);
    }*/
   @ApiOperation(value = "忘记密码")
   @PostMapping("password")
   public void editPassword(@RequestParam(name = "oldPassword", required = false) String oldPassword,
                            @RequestParam(name = "password", required = false) String password) {
       PasswordVO passwordVO = new PasswordVO();
       passwordVO.setOldPassword(oldPassword);
       passwordVO.setPassword(password);
       this.adminService.updatePassword(Subject.getCurrent(), passwordVO);
   }

    @ApiOperation(value = "添加账号")
    @PostMapping("add")
    public Success add(@RequestBody AddAdminVO adminVO) {
        adminService.add(adminVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "删除账号")
    @PostMapping("deleteUser")
    public Success deleteUser(@RequestBody String[] userIds) {
        A.check(userIds == null || userIds.length == 0, "请选择至少一个账号进行删除");
        adminService.deleteUser(userIds);
        return Response.SUCCESS;
    }


    @ApiOperation(value = "获取管理用户列表")
    @GetMapping("users")
    public List<AdminInfo> getUserList() {
        return this.adminService.getUserList();
    }

}
