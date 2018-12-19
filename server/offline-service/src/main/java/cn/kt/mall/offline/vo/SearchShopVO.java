package cn.kt.mall.offline.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/10.
 */
@ApiModel(description = "商圈首页商户请求参数体")
@Setter
@Getter
@NoArgsConstructor
public class SearchShopVO  implements Serializable{

    @ApiModelProperty("市")
    private Long city;

    @ApiModelProperty("区")
    private Long country;

    @ApiModelProperty("经度")
    private String addressLon;

    @ApiModelProperty("纬度")
    private String addressLat;

    @ApiModelProperty("店铺类型")
    private String shopTag;

    @ApiModelProperty("店铺名称")
    private String name;

    @ApiModelProperty("排序(0:智能排序  1:好评优先  2:距离最近)")
    private Integer sort;
}
