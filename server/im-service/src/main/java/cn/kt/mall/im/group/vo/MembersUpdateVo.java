package cn.kt.mall.im.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/27.
 */
@Setter
@Getter
@NoArgsConstructor
public class MembersUpdateVo  implements Serializable{

    @ApiModelProperty(notes = "标识符(1:添加成员 2:删除成员)", dataType = "int")
    private int flag;
    @ApiModelProperty(notes = "群Id", dataType = "string")
    private String groupId;
    @ApiModelProperty(notes = "添加成员或者删除成员ids", dataType = "string[]")
    private String[] userIds;

}
