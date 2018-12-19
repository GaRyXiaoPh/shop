package cn.kt.mall.offline.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/9.
 */
@ApiModel(description = "商圈首页商户请求参数体")
@Setter
@Getter
@NoArgsConstructor
public class HomeShopVO  implements Serializable{

    @ApiModelProperty("市")
    private Long city;

    @ApiModelProperty("经度")
    private String addressLon;

    @ApiModelProperty("纬度")
    private String addressLat;

}
