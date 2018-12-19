package cn.kt.mall.management.admin.vo;

import java.math.BigDecimal;
import java.util.List;

import cn.kt.mall.common.user.vo.UserCountVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndexVO {

	@ApiModelProperty("注册用户总数")
	private Integer totalUser;
	@ApiModelProperty("网上商户总数")
	private Integer totalOnlineUser;
	@ApiModelProperty("地面商户总数")
	private Integer totalOfflineUser;
	@ApiModelProperty("既为地面商户又为网上商户总数")
	private Integer totalShopUser;
	@ApiModelProperty("普通会员用户总数")
	private Integer totalSimpleUser;
	@ApiModelProperty("平台莱姆总数")
	private BigDecimal sysLem;
	@ApiModelProperty("会员账号莱姆总数")
	private BigDecimal userTotalLem;
	@ApiModelProperty("网上商户账户总额")
	private BigDecimal totalOnlineLem;
	@ApiModelProperty("地面商户账户总额")
	private BigDecimal totalOfflineLem;
	@ApiModelProperty("平台费用莱姆额")
	private BigDecimal sysFreeLem;
	@ApiModelProperty("平台交易-线上商城总额")
	private BigDecimal tradeOnlineTotalLem;
	@ApiModelProperty("平台交易-线下商圈总额")
	private BigDecimal tradeOfflineTotalLem;
	@ApiModelProperty("昨日交易-线上商城总额")
	private BigDecimal tradeOnlineLem;
	@ApiModelProperty("昨日交易-线下商圈总额")
	private BigDecimal tradeOfflineLem;
	@ApiModelProperty("用户七日注册用户趋势数据")
	List<UserCountVO> userRegisterList;
	
	@ApiModelProperty("地面商品上架数量")
	private Integer offlineGoodPass;
	@ApiModelProperty("网上商品上架数量")
	private Integer onlineGoodPass;
	@ApiModelProperty("地面商品审核数量")
	private Integer offlineGoodApply;
	@ApiModelProperty("网上商品审核数量")
	private Integer onlineGoodPassApply;

	public IndexVO(Integer totalUser, Integer totalOnlineUser, Integer totalOfflineUser, Integer totalShopUser,
			Integer totalSimpleUser, BigDecimal sysLem, BigDecimal userTotalLem, BigDecimal totalOnlineLem,
			BigDecimal totalOfflineLem, BigDecimal sysFreeLem, BigDecimal tradeOnlineTotalLem,
			BigDecimal tradeOfflineTotalLem, BigDecimal tradeOnlineLem, BigDecimal tradeOfflineLem,
			List<UserCountVO> userRegisterList,Integer offlineGoodPass,Integer onlineGoodPass,Integer offlineGoodApply,Integer onlineGoodPassApply) {
		super();
		this.totalUser = totalUser;
		this.totalOnlineUser = totalOnlineUser;
		this.totalOfflineUser = totalOfflineUser;
		this.totalShopUser = totalShopUser;
		this.totalSimpleUser = totalSimpleUser;
		this.sysLem = sysLem;
		this.userTotalLem = userTotalLem;
		this.totalOnlineLem = totalOnlineLem;
		this.totalOfflineLem = totalOfflineLem;
		this.sysFreeLem = sysFreeLem;
		this.tradeOnlineTotalLem = tradeOnlineTotalLem;
		this.tradeOfflineTotalLem = tradeOfflineTotalLem;
		this.tradeOnlineLem = tradeOnlineLem;
		this.tradeOfflineLem = tradeOfflineLem;
		this.userRegisterList = userRegisterList;
		this.offlineGoodPass = offlineGoodPass;
		this.offlineGoodApply = offlineGoodApply;
		this.onlineGoodPass = onlineGoodPass;
		this.onlineGoodPassApply = onlineGoodPassApply;
	}

	public IndexVO() {
		super();
	}

}
