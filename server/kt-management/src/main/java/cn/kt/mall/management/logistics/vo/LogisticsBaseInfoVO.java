package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@ApiModel("物流的基本信息")
@Getter
@Setter
@NoArgsConstructor
public class LogisticsBaseInfoVO implements Serializable {

    @ApiModelProperty("内部订单Id")
    private String interiorNo;

    @ApiModelProperty("内部订单Id")
    private String tradeId;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    //订单状态,状态(0:待发货 1.部分发货 2.已发货)',
    @ApiModelProperty("0:待发货 1.部分发货 2.已发货")
    private String status;

    //购买者的用户Id
    @ApiModelProperty("购买者的用户Idg")
    private String buyUserId;

    //收货人名字
    @ApiModelProperty("收货人名字")
    private String recvName;

    //收货地址
    @ApiModelProperty("收货地址")
    private String detailAddress;

    //收货人电话
    @ApiModelProperty("收货人电话")
    private String recvMobile;

    //店铺Id
    @ApiModelProperty("店铺Id")
    private String shopId;

    @ApiModelProperty("店长名字")
    private String shopUserName;

    @ApiModelProperty("编号")
    private String tradeNo;

    @ApiModelProperty("商店Id(查询使用)")
    private String shopNo;

    @ApiModelProperty("店长电话")
    private String shopMobile;

    private Long currentTime;

}
