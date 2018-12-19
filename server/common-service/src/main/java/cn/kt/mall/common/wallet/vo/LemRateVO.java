package cn.kt.mall.common.wallet.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LemRateVO {

    private Integer currency;
    private Double unitPriceUsd;
    private Double unitPriceCny;
    private Object upsAndDowns;
}
