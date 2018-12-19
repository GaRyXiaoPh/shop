package cn.kt.mall.shop.good.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GoodShopCenterEntity implements Serializable {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("商品id")
    private String goodId;
    @ApiModelProperty("店铺商品状态")
    private String goodStatus;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("修改时间")
    private Date modifyTime;
    @ApiModelProperty("商品销售数量")
    private int sales;
}
