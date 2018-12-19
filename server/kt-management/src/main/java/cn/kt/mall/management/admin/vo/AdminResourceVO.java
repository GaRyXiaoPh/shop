package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminResourceVO {
    @ApiModelProperty("id，关联角色时需要")
    private Integer id;
    @ApiModelProperty("显示名称，比如会员管理")
    private String name;
    @ApiModelProperty("当前角色是否已经分配了这个权限")
    private Boolean selected = false;
    private Integer parentId;
    private List<AdminResourceVO> childList;

    public void addChild(AdminResourceVO child) {
        if (childList == null) {
            childList = new ArrayList<>();
        }

        childList.add(child);
    }

}
