package cn.kt.mall.shop.good.entity;

import cn.kt.mall.shop.good.vo.GoodPayReqVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GoodEntity implements Serializable {
    private static final long serialVersionUID = -1618620787191155044L;

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
    @ApiModelProperty("商品一级分类, well-sold: 热销 boutique： 精品")
    private String firstGoodType;
    @ApiModelProperty("商品二级分类")
    private String secondGoodType;
    @ApiModelProperty("商品三级分类")
    private String thirdGoodType;
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
    @ApiModelProperty("商品单位")
    private String unit;
    @ApiModelProperty("解冻比率")
    private BigDecimal unfreezeRatio;
    @ApiModelProperty("商品描述")
    private String describes;
    //商铺名称
    private String shopName;
    //商铺级别
    private int shopRank;
    //邮费满减
    private BigDecimal feightRate;
    //商品关联优惠券链表
    private List<GoodCouponCenterEntity> goodCouponCenterEntityList;
    //隶属上级标记
    private int pidFlag;
    //支付方式
    private String payType;
    //支付方式价格
    private String price;
    //商品下架或删除标记
    private String  invalidFlag;

    //商品支付方式列表
    private List<GoodPayReqVO> goodPayEntityList;
   //商铺id
    private String shopShopId;
    //商品销量
    private int sales;
    //中间表商品状态
    private String goodStatus;
    //商品加价
    private BigDecimal raisePrice;




}
