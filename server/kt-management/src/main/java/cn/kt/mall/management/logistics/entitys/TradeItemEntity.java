package cn.kt.mall.management.logistics.entitys;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class TradeItemEntity implements Serializable {
    private static final long serialVersionUID = -2093082766895385040L;

    private String id;
    private String tradeId;
    private String shopId;
    private String goodId;
    private String goodName;
    private String goodImg;
    private Integer buyNum;
    private BigDecimal buyPrice;
    private BigDecimal freightFree;
    private String buyUserId;
    private String payType;
    private Date createTime;
    private Date lastTime;
    private String goodStatus;
    private String firstGoodType;
    private String secondGoodType;
    private String thirdGoodType;

}
