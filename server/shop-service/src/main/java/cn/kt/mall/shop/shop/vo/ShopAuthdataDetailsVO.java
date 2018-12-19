package cn.kt.mall.shop.shop.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ShopAuthdataDetailsVO {

	private String id;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("店铺id")
    private String shopId;
    @ApiModelProperty("公司名称")
    private String company;
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
    @ApiModelProperty(notes = "省code")
    private Long province;
    @ApiModelProperty(notes = "市code")
    private Long city;
    @ApiModelProperty(notes = "县code")
    private Long county;
    @ApiModelProperty(notes = "省value")
    private String provinceValue;
    @ApiModelProperty(notes = "市value")
    private String cityValue;
    @ApiModelProperty(notes = "县value")
    private String countyValue;
}
