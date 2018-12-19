package cn.kt.mall.shop.advertise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 广告VO
 * Created by Administrator on 2018/5/31.
 */
@Getter
@Setter
public class AdResVO implements Serializable {

    private static final long serialVersionUID = 7092452864417787407L;

    @ApiModelProperty("id(不用传)")
    private String id;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("广告标题")
    private String name;

    @ApiModelProperty("广告图片")
    private String url;

    @ApiModelProperty("广告链接")
    private String link;

    @ApiModelProperty("状态：0:上线 1:下线")
    private Integer status;

    @ApiModelProperty("类型：0:链接 1:图文")
    private Integer type;

    @ApiModelProperty(value = "图文富文本")
    private String content;

    @ApiModelProperty(value = "最后编辑时间",hidden = true)
    private String endTime;

}
