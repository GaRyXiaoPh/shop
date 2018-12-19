package cn.kt.mall.common.mvc;

import cn.kt.mall.common.exception.BadRequestException;
import cn.kt.mall.common.exception.ForbiddenException;
import cn.kt.mall.common.exception.UnauthorizedException;
import cn.kt.mall.common.http.ResponseError;
import cn.kt.mall.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.rmi.ServerException;

/**
 * 全局异常处理
 * Created by jerry on 2017/6/26.
 */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionControllerAdvice {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionControllerAdvice.class);

    private static final String UNAUTHORIZED_ERROR = "未登录货登录状态过期，请重新登录";
    private static final String FORBIDDEN_ERROR = "你没有权限进行此操作";
    private static final String APP_ERROR = "程序错误，我们会尽快处理";
    private static final String SERVER_ERROR = "服务器错误，我们会尽快处理";

    /**
     * 未授权异常
     * 导致原因: 业务错误
     */
    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError unauthorizedException(HttpServletResponse response, Exception e) throws Exception {
        logger.debug("未授权错误，错误原因：" + e.getMessage(), e);
        return new ResponseError(HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED_ERROR, e.getMessage());
    }

    /**
     * 拒绝访问
     * 导致原因: 业务错误
     */
    @ExceptionHandler(value = {ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError forbiddenException(HttpServletResponse response, Exception e) throws Exception {
        logger.debug("拒绝请求错误，错误原因：" + e.getMessage(), e);
        return new ResponseError(HttpStatus.FORBIDDEN.value(), FORBIDDEN_ERROR, e.getMessage());
    }

    /**
     * 请求错误异常
     * 导致原因: 请求参数错误
     */
    @ExceptionHandler(value = {BadRequestException.class, ServletException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError badRequestException(HttpServletResponse response, Exception e) throws Exception {
        logger.info("请求错误，错误原因：" + e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), APP_ERROR, e.getMessage());
    }

    /**
     * 业务级异常
     * 导致原因: 业务错误
     */
    @ExceptionHandler(value = {BusinessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError businessException(HttpServletResponse response, BusinessException e) throws Exception {
        logger.info("业务错误，错误原因：" + e.getMessage());
        return new ResponseError(e.getMessage());
    }

    /**
     * 系统级异常
     * 导致原因: 程序错误，或系统环境问题
     */
    @ExceptionHandler(value = {Exception.class, ServerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError serverError(HttpServletResponse response, Exception e) throws Exception {
        logger.error("系统错误，错误原因：" + e.getMessage(), e);
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), SERVER_ERROR, e.getMessage());
    }
}
