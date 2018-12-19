package cn.kt.mall.common.wallet.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.kt.mall.common.util.IDUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

//交易流水

@Setter
@Getter
public class StatementEntity implements Serializable {
	private static final long serialVersionUID = -8132139719637031975L;

	private String id;
	private String userId;
	private String currency;
    @ApiModelProperty("修改前的金额")
	private BigDecimal availableBefore;
    @ApiModelProperty("修改后的金额")
	private BigDecimal availableAfter;
	@ApiModelProperty("变动金额， 负数表示减少")
	private BigDecimal availableChange;
	@ApiModelProperty("修改前的金额")
	private BigDecimal reservedBefore;
    @ApiModelProperty("修改后的金额")
	private BigDecimal reservedAfter;
	@ApiModelProperty("变动金额， 负数表示减少")
	private BigDecimal reservedChange;
	@ApiModelProperty("交易类型: 1:消费 2:收款 3:充值 4:给会员充值 5:扣款 6:扣除用户资金 7:解冻 8:提现")
	private Short tradeType;
    @ApiModelProperty("关联id， 根据tradeType的类型，可能是订单的id，也可能充值或者提现纪录表的ID")
    private String referenceId;
	@ApiModelProperty("状态1:待处理,2:已处理,3:已确定")
	private Short status;
	@ApiModelProperty("交易备注")
	private String mark;
	private Date createTime;
	private String couponId;
	//购物赠送优惠券关联商品Id
	private String goodId;


	public StatementEntity(String userId, String currency,
                           BigDecimal availableBefore, BigDecimal availableAfter, BigDecimal availableChange,
                           BigDecimal reservedBefore, BigDecimal reservedAfter, BigDecimal reservedChange,
                           Short tradeType,String referenceId) {
		this.id = IDUtil.getUUID();
		this.referenceId = referenceId;
		this.userId = userId;
		this.tradeType = tradeType;
		this.currency = currency;
		this.availableBefore = availableBefore;
		this.availableAfter = availableAfter;
        this.availableChange = availableChange;
        this.reservedBefore = reservedBefore;
		this.reservedAfter = reservedAfter;
        this.reservedChange = reservedChange;
		this.mark = "";
		this.createTime = new Date();
		this.status = 2;
	}

	public StatementEntity() {
		super();
	}

}
