package cn.kt.mall.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;

import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * Created by jerry on 2017/12/29.
 */
public class JWTRSAUtil {

    public static void main(String[] args) throws Exception {
        AccessToken accessToken = new AccessToken("management", "abcdedf");
        Map<String, byte[]> rsaKeys = generateKey("123456");
        String publicKey = Base64.encodeBase64String(rsaKeys.get("publicKey"));
        String privateKey = Base64.encodeBase64String(rsaKeys.get("privateKey"));
        System.out.println("publicKey:");
        System.out.println(publicKey);
        System.out.println("privateKey:");
        System.out.println(privateKey);

        String jwt = generateToken(accessToken, privateKey, 120);
        System.out.println(jwt);
        AccessToken result = parserToken(jwt, publicKey);
        System.out.println(result.getType() + ":" + result.getId());
    }

    public static String generateToken(AccessToken accessToken, byte[] privateKey, int expire) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey key = kf.generatePrivate(spec);

        return Jwts.builder()
                .setSubject(accessToken.getId())
                .claim(JWTConstant.TYPE, accessToken.getType())
                .claim(JWTConstant.ID, accessToken.getId())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(SignatureAlgorithm.RS256, key)
                .compact();
    }

    public static String generateToken(AccessToken accessToken, String privateKey, int expire) throws Exception {
        return generateToken(accessToken, Base64.decodeBase64(privateKey), expire);
    }

    public static AccessToken parserToken(String token, byte[] publicKey) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey key = kf.generatePublic(spec);
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return new AccessToken(val(body.get(JWTConstant.TYPE)), val(body.get(JWTConstant.ID)));
    }

    public static AccessToken parserToken(String token, String publicKey) throws Exception {
        return parserToken(token, Base64.decodeBase64(publicKey));
    }

    public static Map<String, byte[]> generateKey(String password) throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(password.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        Map<String, byte[]> map = new HashMap<>();
        map.put("publicKey", publicKeyBytes);
        map.put("privateKey", privateKeyBytes);
        return map;
    }

    private static String val(Object obj) {
        return obj == null ? null : obj.toString();
    }
}
