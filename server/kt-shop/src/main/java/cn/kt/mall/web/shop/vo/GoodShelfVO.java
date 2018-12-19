package cn.kt.mall.web.shop.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GoodShelfVO {

    private String shopId;
    private List<String> goodId;
    private String goodStatus;
}
