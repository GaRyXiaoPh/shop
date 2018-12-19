package cn.kt.mall.shop.good.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class GoodTypeVO implements Serializable {

    private static final long serialVersionUID = -2440209465342630419L;
    // id
    private String id;
    // 商品类别名称
    private String name;
    // 创建时间
    private String createTime;
    // 商品数量
    private int goodCount;
}
