package cn.kt.mall.shop.coupon.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/** no comment on table --> tb_user_release_coupon_log */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class UserReleaseCouponLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 --> id */
    private Long id;

    /** 玩家Id --> userId */
    private String userid;

    /** 玩家的优惠券日志Id --> couponLogId */
    private String couponlogid;

    /** 释放的数量 --> amount */
    private BigDecimal amount;

    /** 为 "释放的时间" 提供查询的起始值 */
    private Date createtimeStart;
    /** 为 "释放的时间" 提供查询的结束值 */
    private Date createtimeEnd;
    /** 释放的时间 --> createTime */
    private Date createtime;

    //释放后的可用总数量
    private BigDecimal releaseAfterNum;
    //释放前的可用数
    private BigDecimal releaseBeforNum;
    //释放彩票的类型：0为购买的彩票积分释放，1.为基础数据释放的'
    private int releaseType;
    public static  UserReleaseCouponLogEntity ConUserReleaseCouponLogEntity( String userid,String couponlogid,
                                                                    BigDecimal amount,Date createtime,BigDecimal releaseAfterNum,
                                                                             BigDecimal releaseBeforNum,int releaseType){
        UserReleaseCouponLogEntity entity = new UserReleaseCouponLogEntity();
        entity.setAmount(amount);
        entity.setUserid(userid);
        entity.setCouponlogid(couponlogid);
        entity.setCreatetime(createtime);
        entity.setReleaseAfterNum(releaseAfterNum);
        entity.setReleaseBeforNum(releaseBeforNum);
        entity.setReleaseType(releaseType);
        return entity;
    }
}