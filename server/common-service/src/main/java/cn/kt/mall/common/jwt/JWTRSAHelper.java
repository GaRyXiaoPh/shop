package cn.kt.mall.common.jwt;

import cn.kt.mall.common.exception.ServerException;
import cn.kt.mall.common.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * JWT 工具类
 * Created by jerry on 2017/12/29.
 */
@ConditionalOnProperty({"jwt.publicKey", "jwt.privateKey"})
@Component
public class JWTRSAHelper {

    private static Logger logger = LoggerFactory.getLogger(JWTRSAHelper.class);

    @Value("${jwt.expire}")
    private int expire;

    @Value("${jwt.publicKey}")
    private String publicKey;

    @Value("${jwt.privateKey}")
    private String privateKey;

    public JWTRSAHelper() {
        super();
    }

    public JWTRSAHelper(int expire, String publicKey, String privateKey) {
        super();
        this.expire = expire;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * 生成JWT访问令牌
     * @param accessToken 令牌信息
     * @return String
     */
    public String generate(AccessToken accessToken) {
        try {
            return JWTRSAUtil.generateToken(accessToken, privateKey, expire);
        } catch (Exception e) {
            throw new ServerException("生成JWT错误", e);
        }
    }

    /**
     * 解析JWT负载信息
     * @param token JWT访问令牌
     * @return AccessToken
     */
    public AccessToken parserToken(String token) {
        try {
            return JWTRSAUtil.parserToken(token, publicKey);
        } catch (ExpiredJwtException e){
            throw new UnauthorizedException("登录状态已过期，请重新登录", e);
        } catch (MalformedJwtException e) {
            logger.error("解析JWT错误，错误原因：" + e.getMessage(), e);
            throw new UnauthorizedException("非法的请求", e);
        } catch (Exception e) {
            logger.error("解析JWT错误，错误原因：" + e.getMessage(), e);
            throw new UnauthorizedException("用户登录状态异常", e);
        }
    }

    public int getExpire() {
        return expire;
    }
}
