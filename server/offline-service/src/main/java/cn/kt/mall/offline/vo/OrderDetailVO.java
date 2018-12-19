package cn.kt.mall.offline.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/5/14.
 */
@Setter
@Getter
public class OrderDetailVO {

    @ApiModelProperty("商品id")
    private String goodNo;

    @ApiModelProperty("商品名称")
    private String goodName;

    @ApiModelProperty("商品主图")
    private String goodMainPic;

    @ApiModelProperty("商品价格")
    private double price;

    @ApiModelProperty("商品数量")
    private Integer num;


}
