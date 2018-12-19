package cn.kt.mall.im.friend.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 添加好友记录VO
 * Created by Administrator on 2017/6/18.
 */

@Getter
@Setter
@NoArgsConstructor
public class FriendInvitationVO implements Serializable{
    private static final long serialVersionUID = -4008080132416968132L;

    @ApiModelProperty(notes = "id", dataType = "string")
    private String invitationId;
    @ApiModelProperty(notes = "好友ID", dataType = "string")
    private String id;//历史friendId
    @ApiModelProperty(notes = "好友名称", dataType = "string")
    private String username;//历史friendName
    @ApiModelProperty(notes = "手机号", dataType = "string")
    private String mobile;
    @ApiModelProperty(notes = "消息", dataType = "string")
    private String message;
    @ApiModelProperty(notes = "头像", dataType = "string")
    private String avatar;
    @ApiModelProperty(notes = "状态：0 已提交, 1 同意, 2 拒绝", dataType = "string")
    private String status;      /** 状态：0 已提交, 1 同意, 2 拒绝 */
}
