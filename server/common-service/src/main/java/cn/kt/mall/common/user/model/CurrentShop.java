package cn.kt.mall.common.user.model;

import cn.kt.mall.common.jwt.SubjectInstance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 当前登录的用户的shopId
 */
@Getter
@Setter
@NoArgsConstructor
public class CurrentShop implements SubjectInstance {

    private String id;

    @Override
    public String getId() {
        return id;
    }
    @Override
    public String getAccessToken() {
        return null;
    }
}
