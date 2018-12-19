package cn.kt.mall.shop.good.vo;

import cn.kt.mall.shop.good.entity.GoodCouponCenterEntity;
import cn.kt.mall.shop.good.entity.GoodImgEntity;
import cn.kt.mall.shop.good.entity.GoodPayEntity;
import cn.kt.mall.shop.good.entity.GoodPropertyEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GoodVO {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("商品编号")
    private String goodNo;
    @ApiModelProperty("商铺id")
    private String shopId;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("商品名称")
    private String goodName;
    @ApiModelProperty("商品价格")
    private BigDecimal goodPrice;
    @ApiModelProperty("商品主图")
    private String goodImg;
    @ApiModelProperty("销量")
    private int goodSales;
    @ApiModelProperty("库存")
    private int goodStock;
    @ApiModelProperty("运费")
    private BigDecimal freightFree;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("最近一次修改时间")
    private Date lastTime;
    @ApiModelProperty("审核时间")
    private Date auditTime;
    @ApiModelProperty("自营状态：0否，1是")
    private String selfSupport;
    @ApiModelProperty("商品一级分类")
    private String firstGoodType;
    @ApiModelProperty("商品一级分类值")
    private String firstGoodTypeValue;
    @ApiModelProperty("商品二级分类")
    private String secondGoodType;
    @ApiModelProperty("商品二级分类值")
    private String secondGoodTypeValue;
    @ApiModelProperty("商品三级分类")
    private String thirdGoodType;
    @ApiModelProperty("商品三级分类值")
    private String thirdGoodTypeValue;
    @ApiModelProperty("商品属性集合")
    private List<GoodPropertyEntity> goodPropertys;
    @ApiModelProperty("商品图片集合")
    private List<GoodImgEntity> goodImgs;
    @ApiModelProperty("商品描述图片集合")
    private List<GoodImgEntity> goodDetails;
    @ApiModelProperty("收藏状态，0未收藏，1已收藏")
    private int collectType;
    @ApiModelProperty("商品描述")
    private String describes;
    @ApiModelProperty("商品单位")
    private String unit;
    //上级id
    private String pid;
    //是否为当前用户隶属上级
    private int pidFlag;
    //商铺名称
    private String shopName;
    //商铺级别
    private int shopRank;
    //邮费满减
    private BigDecimal feightRate;
    //商品关联优惠券链表
    private List<GoodCouponCenterEntity> goodCouponCenterEntityList;
    //支付方式
    private String payType;
    //支付方式价格
    private String price;
    //商品支付方式列表
    private List<GoodPayReqVO> goodPayEntityList;

    private String shopShopId;

    //商品销量
    private int sales;
    //商品加价
    private BigDecimal raisePrice;
}
