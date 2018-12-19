package cn.kt.mall.management.jwt;

import cn.kt.mall.common.admin.model.CurrentAdmin;
import cn.kt.mall.common.jwt.SubjectUtil;

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
    public static CurrentAdmin getCurrent() {
        return (CurrentAdmin) SubjectUtil.getCurrent();
    }

    public static CurrentAdmin getCurrent(HttpServletRequest request) {
        return (CurrentAdmin)SubjectUtil.getCurrent(request);
    }
}
