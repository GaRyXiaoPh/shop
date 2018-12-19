package cn.kt.mall.shop.shop.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthDataVO {
    @ApiModelProperty("商铺基本信息")
    private ShopVO base;
    @ApiModelProperty("商铺认证信息")
    private ShopAuthdataDetailsVO authData;
}
