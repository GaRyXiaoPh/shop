package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@ApiModel("优惠券列表")
@Getter
@Setter
@NoArgsConstructor

public class CoupNamesVO implements Serializable {

    private String goodId;

    private String couponName;
}
