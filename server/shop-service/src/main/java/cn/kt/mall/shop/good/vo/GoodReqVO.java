package cn.kt.mall.shop.good.vo;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import cn.kt.mall.shop.good.entity.GoodPayEntity;
import org.hibernate.validator.constraints.NotBlank;

import cn.kt.mall.shop.good.entity.GoodPropertyEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GoodReqVO {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("商品一级分类")
    @NotBlank
    private String firstGoodType;
    @ApiModelProperty("商品二级分类")
    @NotBlank
    private String secondGoodType;
    @ApiModelProperty("商品三级分类")
    @NotBlank
    private String thirdGoodType;
    @ApiModelProperty("商品名称")
    @NotBlank
    private String goodName;
    @ApiModelProperty("商品价格")
    @NotBlank
    private BigDecimal goodPrice;
    /*@ApiModelProperty("库存")
    @NotNull
    private Integer goodStock;*/
    @ApiModelProperty("运费")
    @NotNull
    private BigDecimal freightFree;
    @ApiModelProperty("商品属性集合")
    private List<GoodPropertyEntity> goodPropertys;
    @ApiModelProperty("商品图片集合")
    private String goodImgs;
    @ApiModelProperty("商品描述图片集合")
    private String goodDetails;
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("优惠卷集合")
    private String couponIdList;
    @ApiModelProperty("商品单位")
    private String unit;
    @ApiModelProperty("解冻比率")
    private BigDecimal unfreezeRatio;
    @ApiModelProperty("优惠券数量")
    private BigDecimal couponNum;
    @ApiModelProperty("商品编号")
    private String goodNo;
    @ApiModelProperty("商品描述")
    private String describes;
    @ApiModelProperty("支付方式")
    private String payIds;
    @ApiModelProperty("商品加价")
    private BigDecimal raisePrice;
}
