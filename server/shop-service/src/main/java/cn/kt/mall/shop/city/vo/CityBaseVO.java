package cn.kt.mall.shop.city.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/5/11.
 */
@Setter
@Getter
public class CityBaseVO implements Serializable{

	private static final long serialVersionUID = 2681208988966547790L;
	@ApiModelProperty(notes = "id")
    private Long sid;
    @ApiModelProperty(notes = "名称")
    private String name;
}
