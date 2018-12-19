package cn.kt.mall.shop.collect.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ShopCollectEntity implements Serializable {
    private static final long serialVersionUID = 463208416594313040L;

    private String id;
    private String userId;
    private String shopId;
    private Date createTime;
}
