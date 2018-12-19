package cn.kt.mall.common.util;

import cn.kt.mall.common.constant.Constants;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64工具类
 * Created by wqt on 2017/8/7.
 */
public class Base64Utils {

    public static String encode(byte[] data) throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(data), Constants.CHARSET);
    }

    public static byte[] decode(String source) throws UnsupportedEncodingException {
        return Base64.getDecoder().decode(source.getBytes(Constants.CHARSET));
    }
}
