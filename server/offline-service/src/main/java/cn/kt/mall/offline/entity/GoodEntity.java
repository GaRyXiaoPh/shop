package cn.kt.mall.offline.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenhong on 2018/4/26.
 */
@ApiModel(description = "商品实体类")
@Getter
@Setter
public class GoodEntity implements Serializable {

    private static final long serialVersionUID = -621454051836992156L;

    @ApiModelProperty("商品id")
    private String  id;

    @ApiModelProperty("店铺id")
    private String  shopId;

    @ApiModelProperty("店铺名称")
    private String  shopName;

    @ApiModelProperty("用户id")
    private String  userId;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品价格")
    private Double price;

    @ApiModelProperty("状态  1:待审核  2:已上架  3:已下架  4:未通过")
    private Integer status;

    @ApiModelProperty("图片类型(0:商品图片  1:商品详情)")
    private Integer type;

    @ApiModelProperty("图片路径")
    private String url;

    @ApiModelProperty("主图(0:主图 1:辅图)")
    private Integer isMain;

    @ApiModelProperty("发布时间")
    private String  createTime;

    @ApiModelProperty("上线时间")
    private String  onTime;

    @ApiModelProperty("下线时间")
    private String  offTime;

    @ApiModelProperty("商品图片信息")
    private List<PicEntity> picList;

    @ApiModelProperty("商品详细图片信息")
    private List<PicEntity>  picDetailList;

    public GoodEntity(String id,Integer status){
        this.id =id;
        this.status = status;
        this.name = null;
        this.price = null;
    }
    public GoodEntity(){

    }



}
