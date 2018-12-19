package cn.kt.mall.offline.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by Administrator on 2018/5/12.
 */
@Getter
@Setter
public class CommentInfo {

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("评分")
    private Integer score;

    @ApiModelProperty("评价")
    private String content;

    @ApiModelProperty("图片")
    private String pics;
}
