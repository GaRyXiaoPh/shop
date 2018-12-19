package cn.kt.mall.shop.shop.vo;

import cn.kt.mall.common.excel.DynamicHeader;
import cn.kt.mall.common.excel.DynamicRow;
import cn.kt.mall.shop.coupon.entity.CouponEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 历史盈利数据
 *
 */


public class HistoryProfitVO implements Serializable,DynamicRow {


    private static final long serialVersionUID = 6864164871751146920L;
    @ApiModelProperty("时间")
    private String createTime;
    @ApiModelProperty("收入总信用金")
    private BigDecimal incomeTotalCredit;
    @ApiModelProperty("收入总优惠券")
    private BigDecimal incomeTotalCoupon;
    @ApiModelProperty("赠送总彩票积分")
    private BigDecimal giveTotalLottery;
    @ApiModelProperty("赠送总优惠券")
    private BigDecimal giveTotalCoupon;
    @ApiModelProperty("赠送总游戏积分")
    private BigDecimal giveTotalGame;
    @ApiModelProperty("赠送总债权")
    private BigDecimal giveTotalIbot;
    @ApiModelProperty("赠送总保险")
    private BigDecimal giveTotalnsurance;
    @ApiModelProperty("赠送类型")
    private String speciesType;
    @ApiModelProperty("赠送数量")
    private BigDecimal rechargeNum;
    @ApiModelProperty("优惠券名称")
    private String couponName;
    /*@ApiModelProperty("数据名称名称")
    private String dataName;
    @ApiModelProperty("数据值")
    private BigDecimal dataValue;*/
    @ApiModelProperty("数据名称与值")
    private List<DynamicHeader> dataNameValue;


    @Override
    public List<DynamicHeader> getDataNameValue() {
        return dataNameValue;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getIncomeTotalCredit() {
        return incomeTotalCredit;
    }

    public void setIncomeTotalCredit(BigDecimal incomeTotalCredit) {
        this.incomeTotalCredit = incomeTotalCredit;
    }

    public BigDecimal getIncomeTotalCoupon() {
        return incomeTotalCoupon;
    }

    public void setIncomeTotalCoupon(BigDecimal incomeTotalCoupon) {
        this.incomeTotalCoupon = incomeTotalCoupon;
    }

    public BigDecimal getGiveTotalLottery() {
        return giveTotalLottery;
    }

    public void setGiveTotalLottery(BigDecimal giveTotalLottery) {
        this.giveTotalLottery = giveTotalLottery;
    }

    public BigDecimal getGiveTotalCoupon() {
        return giveTotalCoupon;
    }

    public void setGiveTotalCoupon(BigDecimal giveTotalCoupon) {
        this.giveTotalCoupon = giveTotalCoupon;
    }

    public BigDecimal getGiveTotalGame() {
        return giveTotalGame;
    }

    public void setGiveTotalGame(BigDecimal giveTotalGame) {
        this.giveTotalGame = giveTotalGame;
    }

    public BigDecimal getGiveTotalIbot() {
        return giveTotalIbot;
    }

    public void setGiveTotalIbot(BigDecimal giveTotalIbot) {
        this.giveTotalIbot = giveTotalIbot;
    }

    public BigDecimal getGiveTotalnsurance() {
        return giveTotalnsurance;
    }

    public void setGiveTotalnsurance(BigDecimal giveTotalnsurance) {
        this.giveTotalnsurance = giveTotalnsurance;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }

    public BigDecimal getRechargeNum() {
        return rechargeNum;
    }

    public void setRechargeNum(BigDecimal rechargeNum) {
        this.rechargeNum = rechargeNum;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public void setDataNameValue(List<DynamicHeader> dataNameValue) {
        this.dataNameValue = dataNameValue;
    }
}
