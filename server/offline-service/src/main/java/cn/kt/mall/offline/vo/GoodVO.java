package cn.kt.mall.offline.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/28.
 */
@ApiModel(description = "商品请求体")
@Setter
@Getter
@NoArgsConstructor
public class GoodVO implements Serializable{

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品价格")
    private Double price;

    @ApiModelProperty("商品图片数组")
    private String[] goodsPic;

    @ApiModelProperty("商品详情图片数组")
    private String[] goodDetailPic;

}
