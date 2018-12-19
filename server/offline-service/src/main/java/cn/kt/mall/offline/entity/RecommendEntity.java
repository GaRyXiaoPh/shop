package cn.kt.mall.offline.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/21.
 */
@Setter
@Getter
public class RecommendEntity implements Serializable {

    private static final long serialVersionUID = 1258045404790546043L;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("头像")
    private String avatar;
}
