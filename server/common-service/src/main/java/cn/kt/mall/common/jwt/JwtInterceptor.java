package cn.kt.mall.common.jwt;

import cn.kt.mall.common.exception.ForbiddenException;
import cn.kt.mall.common.exception.UnauthorizedException;
import cn.kt.mall.common.http.ResponseError;
import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.common.constant.Constants;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 验证拦截器
 * Created by jerry on 2018/1/2.
 */
@ConditionalOnBean(JwtValidator.class)
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    private JwtValidator jwtValidator;
    private PermissionValidator permissionValidator;

    public JwtInterceptor(JwtValidator jwtValidator, PermissionValidator permissionValidator) {
        this.jwtValidator = jwtValidator;
        this.permissionValidator = permissionValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        IgnoreJwtAuth classAnnotation = handlerMethod.getBeanType().getAnnotation(IgnoreJwtAuth.class);
        IgnoreJwtAuth methodAnnotation = handlerMethod.getMethodAnnotation(IgnoreJwtAuth.class);
        ShopAuth methodAuth = handlerMethod.getMethodAnnotation(ShopAuth.class);

        String jwt = request.getHeader(Constants.Header.ACCESS_TOKEN);
        if (jwt == null) {
            //在导出excel的接口中，
            jwt = request.getParameter(Constants.Header.ACCESS_TOKEN);
        }

        if(classAnnotation != null || methodAnnotation != null) {
            return super.preHandle(request, response, handler);
        }

        try {
            // 检查JWT有效性
            SubjectInstance subject = jwtValidator.verify(jwt);
            request.setAttribute("current", subject);
            if(methodAuth != null){
                request.setAttribute("currentShop", jwtValidator.verifyShop(subject.getId()));
            }
            // 检查用户权限
            checkPermission(handlerMethod, subject.getId());
        } catch (UnauthorizedException ex) {
            responseError(response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return false;
        } catch (ForbiddenException ex) {
            responseError(response, HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
            return false;
        } catch (Exception ex) {
            responseError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ResponseMessage.ERROR);
            return false;
        }

        return super.preHandle(request, response, handler);
    }

    private void checkPermission(HandlerMethod handlerMethod, String userId) {
        PermissionCheck classAnnotation = handlerMethod.getBeanType().getAnnotation(PermissionCheck.class);
        PermissionCheck methodAnnotation = handlerMethod.getMethodAnnotation(PermissionCheck.class);
        if(classAnnotation == null && methodAnnotation == null) {
            return;
        }
        String[] roles = {}, privileges = {};
        if(classAnnotation != null) {
            roles = (String[])ArrayUtils.addAll(roles, classAnnotation.roles());
            privileges = (String[])ArrayUtils.addAll(privileges, classAnnotation.privileges());
        }

        if(methodAnnotation != null) {
            roles = (String[])ArrayUtils.addAll(roles, methodAnnotation.roles());
            privileges = (String[])ArrayUtils.addAll(privileges, methodAnnotation.privileges());
        }

        this.permissionValidator.check(userId, roles, privileges);
    }

    private void responseError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        String responseError = JSONUtil.toJSONString(new ResponseError(status, message, null));
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.getWriter().write(responseError);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
