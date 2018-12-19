package cn.kt.mall.im.group.entity;

import cn.kt.mall.im.friend.vo.FriendshipVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
public class GroupChatEntity implements Serializable {
    private static final long serialVersionUID = -7143004707305021148L;

    @ApiModelProperty(notes = "群ID", dataType = "string")
    private String id;
    @ApiModelProperty(notes = "群名称", dataType = "string")
    private String name;
    @ApiModelProperty(notes = "群主", dataType = "string")
    private String master;
    @ApiModelProperty(notes = "创建者", dataType = "string")
    private String creator;
    @ApiModelProperty(notes = "头像", dataType = "string")
    private String avatar;
    @ApiModelProperty(notes = "公告", dataType = "string")
    private String board;
    @ApiModelProperty(notes = "创建时间", dataType = "string")
    private Date createTime;
    @ApiModelProperty(notes = "昵称", dataType = "string")
    private String  nickname;
    @ApiModelProperty(notes = "是否是群主(1:群主)", dataType = "String")
    private String  isManager;
    @ApiModelProperty(notes = "成员列表", dataType = "list")
    private List<FriendshipVO> memberList;

    public GroupChatEntity(String id, String name, String master, String creator, String avatar, String board){
        this.id=id;
        this.name = name;
        this.master = master;
        this.creator = creator;
        this.avatar = avatar;
        this.board = board;
        this.createTime = new Date();
    }
}
