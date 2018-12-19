package cn.kt.mall.im.friend.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 邀请好友VO
 * Created by Administrator on 2017/6/18.
 */
@Getter
@Setter
public class InviteFriendVO {
    @ApiModelProperty(notes = "用户Id", dataType = "string")
    private String userId;
    @ApiModelProperty(notes = "好友Id", dataType = "string")
    private String friendId;
    @ApiModelProperty(notes = "申请消息", dataType = "string")
    private String message;
    @ApiModelProperty(notes = "（应答必填）拒绝或者同意:1 同意 2 拒绝", dataType = "string")
    private String result;
    @ApiModelProperty(notes = "好友名称备注", dataType = "string")
    private String remarkName;
}
