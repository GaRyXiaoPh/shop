package cn.kt.mall.offline.entity;

import cn.kt.mall.offline.vo.CommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/11.
 */
@Setter
@Getter
public class ShopInfoEntity  implements Serializable{

    private static final long serialVersionUID = 1209862496481830433L;

    @ApiModelProperty("用户id")
    private String  userId;

    @ApiModelProperty("店铺id")
    private String  shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺图片")
    private String companyImg;

    @ApiModelProperty("店铺评分")
    private String shopPoint;

    @ApiModelProperty("店铺类型")
    private String shopTag;
    private String companyType;

    @ApiModelProperty("店铺地址")
    private String shopAddress;

    @ApiModelProperty("联系人电话")
    private String mobile;

    @ApiModelProperty("莱姆比例")
    private double  rate;

    @ApiModelProperty("店铺是否被收藏(true:收藏 false:没有收藏)")
    private boolean flag;

    @ApiModelProperty("商品列表")
    private List<GoodEntity> goodsList;

    @ApiModelProperty("评论列表")
    private List<CommentInfo> commentList;

}
