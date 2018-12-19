package cn.kt.mall.management.user.vo;

import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.user.model.UserLevel;
import cn.kt.mall.common.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("关系图信息")
@Getter
@Setter
@NoArgsConstructor
public class RelationshipVO {

    @ApiModelProperty("用户Id")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nick;

    @ApiModelProperty("用户等级")
    private String level;

    @ApiModelProperty("用户等级名称")
    private String levelName;

    @ApiModelProperty("代(第几代用户)")
    private int generation;

    @ApiModelProperty("注册时间")
    private String createTime;

    public RelationshipVO(UserEntity entity) {
        super();
        this.id = entity.getId();
        this.username = entity.getUsername() == null ? entity.getMobile() : entity.getUsername();
        this.nick = entity.getNick();

        UserLevel userLevel = UserLevel.getByCode(entity.getLevel());
        userLevel = userLevel == null ? UserLevel.DEFAULT : userLevel;

        this.level = userLevel.getCode();
        this.levelName = userLevel.getName();

        if(entity.getCreateTime() != null) {
            this.createTime = DateUtil.getTime(entity.getCreateTime());
        }
    }

    public RelationshipVO(UserEntity entity, int generation) {
        this(entity);
        this.generation = generation;
    }
}
