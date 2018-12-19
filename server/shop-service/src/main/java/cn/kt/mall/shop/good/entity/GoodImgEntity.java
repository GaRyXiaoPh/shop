package cn.kt.mall.shop.good.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GoodImgEntity implements Serializable {
    private static final long serialVersionUID = -2644044801881192791L;

    private String id;
    private String goodId;
    private String img;
    private Short type;//1表示商品图片，2表示商品描述图片
    private Date createTime;

    public GoodImgEntity(String id, String goodId, String img, Short type){
        this.id = id;
        this.goodId = goodId;
        this.img = img;
        this.createTime = new Date();
        this.type = type;
    }
}
