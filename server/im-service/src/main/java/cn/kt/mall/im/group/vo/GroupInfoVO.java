package cn.kt.mall.im.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GroupInfoVO {
    //群信息

    @ApiModelProperty(name = "groupId", dataType = "string")
    private String groupId;
    @ApiModelProperty(name = "群名", dataType = "string")
    private String name;
    @ApiModelProperty(name = "群主", dataType = "string")
    private String master;
    @ApiModelProperty(name = "头像", dataType = "avatar")
    private String avatar;
    @ApiModelProperty(name = "群公告", dataType = "board")
    private String board;
    @ApiModelProperty(name = "状态", dataType = "status")
    private String status;
    @ApiModelProperty(name = "创建时间", dataType = "Date")
    private Date createTime;
}
