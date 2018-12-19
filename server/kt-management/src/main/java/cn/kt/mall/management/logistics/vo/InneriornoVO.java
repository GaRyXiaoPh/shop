package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@ApiModel("订单内部编号")
@Getter
@Setter
@NoArgsConstructor
public class InneriornoVO implements Serializable {


    private String interiorNo;
}
