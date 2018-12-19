package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@ApiModel("商店店铺店主的基本信息")
@Getter
@Setter
@NoArgsConstructor
public class ShopUserBaseInfoVO implements Serializable {


    private String shopNo;

    private String mobile;

    private String trueName;



}
