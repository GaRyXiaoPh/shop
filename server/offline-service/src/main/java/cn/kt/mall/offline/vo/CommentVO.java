package cn.kt.mall.offline.vo;

import cn.kt.mall.common.util.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */
@Setter
@Getter
public class CommentVO implements Serializable{

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("店铺id")
    private String shopId;

    @ApiModelProperty("评分")
    private Integer score;

    @ApiModelProperty("评价")
    private String content;

    @ApiModelProperty("图片")
    private List<String> pics;

    @ApiModelProperty("是否匿名(0:匿名  1:实名)")
    private Integer flag;

}
