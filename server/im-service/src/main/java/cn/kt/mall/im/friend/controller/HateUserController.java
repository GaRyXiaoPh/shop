package cn.kt.mall.im.friend.controller;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.user.vo.UserInfoVO;
import cn.kt.mall.im.rong.service.RongCloudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(description = "IM-黑名单管理", tags = "im-hate")
@RequestMapping("/im/hate")
@RestController
public class HateUserController {

    @Autowired
    RongCloudService rongCloudService;

    @Autowired
    UserDAO userDAO;

    //获取黑名单列表
    @ApiOperation(value = "获取黑名单列表", notes = "")
    @GetMapping("list")
    @ResponseBody
    public PageVO<UserInfoVO> getHateUserList(@RequestParam("pageNo")int pageNo, @RequestParam("pageSize")int pageSize) {
        int srcPageNo = pageNo;
        if (pageNo>0) pageNo=pageNo-1;
        int offset = pageNo*pageSize;
        String[] hates = rongCloudService.getHateList(SubjectUtil.getCurrent().getId());
        int count = hates.length;

        List<UserInfoVO> list = new ArrayList<UserInfoVO>();
        for (int i = offset; i < offset+pageSize; i++){
            UserEntity user = userDAO.getById(hates[i]);
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setUserId(user.getId());
            userInfoVO.setAvatar(user.getAvatar());
            userInfoVO.setMobile(user.getMobile());
            userInfoVO.setNick(user.getNick());
            userInfoVO.setUsername(user.getUsername());
            list.add(userInfoVO);
        }
        return new PageVO<UserInfoVO>(srcPageNo, pageSize, count, list);
    }

    //添加黑名单
    @ApiOperation(value = "添加黑名单", notes = "")
    @GetMapping("add")
    @ResponseBody
    public Success addHateUser(@RequestParam("hateId")String hateId) {
        rongCloudService.addHate(SubjectUtil.getCurrent().getId(), hateId);
        return Response.SUCCESS;
    }

    //移除黑名单
    @ApiOperation(value = "移除黑名单", notes = "")
    @GetMapping("remove")
    @ResponseBody
    public Success removeHateUser(@RequestParam("hateId")String hateId){
        rongCloudService.removeHate(SubjectUtil.getCurrent().getId(), hateId);
        return Response.SUCCESS;
    }

}
