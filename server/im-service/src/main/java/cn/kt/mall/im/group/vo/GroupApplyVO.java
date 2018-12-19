package cn.kt.mall.im.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class GroupApplyVO {

    @ApiModelProperty(notes = "Id", dataType = "string")
    private String id;
    @ApiModelProperty(notes = "群Id", dataType = "string")
    private String groupId;
    @ApiModelProperty(notes = "群名称", dataType = "string")
    private String groupName;
    @ApiModelProperty(notes = "申请人ID", dataType = "string")
    private String userId;
    @ApiModelProperty(notes = "申请人账户", dataType = "string")
    private String username;
    @ApiModelProperty(notes = "申请人手机号", dataType = "string")
    private String mobile;
    @ApiModelProperty(notes = "申请人昵称", dataType = "string")
    private String nick;
    @ApiModelProperty(notes = "申请消息", dataType = "string")
    private String message;
    @ApiModelProperty(notes = "申请状态", dataType = "string")
    private String status;
    @ApiModelProperty(notes = "申请时间", dataType = "string")
    private Date createTime;
}
