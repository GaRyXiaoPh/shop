package cn.kt.mall.management.user.controller;

import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.jwt.PermissionCheck;
import cn.kt.mall.management.user.service.MgrUserService;
import cn.kt.mall.management.user.vo.RelationshipVO;
import cn.kt.mall.management.user.vo.UserAddressVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(description = "用户信息", tags = "user-info")
@RequestMapping("/user")
@RestController
public class UserController {

    private MgrUserService mgrUserService;

    @Autowired
    public UserController(MgrUserService mgrUserService) {
        this.mgrUserService = mgrUserService;
    }

    @ApiOperation(value = "获取账户地址", notes = "返回:用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "账户类型：lem,aec,btc,ltc", paramType = "query")
    })
    @PermissionCheck
    @GetMapping("address")
    public UserAddressVO getAddress(@RequestParam("mobile") @ApiIgnore String mobile,
                                    @RequestParam("type") @ApiIgnore String type) {
        return this.mgrUserService.getAddress(mobile, type);
    }

    @ApiOperation(value = "获取直接下级用户关系", notes = "返回:用户关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id，非必填，不填返回顶级用户信息", paramType = "query")
    })
    @IgnoreJwtAuth
    @GetMapping("slave/list")
    public List<RelationshipVO> getDirectSlave(@RequestParam(value = "userId", required = false) @ApiIgnore String userId) {
        return this.mgrUserService.getDirectSlave(userId);
    }
}
