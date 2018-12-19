package cn.kt.mall.offline.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/16.
 */
@Setter
@Getter
public class CommentResVO implements Serializable{

    private static final long serialVersionUID = 2089402933189488531L;

    @ApiModelProperty("店铺id(不必填)")
    private String shopId;

    @ApiModelProperty("评论者姓名")
    private String username;

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
