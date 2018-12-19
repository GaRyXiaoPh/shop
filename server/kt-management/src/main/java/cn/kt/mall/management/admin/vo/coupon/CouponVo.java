package cn.kt.mall.management.admin.vo.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Getter
@Setter
public class CouponVo implements Serializable {

    private static final long serialVersionUID = -1L;
    //主键
    @ApiModelProperty(notes = "主键'," , dataType = "String")
    private String  id;

    //优惠券名字
    @ApiModelProperty(notes = "优惠券名字'," , dataType = "String")
    private String couponName;
    //优惠券数量
    @ApiModelProperty(notes = "优惠券数量'," , dataType = "Long")
    private Long couponNum;
    //单位
    @ApiModelProperty(notes = "单位'," , dataType = "String")
    private String unit;

    //发放比例
    @ApiModelProperty(notes = "发放比例'," , dataType = "BigDecimal")
    private BigDecimal ratio;
    //发放的天数
    @ApiModelProperty(notes = "发放的天数'," , dataType = "Integer")
    private Integer  sendDays;
    //类型 0.不同用 1.通用
    @ApiModelProperty(notes = "类型 0.不同用 1.通用'," , dataType = "String")
    private Integer couponType;


    //优惠券英文名
    @ApiModelProperty(notes = "优惠券英文名'," , dataType = "String")
    private String couponEnglishName;

    //是否可发送
    @ApiModelProperty(notes = "是否可发送'0：可以 1.不可以," , dataType = "Integer")
    private Integer isSend;

    //不可发送时间列表
    @ApiModelProperty(notes = "不可发送时间列表" , dataType = "List")
    private List<String> notSendDateList;

    //0.不可与第三方对接 1.可以与第三方对接 2.其他
    @ApiModelProperty(notes = "0.不可与第三方对接 1.可以与第三方对接 2.其他" , dataType = "String")
    private Integer isDocking;



}
