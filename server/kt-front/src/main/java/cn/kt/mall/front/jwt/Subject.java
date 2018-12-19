package cn.kt.mall.front.jwt;

import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.model.CurrentUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取当前登录用户信息工具类
 * Created by jerry on 2018/1/8.
 */
public class Subject {

    /**
     * 获取当前登录信息
     * @return CurrentUser
     */
    public static CurrentUser getCurrent() {
        return (CurrentUser)SubjectUtil.getCurrent();
    }

    public static CurrentUser getCurrent(HttpServletRequest request) {
        return (CurrentUser)SubjectUtil.getCurrent(request);
    }
}
