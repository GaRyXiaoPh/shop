package cn.kt.mall.common.user.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserManageBaseVO implements Serializable{

	private static final long serialVersionUID = 1661664029103164100L;

	@ApiModelProperty("用户姓名")
	private String trueName;
	@ApiModelProperty("用户账号（手机号码）")
	private String mobile;
	@ApiModelProperty("用户等级")
	private String level;
	@ApiModelProperty("推荐人姓名")
	private String referrerName;
	@ApiModelProperty("推荐人电话")
	private String referrerMobile;
	@ApiModelProperty("所属商铺ID")
	private String shopNo;
	@ApiModelProperty("所属店铺名称")
	private String pName;
	@ApiModelProperty("所属店铺手机号")
	private String pMobile;
	@ApiModelProperty("商铺级别， 2:零售店 3:批发店")
	private String shopLevel;
	@ApiModelProperty("可用信用金")
	private BigDecimal availableBalance;
	@ApiModelProperty("团队人数")
	private Integer referrerCounts;
    @ApiModelProperty("销售业绩（新增加）")
    private BigDecimal salePerformance;

}
