package cn.kt.mall.shop.shop.vo;

import cn.kt.mall.common.excel.DynamicHeader;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class HeadDataVO implements DynamicHeader {

	@ApiModelProperty("数据名称")
	private String headName;
	@ApiModelProperty("数据值")
	private BigDecimal headvalue;

}
