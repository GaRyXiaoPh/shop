package cn.kt.mall.common.jwt;

/**
 * Jwt 主体实例基类
 * Created by wqt on 2018/2/1.
 */
public interface SubjectInstance {

    String getId();

    String getAccessToken();
}
