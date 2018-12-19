package cn.kt.mall.common.user.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.kt.mall.common.user.entity.UserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class UserManageRespVO implements Serializable{

	private static final long serialVersionUID = -8770574125686125254L;
	@ApiModelProperty("id")
	private String id;
	@ApiModelProperty("注册时间")
	private Date createTime;
	@ApiModelProperty("会员姓名")
	private String trueName;
	@ApiModelProperty("会员账号（手机号码）")
	private String mobile;
	@ApiModelProperty("推荐人id")
	private String referrer;
	@ApiModelProperty("推荐人姓名")
	private String referrerTrueName;
	@ApiModelProperty("推荐人电话")
	private String referrerMobile;
	@ApiModelProperty("会员等级")
	private String level;
	@ApiModelProperty("实名审核状态，0未审核，1已通过，2已拒绝，3未实名")
	private String certificationStatus;
	@ApiModelProperty("账户余额")
	private BigDecimal availableBalance;
	@ApiModelProperty("")
	private String shopType;
	private String pid;
	@ApiModelProperty("商铺类型 2:零售店 3:批发店")
	private String shopLevel;
	@ApiModelProperty("商铺等级")
	private String shopRank;
	@ApiModelProperty("所属商铺的店长名称 － 如果当前会员有店铺，店长名称就是会员的真实姓名， 否则就是上级门店的店长的真实姓名")
	private String shopUser;
	@ApiModelProperty("所属商铺的电话 - 如果当前会员有店铺，就是当前会员的电话。")
	private String shopMobile;
	@ApiModelProperty("用户状态,0已启用，1已禁用")
	private String status;

	public static UserManageRespVO fromEntity(UserEntity userEntity) {
		UserManageRespVO vo = new UserManageRespVO();
		BeanUtils.copyProperties(userEntity, vo);
		return vo;
	}

	public static List<UserManageRespVO> fromEntity(List<UserEntity> userEntity) {
		List<UserManageRespVO> voList = new ArrayList<>();
		for (UserEntity u : userEntity) {
			voList.add(fromEntity(u));
		}
		return voList;
	}

}
