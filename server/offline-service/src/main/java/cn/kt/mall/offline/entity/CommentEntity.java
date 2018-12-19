package cn.kt.mall.offline.entity;

import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.offline.vo.CommentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */
@Getter
@Setter
public class CommentEntity  implements Serializable{

    private static final long serialVersionUID = 310042335769606952L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("店铺id")
    private String shopId;

    @ApiModelProperty("评分")
    private Integer score;

    @ApiModelProperty("评价")
    private String content;

    @ApiModelProperty("图片")
    private String  commentPics;

    @ApiModelProperty("评论时间")
    private String createTime;

    public CommentEntity(){}
    // 新建评论实体
    public CommentEntity(String shopId,String orderId,String userId,String text, Integer score, List<String> images){
        this.id = IDUtil.getUUID();
        this.orderId = orderId;
        this.shopId = shopId;
        this.userId = userId;
        this.score = score;
        this.content = text;
        this.commentPics = JSONUtil.toJSONString(images);
    }
}
