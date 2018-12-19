package cn.kt.mall.common.jwt;

import cn.kt.mall.common.asserts.A;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取当前登录主体
 * Created by wqt on 2018/2/1.
 */
public class SubjectUtil {

    public static SubjectInstance getCurrent() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getCurrent(request);
    }

    public static SubjectInstance getCurrent(HttpServletRequest request) {
        Object obj = request.getAttribute("current");
        A.checkAuth(obj == null, "未登录或登录状态已过期，请重新登录");
        return (SubjectInstance) obj;
    }

    public static SubjectInstance getCurrentShop(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object obj = request.getAttribute("currentShop");
        A.checkAuth(obj == null, "未登录或登录状态已过期，请重新登录");
        return (SubjectInstance) obj;
    }

    public static SubjectInstance getCurrentNoThrows(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object obj = request.getAttribute("current");
        if(obj == null){
            return null;
        }else{
            return (SubjectInstance) obj;
        }
    }
}
