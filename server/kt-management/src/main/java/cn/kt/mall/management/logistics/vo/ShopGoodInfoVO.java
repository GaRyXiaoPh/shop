package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@ApiModel("商品类型和单位")
@Getter
@Setter
@NoArgsConstructor
public class ShopGoodInfoVO implements Serializable {

    private String goodType;

    private String unit;
}
