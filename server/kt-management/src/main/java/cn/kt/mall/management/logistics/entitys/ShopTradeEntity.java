package cn.kt.mall.management.logistics.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopTradeEntity implements Serializable {
    private static final long serialVersionUID = 1258933556810658056L;

    private String id;
    /**
     * 内部编号
     */
    private String interiorNo;
    /**
     * 订单编号
     */
    private String tradeNo;
    /**
     * 店铺id
     */
    private String shopId;
    /**
     * '购买者ID'
     */
    private String buyUserId;
    /**
     * 状态(0待发货、1待收货-部分发货、2待收货、3已完成)
     */
    private String status;
    /**
     * 总商品价格（元）
     */
    private BigDecimal totalPrice;
    /**
     * 总运费（元）
     */
    private BigDecimal totalFreightFree;
    /**
     * 订单总金额(元)
     */
    private BigDecimal totalCny;
    /**
     * 需付（优惠券，popc)
     */
    private BigDecimal coinWait;
    /**
     * 购买的时候莱姆币兑换比例
     */
    private BigDecimal lemRate;
    /**
     * 实付（优惠券，popc)
     */
    private BigDecimal coined;
    /**
     * 国
     */
    private Integer country;
    /**
     * 省
     */
    private Integer province;
    /**
     * 市
     */
    private Integer city;
    /**
     * 县
     */
    private Integer county;
    /**
     * 详细地址
     */
    private String detailAddress;
    /**
     * 🉑️方电话
     */
    private String recvName;
    /**
     * 接收方名字
     */
    private String recvMobile;
    /**
     * 邮政编码
     */
    private String zipcode;
    /**
     * 说明
     */
    private String mark;
    /**
     * 删除标志（0：未删除，1用户已删除）
     */
    private Integer flag;
    /**
     * 商家端删除标志（0：未删除，1已删除）
     */
    private Integer shopFlag;
    /**
     * 0表示app来源
     */
    private Integer source;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date lastTime;
    /**
     * 实付余额
     */
    private BigDecimal point;
    /**
     * 省名称
     */
    private String provinceValue;
    /**
     * 市名称
     */
    private String cityValue;
    /**
     * 区名称
     */
    private String countyValue;
    /**
     * 支付类型（1.余额,2.余额+优惠券）
     */
    private String payType;

}
