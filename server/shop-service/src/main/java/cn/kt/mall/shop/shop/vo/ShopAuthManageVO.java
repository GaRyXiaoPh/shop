package cn.kt.mall.shop.shop.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShopAuthManageVO implements Serializable {

	private static final long serialVersionUID = -4887372130407080863L;
	@ApiModelProperty("id")
	private String id;
	private String userId;
	@ApiModelProperty("用户昵称")
	private String nick;
	@ApiModelProperty("用户账号")
	private String userName;
	@ApiModelProperty("申请时间")
	private Date createTime;
	@ApiModelProperty("审核状态：0申请, 1审核通过正常，2审核不通过")
	private String status;
	@ApiModelProperty("商家类型：1 地面商户, 2 网上商户")
	private String shopType;
	@ApiModelProperty("店铺名称")
	private String shopName;
	@ApiModelProperty("联系电话")
	private String mobile;
	@ApiModelProperty("通讯地址")
	private String address;
}
