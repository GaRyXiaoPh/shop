package cn.kt.mall.common.util;


import org.apache.commons.codec.digest.Sha2Crypt;

/**
 * 密码加密工具类
 * Created by jerry on 2017/12/29.
 */
public class PasswordUtil {

    public static String[] getEncryptPassword(String password) {
        String salt = B64Copy.getRandomSaltForSha256();
        return new String[]{Sha2Crypt.sha256Crypt(password.getBytes(), salt), salt};
    }

    public static boolean check(String input, String password, String salt) {
        return password.equals(Sha2Crypt.sha256Crypt(input.getBytes(), salt));
    }

    public static void main(String[] args) {
        System.out.println(Sha2Crypt.sha256Crypt("123456".getBytes(), "$5$BKMxK0alsFAzfJnQ"));
    }
}
