package cn.kt.mall.im.moments.vo;

import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MomentsReqVO {

	@ApiModelProperty("文本内容")
	@NotEmpty
	private String content;
	@ApiModelProperty("经度")
	@NotEmpty
	private String addressLon;
	@ApiModelProperty("纬度")
	@NotEmpty
	private String addressLat;
	@ApiModelProperty("详细地址")
	@NotEmpty
	private String location;
	@ApiModelProperty("图片")
	@Size(max = 9, min = 0)
	private List<String> imgUrl;
}
