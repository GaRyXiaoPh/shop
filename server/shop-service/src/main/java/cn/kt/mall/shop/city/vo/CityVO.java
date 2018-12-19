package cn.kt.mall.shop.city.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CityVO {

    @ApiModelProperty(notes = "id")
    private String id;
    @ApiModelProperty(notes = "国")
    private Long country;
    @ApiModelProperty(notes = "省")
    private Long province;
    @ApiModelProperty(notes = "市")
    private Long city;
    @ApiModelProperty(notes = "县")
    private Long county;
    @ApiModelProperty(notes = "详细地址")
    private String detailAddress;
    @ApiModelProperty(notes = "收货姓名")
    private String recvName;
    @ApiModelProperty(notes = "收货电话")
    private String recvMobile;
    @ApiModelProperty(notes = "邮编")
    private String zipcode;
    @ApiModelProperty(notes = "是否默认：0默认")
    private String def;
}
