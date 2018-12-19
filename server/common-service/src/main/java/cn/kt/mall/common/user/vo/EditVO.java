package cn.kt.mall.common.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 编辑个人信息VO
 * Created by wqt on 2018/1/26.
 */
@ApiModel(description = "编辑个人信息")
@Getter
@Setter
@NoArgsConstructor
public class EditVO {

    @ApiModelProperty(hidden = true)
    private String userId;

    @ApiModelProperty(notes = "昵称", dataType = "string")
    private String nick;

    @ApiModelProperty(notes = "头像", dataType = "string")
    private String avatar;
}
