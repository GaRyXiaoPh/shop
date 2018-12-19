package cn.kt.mall.shop.coupon.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/** 用户兑换券记录 --> tb_user_coupon_log */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class UserCouponLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    /** 用户ID --> userId */
    private String userid;

    /** 订单ID --> tradeId */
    private String tradeid;

    /** ⁮变动之前数量 --> beforeNum */
    private BigDecimal beforenum;

    /** ⁮变动数量 --> rechargeNum */
    private BigDecimal rechargenum;

    /** ⁮变动之后数量 --> afterNum */
    private BigDecimal afternum;

    /** 兑换券名称 --> couponName */
    private String couponname;

    /** 为 "创建时间" 提供查询的起始值 */
    private Date createtimeStart;
    /** 为 "创建时间" 提供查询的结束值 */
    private Date createtimeEnd;
    /** 创建时间 --> createTime */
    private Date createtime;

    /** 商品id --> goodId */
    private String goodid;

    /** 优惠劵对应的配置表Id --> couponId */
    private String couponid;

    /** 0:表示未发送完成 1.表示发放完成 --> sendFinish */
    private Integer sendfinish;

    /** 当前第几次释放 --> currentReleaseNum */
    private Integer currentreleasenum;

    /** 为 "下次释放的时间" 提供查询的起始值 */
    private Date nextreleasetimeStart;
    /** 为 "下次释放的时间" 提供查询的结束值 */
    private Date nextreleasetimeEnd;
    /** 下次释放的时间 --> nextReleaseTime */
    private Date nextreleasetime;

    /** 每次释放的数量 --> everyTimeReleaseNum */
    private BigDecimal everytimereleasenum;

    /** 优惠券种类 */
    private int speciesType;
    /** 需要释放的天数 */
    private int needReleseDays;

    /** 触发赠送的人Id*/
    private String relationUserId;

    /** 消费金额*/
    private BigDecimal totalCny;

    /** 消费返推荐人比率*/
    private BigDecimal ratio;

    /** 商品加价总额*/
    private BigDecimal totalRaisePrice;
}