package cn.kt.mall.im.friend.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 好友实体类
 * Created by wangjie on 2017/6/18.
 */
@Setter
@Getter
public class FriendEntity implements Serializable{
    private static final long serialVersionUID = 9191649161124381900L;

    private String id;

    /** 用户Id */
    private String userId;

    /** 朋友Id */
    private String friendId;

    /** 星级好友：1 是, 0 否 */
    private String star;

    /** 不看他的朋友圈:1 是，0 否 */
    private String invisibleFriend;

    /** 不让他看我的朋友圈:1 是，0 否 */
    private String invisibleMe;

    /** 创建时间 */
    private Date createTime;


    public FriendEntity(String id, String userId, String friendId){
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.star = "0";
        this.invisibleFriend="0";
        this.invisibleMe="0";
    }
}
