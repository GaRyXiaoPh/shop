package cn.kt.mall.common.wallet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class ShopEntity implements Serializable {
    private static final long serialVersionUID = 284225801729573398L;

    private String id;
    private String userId;
    private String shopNo;
    private String shopName;
    //0 平台(自营)商户, 1 地面商户, 2 网上商户
    private String shopType;
    private String shopLevel;
    private String shopPoint;
    //店铺等级
    private int shopRank;
    //店铺销售总计
    private BigDecimal shopConsume;
    private String shopTag;
    private String shopAddress;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private String avatar;
    //0申请, 1审核通过正常，2审核不通过 3管理员禁用
    private String status;
    private BigDecimal feightRate;
    private String mark;//审核意见
    private Date createTime;
    private Date lastTime;
    private String whetherLogistics;
    private String whetherPay;
    private int pidFlag;
    //店铺升级之前的等级
    private int shopRankBefore;
    //店铺销售额度
    private BigDecimal shopSalesAmount;
}
