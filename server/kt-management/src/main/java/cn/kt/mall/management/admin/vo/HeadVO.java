package cn.kt.mall.management.admin.vo;

import cn.kt.mall.common.user.vo.UserCountVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class HeadVO {

	@ApiModelProperty("表头名称")
	private String headName;
	@ApiModelProperty("表头值")
	private String headValue;

}
