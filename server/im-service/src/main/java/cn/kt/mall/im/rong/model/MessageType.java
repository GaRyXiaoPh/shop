package cn.kt.mall.im.rong.model;

/**
 * 融云推送消息类型
 * Created by wangjie on 2017/7/20.
 */
public enum MessageType {
    /** 群创建 */
    GROUP_CREATE("GROUP_CREATE"),

    /** 修改群名称 */
    GROUP_RENAME("GROUP_RENAME"),

    /** 修改群公告 */
    GROUP_BROAD("GROUP_BROAD"),

    /** 增加群成员 */
    GROUP_ADD_MEMBER("GROUP_ADD_MEMBER"),

    /** 添加好友申请 */
    FRIEND_INVITE("FRIEND_INVITE"),
    
    /** 莱粉圈 */
    MOMENTS_MESSAGE("MOMENTS_MESSAGE"),

    /** 删除好友 */
    FRIEND_DELETE("FRIEND_DELETE"),
	
    /** 领取红包  */
	RED_GET("RED_GET"),
	
	SYSTEM("SYSTEM");

    private String code;

    MessageType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
