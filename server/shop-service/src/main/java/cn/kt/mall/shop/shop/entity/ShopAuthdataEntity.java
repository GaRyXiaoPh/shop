package cn.kt.mall.shop.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

@Setter
@Getter
@NoArgsConstructor
public class ShopAuthdataEntity implements Serializable {
    private static final long serialVersionUID = -2174106299728476790L;

    private String id;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("公司名称")
    private String company;
    @ApiModelProperty("省")
    private Long province;
    @ApiModelProperty("市")
    private Long city;
    @ApiModelProperty("区")
    private Long county;
    @ApiModelProperty("详细地址")
    private String detailAddress;
    @ApiModelProperty("经度")
    private String addressLon;
    @ApiModelProperty("纬度")
    private String addressLat;
    @ApiModelProperty(notes = "商户类型", dataType = "string")
    private String companyType;
    @ApiModelProperty(notes = "商户电话", dataType = "string")
    private String companyMobile;
    @ApiModelProperty(notes = "商户Email", dataType = "string")
    private String companyEmail;
    @ApiModelProperty(notes = "商户图片", dataType = "string")
    private String companyImg;
    @ApiModelProperty(notes = "营业编号", dataType = "string")
    private String businessNo;
    @ApiModelProperty(notes = "营业范围", dataType = "string")
    private String businessScope;
    private String businessImg1;
    private String businessImg2;
    private String businessImg3;
    private String businessImg4;
    private String businessImg5;
    @ApiModelProperty("门牌号")
    private String addressNo;
}
