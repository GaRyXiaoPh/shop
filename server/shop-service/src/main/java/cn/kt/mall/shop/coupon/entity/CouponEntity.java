package cn.kt.mall.shop.coupon.entity;

import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
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
public class CouponEntity implements Serializable {
    private static final long serialVersionUID = 2630805671121979141L;
    //兑换券表id
    private String id;
    private String couponName;
    private BigDecimal couponNum;
    private String unit;
    private Date createTime;
    private String userId;
    private long cdkeyNum;
    private int couponType;
    //用户兑换券表id
    private String userCouponId;
    //0.不可与第三方对接 1.可以与第三方对接 2.其他
    private int isDocking;
    //'优惠券类型:1.优惠券，2.彩票 3.债权 4.游戏积分'
    private int speciesType;
    //优惠券（popc）消费赠送记录
    private List<StatementEntity> statementEntityList;
    //冻结的数量
    private BigDecimal  reservedCouponNum;
    //优惠券表中如果为优惠券类型则加入优惠券初始数据
    private UserAssetEntity userAssetEntity;
    // 用户初始化优惠券
    private BigDecimal reservedBalanceBase;
    // 用户初始化优惠券释放总数
    private BigDecimal releaseBalanceBase;
    // 用户初始化优惠券时间
    private Date createTimeBase;


}
