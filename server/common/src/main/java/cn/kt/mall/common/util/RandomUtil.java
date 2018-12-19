package cn.kt.mall.common.util;

import java.util.Random;

/**
 * 随机数工具类
 * Created by Administrator on 2017/6/18.
 */
public class RandomUtil {

    /**
     * 获取随机码
     * @param length 长度,最大10位
     * @return String
     */
    public static String getCode(int length) {
        if(length < 1 && length > 10) {
            return null;
        }
        int max = (int)Math.pow(10, length);
        int rand = new Random().nextInt(max);
        return String.valueOf(max + rand).substring(1);
    }
}
