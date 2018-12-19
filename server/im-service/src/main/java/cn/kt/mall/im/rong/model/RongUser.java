package cn.kt.mall.im.rong.model;

/**
 * 融云用户
 * Created by Administrator on 2017/6/18.
 */
public class RongUser {
    private String userId;
    private String name;
    private String portraitUri;

    public RongUser() {
        super();
    }

    public RongUser(String userId, String name, String portraitUri) {
        this.userId = userId;
        this.name = name;
        this.portraitUri = portraitUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }
}
