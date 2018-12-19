package cn.kt.mall.shop.good.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/31.
 */
@Getter
@Setter
public class AdResVO implements Serializable{

    private static final long serialVersionUID = 7092452864417787407L;

    @ApiModelProperty("id(不用传)")
    private String id;

    @ApiModelProperty("广告名称")
    private String name;

    @ApiModelProperty("广告位置")
    private String position;

//    @ApiModelProperty("开始时间")
//    private String startTime;
//
//    @ApiModelProperty("结束时间")
//    private String endTime;

    @ApiModelProperty("状态 (0:上线 1:下线)")
    private Integer status;

    @ApiModelProperty("广告图片")
    private String url;

    @ApiModelProperty("广告链接")
    private String link;

    @ApiModelProperty("广告备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("广告类型")
    private String type;
    @ApiModelProperty("图文富文本")
    private String content;

}
