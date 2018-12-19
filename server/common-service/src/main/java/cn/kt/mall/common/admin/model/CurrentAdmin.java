package cn.kt.mall.common.admin.model;

import cn.kt.mall.common.jwt.SubjectInstance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 当前登录用户信息
 * Created by jerry on 2017/12/28.
 */
@Getter
@Setter
@NoArgsConstructor
public class CurrentAdmin extends AdminInfo implements SubjectInstance{

    private String token;

    @Override
    public String getAccessToken() {
        return token;
    }
}
