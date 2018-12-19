package cn.kt.mall.shop.cart.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class UpdateCartVO {
    @NotNull
    private List<CartBatchVO> carts;
}
