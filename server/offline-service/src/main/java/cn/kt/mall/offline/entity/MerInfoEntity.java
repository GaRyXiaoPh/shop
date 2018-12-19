package cn.kt.mall.offline.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/24.
 */
@Getter
@Setter
public class MerInfoEntity implements Serializable{

    @ApiModelProperty("联系人姓名")
    private String  name;

    @ApiModelProperty("联系方式")
    private String  mobile;

    @ApiModelProperty("通信地址")
    private String address;

    @ApiModelProperty("店铺id")
    private String shopId;

    @ApiModelProperty("公司名称")
    private String shopName;

    @ApiModelProperty("省")
    private Long province;

    @ApiModelProperty("市")
    private Long city;

    @ApiModelProperty("区")
    private Long county;

    @ApiModelProperty("公司地址")
    private String detailAddress;

    @ApiModelProperty("商户类型")
    private String shopTag;

    @ApiModelProperty("商户类型")
    private String companyType;

    @ApiModelProperty("经营范围")
    private String businessScope;

    @ApiModelProperty("商户类型")
    private String businessNo;

    @ApiModelProperty("商户类型")
    private String companyImg;

    @ApiModelProperty("商户类型")
    private String businessImg1;

    @ApiModelProperty("商户类型")
    private String businessImg2;

    @ApiModelProperty("商户类型")
    private String businessImg3;

    @ApiModelProperty("商户类型")
    private String businessImg4;

    @ApiModelProperty("商户类型")
    private String businessImg5;

    @ApiModelProperty("商户类型")
    private String companyMobile;

}
