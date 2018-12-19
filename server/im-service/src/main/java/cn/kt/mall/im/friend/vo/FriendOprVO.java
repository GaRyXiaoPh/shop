package cn.kt.mall.im.friend.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 好友关系操作VO
 * Created by Administrator on 2017/7/5.
 */
@Setter
@Getter
@NoArgsConstructor
public class FriendOprVO {

    @ApiModelProperty(notes = "用户Id", dataType = "string")
    private String userId;
    @ApiModelProperty(notes = "好友Id", dataType = "string")
    private String friendId;
    @ApiModelProperty(notes = "星级好友: 1 是，0 否", dataType = "string")
    private String star;
    @ApiModelProperty(notes = "不看他的朋友圈:1 是，0 否", dataType = "string")
    private String invisibleFriend;
    @ApiModelProperty(notes = "不让他看我的朋友圈:1 是，0 否", dataType = "string")
    private String invisibleMe;

}
