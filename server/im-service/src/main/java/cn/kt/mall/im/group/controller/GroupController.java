package cn.kt.mall.im.group.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.im.group.entity.GroupChatEntity;
import cn.kt.mall.im.group.service.GroupService;
import cn.kt.mall.im.group.vo.GroupApplyVO;
import cn.kt.mall.im.group.vo.MembersUpdateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(description = "IM-群管理", tags = "im-group")
@RequestMapping("/im/group")
@RestController
public class GroupController {

    @Autowired
    GroupService groupService;

    //获取群信息
    @ApiOperation(value = "获取群信息", notes = "")
    @GetMapping("getGroupChat")
    @ResponseBody
    public GroupChatEntity getGroupChat(@RequestParam(name = "groupId") String groupId){
        GroupChatEntity groupChatEntity = groupService.getGroupChat(groupId,SubjectUtil.getCurrent().getId());
        A.checkParam(groupChatEntity == null, "该群不存在 ");
        groupChatEntity.setMemberList(groupService.getGroupMemberList(groupId,SubjectUtil.getCurrent().getId()));
        return groupChatEntity;
    }

    //修改群信息
    @ApiOperation(value = "修改群信息", notes = "")
    @PostMapping("/fixGroupChat")
    @ResponseBody
    public Success fixGroupChat(@RequestParam("groupId") String groupId,
                                @RequestParam("groupName")String groupName,
                                @RequestParam("board")String board){
        groupService.fixGroupChat(SubjectUtil.getCurrent().getId(), groupId, groupName, board);
        return Response.SUCCESS;
    }

    //创建群
    @ApiOperation(value = "创建群", notes = "")
    @PostMapping("/createGroupChat")
    @ResponseBody
    public Map createGroupChat(@RequestParam("userIds") String userIds){
        String groupName = "群聊";
        userIds = userIds +","+ SubjectUtil.getCurrent().getId();
        String newUserIds [ ] = userIds.split(",");
        String groupId = IDUtil.getUUID();
        groupService.createGroupChat(SubjectUtil.getCurrent().getId(), newUserIds,groupId,groupName);
        Map map = new HashMap<>();
        map.put("message","success");
        map.put("groupId",groupId);
        map.put("groupName",groupName);
        return map;
    }

    //获取我加入的群
    @ApiOperation(value = "获取我加入的群", notes = "")
    @GetMapping("/getMyGroupChat")
    @ResponseBody
    public PageVO<GroupChatEntity> getMyGroupChat(@RequestParam(name = "pageNo") int pageNo,
                                                  @RequestParam(name = "pageSize") int pageSize){
        return groupService.getMyGroupChat(SubjectUtil.getCurrent().getId(), pageNo, pageSize);
    }

    //申请加群
    @ApiOperation(value = "申请加群", notes = "")
    @PostMapping("/joinGroupChat")
    @ResponseBody
    public Success joinGroupChat(@RequestParam(name = "groupId") String groupId,
                                 @RequestParam(name = "message") String message){
        groupService.joinGroupChat(SubjectUtil.getCurrent().getId(), groupId, message);
        return Response.SUCCESS;
    }

    //获取待我审批的群
    @ApiOperation(value = "获取待我审批的群", notes = "")
    @GetMapping("/getMyGroupApply")
    @ResponseBody
    public PageVO<GroupApplyVO> getMyGroupApply(@RequestParam(name = "groupId") String groupId,
                                                        @RequestParam(name = "pageNo") int pageNo,
                                                        @RequestParam(name = "pageSize") int pageSize){
        ;
        return groupService.getMyGroupApply(SubjectUtil.getCurrent().getId(), groupId, pageNo, pageSize);
    }

    //同意加群
    @ApiOperation(value = "同意加群", notes = "status， 1同意， 2拒绝")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "申请ID", name = "id", paramType = "query", required = true),
            @ApiImplicitParam(value = "1同意， 2拒绝", name = "status", paramType = "query", required = true)
    })
    @PostMapping("/confirm")
    @ResponseBody
    public Success confirmMyGroupApply(@RequestParam("id")String applyId,
                                       @RequestParam("status")String status) {
        groupService.confirmMyGroupApply(SubjectUtil.getCurrent().getId(), applyId, status);
        return Response.SUCCESS;
    }

    //退群
    @ApiOperation(value = "退群", notes = "")
    @PostMapping("/exitGroupChat")
    @ResponseBody
    public Success exitGroupChat(@RequestParam(name = "groupId") String groupId){
        groupService.exitGroupChat(SubjectUtil.getCurrent().getId(), groupId);
        return Response.SUCCESS;
    }

    //解散群
    @ApiOperation(value = "解散群", notes = "")
    @PostMapping("/disGroupChat")
    @ResponseBody
    public Success disGroupChat(@RequestParam(name = "groupId") String groupId){
        groupService.disGroupChat(SubjectUtil.getCurrent().getId(), groupId);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "更新成员", notes = "")
    @PostMapping("/updateMember")
    @ResponseBody
    public Success updateMember(@RequestBody MembersUpdateVo membersUpdateVo){
        int count = groupService.updateMember(SubjectUtil.getCurrent().getId(),membersUpdateVo);
        A.check(count<1,"更新失败");
        return Response.SUCCESS;
    }

    @ApiOperation(value = "更新昵称", notes = "")
    @GetMapping("/updateNick")
    @ResponseBody
    public Success updateNick(@RequestParam(name = "groupId") String groupId,
                              @RequestParam(name = "nickname") String nickname){
        groupService.updateNick(nickname,groupId,SubjectUtil.getCurrent().getId());
        return Response.SUCCESS;
    }

    @ApiOperation(value = "群主权限过度", notes = "")
    @GetMapping("/overAuthority")
    @ResponseBody
    public Success overAuthority(@RequestParam(name = "groupId") String groupId,
                                 @RequestParam(name = "newUserId") String newUserId){
        groupService.overAuthority(groupId,SubjectUtil.getCurrent().getId(),newUserId);
        return Response.SUCCESS;
    }


}
