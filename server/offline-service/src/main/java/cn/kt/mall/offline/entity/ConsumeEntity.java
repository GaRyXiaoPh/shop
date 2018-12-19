package cn.kt.mall.offline.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 消费实体类
 *
 * Created by Administrator on 2018/5/23.
 */
@Getter
@Setter
public class ConsumeEntity  implements Serializable{

    private static final long serialVersionUID = -8133535173975332148L;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("消费莱姆币")
    private double lemy;

    @ApiModelProperty("推荐人")
    private String referrer;
}
