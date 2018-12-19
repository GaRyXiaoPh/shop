package cn.kt.mall.offline.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/5/28.
 */
@Getter
@Setter
public class PicEntity {
    @ApiModelProperty("图片路径")
    private String url;

    @ApiModelProperty("主图(0:主图 1:辅图)")
    private Integer isMain;
}
