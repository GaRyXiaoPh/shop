package cn.kt.mall.shop.city.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CityRespVO implements Serializable{

	private static final long serialVersionUID = 6125262881384206704L;
	@ApiModelProperty(notes = "省")
    private Long province;
    @ApiModelProperty(notes = "市")
    private Long city;
    @ApiModelProperty(notes = "县")
    private Long county;
    @ApiModelProperty(notes = "省")
    private String provinceValue;
    @ApiModelProperty(notes = "市")
    private String cityValue;
    @ApiModelProperty(notes = "县")
    private String countyValue;
}
