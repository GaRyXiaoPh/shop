package cn.kt.mall.shop.coupon.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class UserReturnCouponLogEntity implements Serializable {
    private static final long serialVersionUID = 2630805671121979141L;
    //下单返还推荐人优惠券记录表
    /** 0:表示未发送完成 1.表示发放完成 --> sendFinish */
    private String id;
    /** 用户id */
    private String userId;
    /** 订单id */
    private String tradeId;
    /** 触发赠送的人Id */
    private String relationUserId;
    /** 赠送数量 */
    private BigDecimal amount;
    /**优惠券配置Id */
    private String couponId;
    /** 创建时间 */
    private Date createTime;
   // 当前释放的天数
    private Integer currentReleaseNum=0;

    //0:未发送完 1.发送完
    private Integer sendFinish = 0;

    //变动后的数量
    private  BigDecimal afterAmount;
    /** 需要释放的天数 */
    private int needReleaseDays;

    /** 返还推荐人比率 */
    private BigDecimal returnRatio;



}
