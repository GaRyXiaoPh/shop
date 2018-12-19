package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.poifs.property.Child;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@ApiModel("资源操作权限列表")
@Data
public class AdminResourcesVO implements Serializable {
    private static final long serialVersionUID = -1661830493897252605L;
    @ApiModelProperty("资源ID")
    private Integer resId;
    @ApiModelProperty("资源名称")
    private String name;
    @ApiModelProperty("资源类别(1:一级菜单,2:二级菜单,3:三级菜单)")
    private Integer resLever;
    @ApiModelProperty("资源URL")
    private String url;
    @ApiModelProperty("父菜单ID")
    private Integer preId;
    @ApiModelProperty("排序(菜单顺序)")
    private Integer sortIndex;
    @ApiModelProperty("是否拥有该资源权限")
    private Boolean selected = false;
    @ApiModelProperty("子资源列表")
    private List<AdminResourcesVO> childMenus;
    @ApiModelProperty("子操作列表")
    private List<AdminOperationVO> menuOperations;
}
