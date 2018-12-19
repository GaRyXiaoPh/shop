package cn.kt.mall.shop.good.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.kt.mall.shop.coupon.entity.CouponEntity;
import cn.kt.mall.shop.good.entity.GoodCouponCenterEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GoodRespVO implements Serializable {

    private static final long serialVersionUID = -2152599071413975544L;
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("商品编号")
    private String goodNo;
    @ApiModelProperty("商铺id")
    private String shopId;
    @ApiModelProperty("商铺类别")
    private String shopLevel;
    @ApiModelProperty("商铺级别")
    private int shopRank;
    @ApiModelProperty("商铺名称")
    private String shopName;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("商品名称")
    private String goodName;
    @ApiModelProperty("商品价格")
    private BigDecimal goodPrice;
    @ApiModelProperty("商品主图")
    private String goodImg;
    @ApiModelProperty("商品一级分类")
    private String firstGoodType;
    @ApiModelProperty("商品二级分类")
    private String secondGoodType;
    @ApiModelProperty("商品三级分类")
    private String thirdGoodType;
    @ApiModelProperty("商品一级分类值")
    private String firstGoodTypeValue;
    @ApiModelProperty("商品二级分类值")
    private String secondGoodTypeValue;
    @ApiModelProperty("商品三级分类值")
    private String thirdGoodTypeValue;
    @ApiModelProperty("销量")
    private int goodSales;
    @ApiModelProperty("库存")
    private int goodStock;
    @ApiModelProperty("运费")
    private BigDecimal freightFree;
    @ApiModelProperty("状态：0上传待审核，1审核通过，2审核不通过, 3商品下架")
    private String status;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("最近一次修改时间")
    private Date lastTime;
    @ApiModelProperty("审核人")
    private String auditUser;
    @ApiModelProperty("审核时间")
    private Date auditTime;
    @ApiModelProperty("自营状态：0否，1是")
    private String selfSupport;
    @ApiModelProperty("删除状态：0否，1是")
    private Short delFlag;
    @ApiModelProperty("优惠券集合")
    private List<GoodCouponCenterReqVO> couponEntityList;
    @ApiModelProperty("支付类型集合")
    private List<GoodPayReqVO> goodPayList;
    @ApiModelProperty("商品单位")
    private String unit;
    @ApiModelProperty("解冻比率")
    private BigDecimal unfreezeRatio;
    @ApiModelProperty("商品描述")
    private String describes;
    //是否为当前用户隶属上级 1上级 0 无
    private int pidFlag;
    //邮费满减
    private BigDecimal feightRate;

    private String goodStatus;
    @ApiModelProperty("支付类型集合前台用")
    private List<GoodPayReqVO> goodPayEntityList;
    @ApiModelProperty("商铺id")
    private String shopShopId;
    //商品销量
    private int sales;
    @ApiModelProperty("支付类型比例：信用金:60% 优惠券:40%")
    private String payRatio;
    //商品加价
    private BigDecimal raisePrice;
}


