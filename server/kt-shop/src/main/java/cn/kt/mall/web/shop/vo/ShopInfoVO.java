package cn.kt.mall.web.shop.vo;

import cn.kt.mall.common.user.vo.LoginInfoVO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShopInfoVO {

    private LoginInfoVO base;
    private ShopEntity shopData;
}
