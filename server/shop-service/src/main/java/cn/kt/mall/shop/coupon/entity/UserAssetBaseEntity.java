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
public class UserAssetBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

       //用户Id',
    private String userId;

    //'资产类型：popc'
    private String assetType;

    //不可用金额（popc）
    private BigDecimal reservedBalance;
    //当前次数
    private Integer currentReleseNum = 0;
    //是否释放完成
    private Integer sendFinish = 0;

    //创建时间
    private Date createTime;
    //当前剩余的待解冻数据'
    private BigDecimal afterAmount;
    //释放的天数
    private Integer needReleseDays;

}
