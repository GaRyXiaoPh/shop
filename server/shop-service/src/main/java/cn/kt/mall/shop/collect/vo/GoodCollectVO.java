package cn.kt.mall.shop.collect.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GoodCollectVO {
    private String id;
    private String userId;
    private String goodId;
    private Date createTime;

    private String goodName;
}
