package cn.kt.mall.shop.coupon.vo;

import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
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
public class CouponVO implements Serializable {
    private static final long serialVersionUID = 2970777341965201099L;
    private String id;
    private String couponId;
    private String userId;
    private long cdkeyNum;
    private long couponType;
    private BigDecimal couponNum;
    private Date createTime;
    private String status;
    //0.不可与第三方对接 1.可以与第三方对接 2.其他
    private int isDocking;
    //List<CouponVO> list;
    //优惠券（popc）消费赠送记录
    private List<StatementEntity> statementEntityList;
    //优惠券赠送记录(2.彩票 3.债权 4.游戏积分)
    private List<UserCouponLogEntity> userCouponLogEntityList;
    //冻结的数量
    private BigDecimal  reservedCouponNum;
}

