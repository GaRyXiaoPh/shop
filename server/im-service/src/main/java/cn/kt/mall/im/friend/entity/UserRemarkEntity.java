package cn.kt.mall.im.friend.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 备注用户信息
 * Created by Administrator on 2017/6/18.
 */

@Setter
@Getter
public class UserRemarkEntity {

    private String id;

    /** 用户Id: 被标记用户的Id */
    @ApiModelProperty(notes = "用户id", dataType = "string")
    private String userId;

    /** 创建人 */
    @ApiModelProperty(notes = "朋友id", dataType = "string")
    private String friendId;

    /** 备注名 */
    @ApiModelProperty(notes = "备注名", dataType = "string")
    private String remarkName;
}
