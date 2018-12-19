package cn.kt.mall.common.user.vo;

import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.user.model.UserLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 下级用户信息
 * Created by wqt on 2018/2/7.
 */
@ApiModel("下级用户信息")
@Getter
@Setter
@NoArgsConstructor
public class SlaveVO {

    @ApiModelProperty(notes = "用户Id", dataType = "string")
    private String id;

    @ApiModelProperty(notes = "手机号码", dataType = "string")
    private String mobile;

    @ApiModelProperty(notes = "用户等级(编码)：l1 - l10", dataType = "string")
    private String level;

    @ApiModelProperty(notes = "用户等级(名称)：1级 - 10级", dataType = "string")
    private String levelName;

    public SlaveVO(UserEntity entity) {
        super();
        this.id = entity.getId();
        this.mobile = entity.getMobile();
        this.level = entity.getLevel();
        UserLevel level = UserLevel.getByCode(this.level);
        this.levelName = level == null ? "普通用户" : level.getName();
    }
}
