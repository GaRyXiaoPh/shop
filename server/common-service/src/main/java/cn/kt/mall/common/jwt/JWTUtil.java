package cn.kt.mall.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;

/**
 * JWT 工具类
 * Created by jerry on 2017/12/29.
 */
public class JWTUtil {

    public static void main(String[] args) throws Exception {
        String secret = generateHMACSecret();
        AccessToken accessToken = new AccessToken("management", "123456");
        String token = generateToken(accessToken, secret, 2);
        System.out.println(token);
        Thread.sleep(3000);
        AccessToken result = parserToken(token, secret);
        System.out.println("type:" + result.getType() + ", id:" + result.getId());
    }

    public static String generateHMACSecret() {
        return Base64.encodeBase64String(MacProvider.generateKey(SignatureAlgorithm.HS256).getEncoded());
    }

    public static String generateToken(AccessToken accessToken, String secret, int expire) throws Exception {
        return Jwts.builder()
                .setSubject(accessToken.getId())
                .claim(JWTConstant.TYPE, accessToken.getId())
                .claim(JWTConstant.ID, accessToken.getId())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public static AccessToken parserToken(String token, String secret) throws Exception {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return new AccessToken(val(body.get(JWTConstant.TYPE)), val(body.get(JWTConstant.ID)));
    }

    private static String val(Object obj) {
        return obj == null ? null : obj.toString();
    }
}
