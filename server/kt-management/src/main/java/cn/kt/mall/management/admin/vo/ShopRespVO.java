package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShopRespVO {
    @ApiModelProperty("店铺ID")
    private String id;
    @ApiModelProperty("店长电话")
    private String mobile;
    @ApiModelProperty("店长Id")
    private String userId;
    @ApiModelProperty("店长名称")
    private String name;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("门店编号")
    private String shopNo;
    @ApiModelProperty(" 2:零售店  3:批发店")
    private String shopLevel;
    @ApiModelProperty("店铺销售业绩")
    private BigDecimal salePerformance;
    @ApiModelProperty("所有店铺销售业绩")
    private BigDecimal allSalePerformance;
}
