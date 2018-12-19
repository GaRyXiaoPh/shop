package cn.kt.mall.shop.collect.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GoodCollectEntity  implements Serializable{
    private static final long serialVersionUID = -1891743169926583570L;

    private String id;
    private String userId;
    private String goodId;
    private Date createTime;
}
