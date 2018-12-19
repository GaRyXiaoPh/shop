package cn.kt.mall.shop.address.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AddressVO implements Serializable {

	private static final long serialVersionUID = -3697700415046822614L;
	@ApiModelProperty(notes = "id")
	private String id;
	@ApiModelProperty(notes = "省")
	@NotNull
	private Long province;
	private String provinceValue;
	@ApiModelProperty(notes = "市")
	@NotNull
	private Long city;
	private String cityValue;
	@ApiModelProperty(notes = "县")
	@NotNull
	private Long county;
	private String countyValue;
	@ApiModelProperty(notes = "详细地址")
	@NotBlank
	private String detailAddress;
	@ApiModelProperty(notes = "收货姓名")
	@NotBlank
	private String recvName;
	@ApiModelProperty(notes = "收货电话")
	@NotBlank
	private String recvMobile;
	@ApiModelProperty(notes = "邮编")
	@NotBlank
	private String zipcode;
	@ApiModelProperty(notes = "是否默认：0默认")
	private String def;
}
