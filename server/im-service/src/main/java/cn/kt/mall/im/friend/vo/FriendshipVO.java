package cn.kt.mall.im.friend.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 好友关系VO
 * Created by Administrator on 2017/6/18.
 */
@Setter
@Getter
public class FriendshipVO implements Serializable {
    private static final long serialVersionUID = 1006817797370994388L;

    @ApiModelProperty(notes = "朋友Id", dataType = "string")
    private String userId;
    @ApiModelProperty(notes = "朋友名称", dataType = "string")
    private String username;
    @ApiModelProperty(notes = "备注名", dataType = "string")
    private String remarkName;
    @ApiModelProperty(notes = "手机号", dataType = "string")
    private String mobile;
    @ApiModelProperty(notes = "头像url", dataType = "string")
    private String avatar;
    @ApiModelProperty(notes = "星级好友：1 是, 0 否", dataType = "string")
    private String star;
    @ApiModelProperty(notes = "昵称", dataType = "string")
    private String nick;
    @ApiModelProperty(notes = "群昵称", dataType = "string")
    private String groupNickname;
    @ApiModelProperty(notes = "是否是群主(1:群主)", dataType = "string")
    private String isManage;
    @ApiModelProperty(notes = "是否是好友(是:true 不是:false)", dataType = "boolean")
    private Boolean flag;
}
