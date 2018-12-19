package cn.kt.mall.common.util;

public class EnumUtil {

    public static <T extends Enum<T>> T getField(Class<T> tClass, String code) {
        if(code == null) {
            return null;
        }

        try {
            return Enum.valueOf(tClass, code);
        } catch (Exception ex) {
            return null;
        }
    }
}
