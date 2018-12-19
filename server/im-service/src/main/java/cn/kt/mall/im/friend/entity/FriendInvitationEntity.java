package cn.kt.mall.im.friend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 好友邀请记录实体类
 * Created by Administrator on 2017/6/18.
 */
@Setter
@Getter
@NoArgsConstructor
public class FriendInvitationEntity implements Serializable {
    private static final long serialVersionUID = 953789989434444848L;

    private String id;

    /** 邀请人Id */
    private String userId;

    /** 被邀请人Id(好友Id) */
    private String friendId;

    /** 状态：0 未确认，1 接受, 2 拒绝 */
    private String status;

    /** 邀请消息 */
    private String message;

    /** 创建时间 */
    private Date createTime;

    /** 最后更新时间 */
    private Date lastTime;

    public FriendInvitationEntity(String id, String userId, String friendId, String status, String message){
        this.id = id;
        this.userId = userId;
        this.friendId=friendId;
        this.status=status;
        this.message=message;
    }
}
