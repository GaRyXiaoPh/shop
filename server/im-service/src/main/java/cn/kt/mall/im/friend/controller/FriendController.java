package cn.kt.mall.im.friend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.vo.UserInfoVO;
import cn.kt.mall.im.friend.entity.FriendInvitationEntity;
import cn.kt.mall.im.friend.entity.UserRemarkEntity;
import cn.kt.mall.im.friend.service.FriendService;
import cn.kt.mall.im.friend.vo.FriendInvitationVO;
import cn.kt.mall.im.friend.vo.FriendOprVO;
import cn.kt.mall.im.friend.vo.FriendshipVO;
import cn.kt.mall.im.friend.vo.InviteFriendVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "IM-好友管理", tags = "im-friend")
@RequestMapping("/im/friend")
@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    //获取好友关系分页数据
    @ApiOperation(value = "获取好友列表", notes = "")
    @GetMapping("list")
    @ResponseBody
    public List<FriendshipVO> getFriendshipList() {
        return this.friendService.getFriendsList(SubjectUtil.getCurrent().getId(),0);
    }

    //获取好友数据
    @ApiOperation(value = "获取好友数据", notes = "")
    @GetMapping("friends-filter")
    @ResponseBody
    public List<FriendshipVO> filter(@RequestParam("friends") String friends) {
        String[] friend = friends.split(",");
        return this.friendService.getFriendsFilter(friend,SubjectUtil.getCurrent().getId());
    }

    //获取好友邀请记录分页数据
    @ApiOperation(value = "获取好友邀请记录分页数据", notes = "")
    @GetMapping("invitation/page")
    @ResponseBody
    public PageVO<FriendInvitationVO> getFriendInvitationPage(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return this.friendService.getMyFriendInvitation(SubjectUtil.getCurrent().getId(), pageNo, pageSize);
    }

    //搜索好友信息
    @ApiOperation(value = "根据手机号搜索好友信息", notes = "")
    @GetMapping("search")
    @ResponseBody
    public UserInfoVO searchUserByMobile(@RequestParam("mobile") String mobile){
        UserInfoVO userInfoVO = friendService.searchUserByMobile(mobile);
        userInfoVO.setIsFriend((short) 0);
        return userInfoVO;
    }

    //添加好友(好友邀请)
    @ApiOperation(value = "添加好友(好友邀请)", notes = "")
    @PostMapping("invitation")
    @ResponseBody
    public Success inviteFriend(@RequestBody InviteFriendVO inviteFriendVO) {
        inviteFriendVO.setUserId(SubjectUtil.getCurrent().getId());
        // 添加好友记录
        friendService.inviteFriend(inviteFriendVO);
        return Response.SUCCESS;
    }

    //确认添加好友，result: 结果，2 拒绝, 1 同意
    @ApiOperation(value = "确认添加好友", notes = "result: 结果，1 同意 2 拒绝")
    @PostMapping("invitation/confirm")
    @ResponseBody
    public Success confirmInvite(@RequestBody InviteFriendVO inviteFriendVO) {
        // 确认添加好友请求
        FriendInvitationEntity friendInvitation = this.friendService.confirmInvitation(SubjectUtil.getCurrent().getId(),
                inviteFriendVO.getFriendId(), inviteFriendVO.getResult(),inviteFriendVO.getRemarkName());
        return Response.SUCCESS;
    }

    // 删除好友
    @ApiOperation(value = "删除好友", notes = "")
    @DeleteMapping("{friendId}")
    @ResponseBody
    public Success deleteFriend(@PathVariable("friendId") String friendId) {
        this.friendService.deleteFriend(SubjectUtil.getCurrent().getId(), friendId);
        return Response.SUCCESS;
    }

    // 修改好友信息
    @ApiOperation(value = "修改好友信息", notes = "")
    @PostMapping("{friendId}")
    @ResponseBody
    public Success editFriend(@PathVariable("friendId") String friendId, @RequestBody FriendOprVO friendOprVO) {
        friendOprVO.setFriendId(friendId);
        friendOprVO.setUserId(SubjectUtil.getCurrent().getId());
        this.friendService.updateFriend(friendOprVO);
        return Response.SUCCESS;
    }

    //修改好友备注
    @ApiOperation(value = "修改好友备注", notes = "")
    @PostMapping("updateRemark")
    @ResponseBody
    public Success updateRemark(@RequestParam("friendId") String friendId,@RequestParam("remarkName") String remarkName) {
        UserRemarkEntity userRemarkEntity = new UserRemarkEntity();
        userRemarkEntity.setUserId(SubjectUtil.getCurrent().getId());
        userRemarkEntity.setFriendId(friendId);
        userRemarkEntity.setRemarkName(remarkName);
        this.friendService.updateRemark(userRemarkEntity);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "获取好友信息", notes = "")
    @GetMapping("getFriendInfo")
    @ResponseBody
    public FriendshipVO getFriendInfo(@RequestParam("friendId") String friendId){
        return friendService.getFriendMessage(SubjectUtil.getCurrent().getId(),friendId);
    }

}
