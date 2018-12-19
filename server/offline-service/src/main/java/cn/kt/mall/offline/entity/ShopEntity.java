package cn.kt.mall.offline.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/9.
 */
@Setter
@Getter
@NoArgsConstructor
public class ShopEntity implements Serializable{
    /** 店铺id */
    private String shopId;

    /** 店铺名称 */
    private String shopName;

    /** 店铺类型 */
    private String companyType;

    /** 店铺评分 */
    private String shopPoint;

    /** 店铺图片 */
    private String companyImg;

    /** 市 */
    private Long city;
    private String cityName;

    /** 区 */
    private Long county;
    private String countryName;

    /** 经度 */
    private String addressLon;

    /** 纬度 */
    private String addressLat;

    /** 距离 */
    private double distance;
}
