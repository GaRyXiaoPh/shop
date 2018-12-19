package cn.kt.mall.shop.cart.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class CartBatchVO {
    @ApiModelProperty(name="id", dataType = "string")
    @NotEmpty
    private String id;
    @ApiModelProperty(name="购买数量", dataType = "int")
    private int buyNum;
}
