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
public class UserCouponEntity implements Serializable {
    private static final long serialVersionUID = -1L;

        private String id;
        //用户ID
         private String userId;
        //消耗cd-key数量
         private long cdkeyNum;
        //兑换优惠券种类：1彩票，2obb，3股权
         private long couponType;
        //兑换优惠券数量
         private BigDecimal couponNum;
        //创建时间
         private Date createTime;
        //状态：0未兑换，1兑换
         private String status;
        //优惠券Id
         private String couponId;

        //待审核兑换优惠券总数量
        private BigDecimal currentExtractNum;

        //冻结的数量
        private BigDecimal  reservedCouponNum;

        //使用起始金额
        private BigDecimal transferStart;
        //使用增加值
        private BigDecimal transferIncrease;
        //使用费率百分比
        private BigDecimal couponExtract;

        //用户手机号
        private String  mobile;

}
