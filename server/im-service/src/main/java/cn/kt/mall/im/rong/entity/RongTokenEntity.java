package cn.kt.mall.im.rong.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 融云Token
 * 绑定每个用户(永久性)
 * Created by Administrator on 2017/6/16.
 */
public class RongTokenEntity implements Serializable {
    private String id;

    /** 用户Id */
    private String userId;

    /** 融云令牌 */
    private String token;

    /** 状态：0 正常,1 无效' */
    private String status;

    /** 创建时间 */
    private Date createTime;

    public RongTokenEntity() {
        super();
    }

    public RongTokenEntity(String id, String userId, String token) {
        this.id = id;
        this.userId = userId;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
