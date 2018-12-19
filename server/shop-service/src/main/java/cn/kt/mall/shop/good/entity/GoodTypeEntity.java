package cn.kt.mall.shop.good.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

@Setter
@Getter
@NoArgsConstructor
public class GoodTypeEntity implements Serializable {
    private static final long serialVersionUID = 8039893753017467969L;

    @ApiModelProperty(value = "新增传空，修改传当前编辑的id")
    private String id;
    @ApiModelProperty(value = "分类名称", required = true)
    @NotBlank
    private String name;
    @ApiModelProperty(value = "分类图片", required = true)
    private String img;
    @ApiModelProperty(value = "分类排序", required = true)
    @NotNull
    private Integer sort;
    @ApiModelProperty(value = "上级id,第一级为0", required = true)
    @NotBlank
    private String parentId;
    @ApiModelProperty(hidden = true)
    private Date createTime;

    public GoodTypeEntity(String id, String name, String img, int sort, String parentId){
        this.id = id;
        this.name = name;
        this.img = img;
        this.sort = sort;
        this.parentId = parentId;
        this.createTime = new Date();
    }

}
