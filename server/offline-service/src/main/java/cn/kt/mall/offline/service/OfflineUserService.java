package cn.kt.mall.offline.service;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/28.
 */
public interface OfflineUserService {

    /**
     * 商圈用户登录
     *
     * @param username
     * @param pwd
     */
    Map login(String username, String pwd);
}
