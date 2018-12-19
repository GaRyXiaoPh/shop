package cn.kt.mall.shop.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

@Setter
@Getter
@NoArgsConstructor
public class ShopTemplateEntity implements Serializable{

	private static final long serialVersionUID = 272472353866096301L;
	@ApiModelProperty("id为空表示新增，存在id则表示编辑")
    private String id;
    @ApiModelProperty(hidden = true)
    private String shopId;
    @ApiModelProperty(value = "名称")
    @NotEmpty
    private String name;
    @ApiModelProperty("邮费费用")
    @NotNull
    private BigDecimal freightFree;
    @ApiModelProperty("模版说明")
    private String remark;
    @ApiModelProperty("模版状态：0启用，1禁用")
    @NotEmpty
    private String flag;
    @ApiModelProperty(hidden = true)
    private Date createTime;
    @ApiModelProperty(hidden = true)
    private Date lastTime;
}
