package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel("商店商品物流列表查询条件")
@Getter
@Setter
@NoArgsConstructor
public class LogisticsRquestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    //下单时间
    //开始时间
    @ApiModelProperty( "下单开始时间")
    private String startTime;

    //结束时间
    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("商店Id用于查询，其实叫编号")
    private String shopNo;
    //店铺 Id
    @ApiModelProperty("店铺 Id")
    private String shopId;
    //订单状态
    @ApiModelProperty("订单状态")
    private String status;
    //商店名称
    @ApiModelProperty("商店名称")
    private String shopName;
    //店长名称
    @ApiModelProperty("店长名称")
    private String shopUserName;
    //s收货人名
    @ApiModelProperty("收货人名")
    private String recvName;
    //收货人电话
    @ApiModelProperty("收货人电话")
    private String recvMobile;

    @ApiModelProperty("内部编号，查询用")
    private String interiorNo;
    //物品名称
    @ApiModelProperty("物品名称")
    private List<String> goodNames;

    @ApiModelProperty("店长手机号")
    private String shopUserPhone;


    public LogisticsRquestVO(String startTime, String endTime, String interiorNo,String shopId,
                                           String status,String shopName,String shopUserName,
                                          String shopUserPhone,
                                          String recvName,String recvMobil,List<String> goodNames){
        super();
        this.startTime=startTime;
        this.endTime = endTime;
        this.interiorNo=interiorNo;
        this.shopId = shopId;
        this.status=status;
        this.shopName = shopName;
        this.shopUserName=shopUserName;
        this.recvName =recvName;
        this.recvMobile=recvMobil;
        this.shopUserPhone =shopUserPhone;
    }
}
