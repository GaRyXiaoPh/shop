package cn.kt.mall.common.user.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员管理接收model
 */
@Setter
@Getter
@NoArgsConstructor
public class UserManageReqVO implements Serializable {

	private static final long serialVersionUID = -6963231845521576460L;

	@ApiModelProperty("用户手机号")
	private String userMobile;
	@ApiModelProperty("推荐人手机号")
	private String referrerMobile;
	@ApiModelProperty("用户等级")
	private String level;
	@ApiModelProperty("店铺类型--2:零售店  3:批发店")
	private String shopType;
	@ApiModelProperty("所属商铺电话--如果当前会员有店铺，就是当前会员的电话。")
	private String shopMobile;
	@ApiModelProperty("用户状态,0已启用，1已禁用")
	private String status;
	@ApiModelProperty("实名审核状态，0未审核，1已通过，2已拒绝，3未实名")
	private String certificationStatus;

	public UserManageReqVO(String userMobile,String referrerMobile,String level,String shopType,String shopMobile,String status,String certificationStatus) {
		this.userMobile = userMobile;
		this.referrerMobile = referrerMobile;
		this.level = level;
		this.shopType = shopType;
		this.shopMobile = shopMobile;
		this.status = status;
		this.certificationStatus = certificationStatus;
	}
}
