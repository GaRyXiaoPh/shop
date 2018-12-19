package cn.kt.mall.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 金额运算工具类
 * Created by jerry on 2017/12/28.
 */
public class MoneyUtil {
    private static final int LEN = 2;

    private static DecimalFormat DF_INT = new DecimalFormat("#,###");

    private static DecimalFormat DF_MONEY = new DecimalFormat("#,##0.00");

    private static DecimalFormat DF_BALANCE = new DecimalFormat("#,##0.00######");

    public static double add(double val1, double val2) {
        return BigDecimal.valueOf(val1).add(BigDecimal.valueOf(val2)).doubleValue();
    }

    public static double subtract(double val1, double val2) {
        return BigDecimal.valueOf(val1).subtract(BigDecimal.valueOf(val2)).doubleValue();
    }

    public static double multiply(double val1, double val2) {
        return BigDecimal.valueOf(val1).multiply(BigDecimal.valueOf(val2)).doubleValue();
    }

    public static double multiply(BigDecimal val1, double val2) {
        return val1.multiply(BigDecimal.valueOf(val2)).doubleValue();
    }

    public static double divide(double val1, double val2) {
        return BigDecimal.valueOf(val1).divide(BigDecimal.valueOf(val2), LEN, BigDecimal.ROUND_DOWN).doubleValue();
    }

    public static double divide(BigDecimal val1, double val2) {
        return val1.divide(BigDecimal.valueOf(val2), LEN, BigDecimal.ROUND_DOWN).doubleValue();
    }

    public static String getValue(BigDecimal balance, RoundingMode roundingMode) {
        DF_BALANCE.setRoundingMode(roundingMode);
        return DF_BALANCE.format(balance);
    }

    public static String getValue(BigDecimal balance) {
        DF_BALANCE.setRoundingMode(RoundingMode.DOWN);
        return DF_BALANCE.format(balance);
    }

    public static String getMoney(BigDecimal balance, RoundingMode roundingMode) {
        DF_MONEY.setRoundingMode(roundingMode);
        return DF_MONEY.format(balance);
    }

    public static String getMoney(BigDecimal balance) {
        DF_MONEY.setRoundingMode(RoundingMode.DOWN);
        return DF_MONEY.format(balance);
    }

    public static String getInteger(BigDecimal balance, RoundingMode roundingMode) {
        DF_INT.setRoundingMode(roundingMode);
        return DF_INT.format(balance);
    }

    public static String getInteger(BigDecimal balance) {
        DF_INT.setRoundingMode(RoundingMode.DOWN);
        return DF_INT.format(balance);
    }

    public static void main(String[] args) {
        System.out.println(getValue(BigDecimal.valueOf(1456464654.46456465465464)));
        System.out.println(getMoney(BigDecimal.valueOf(1456464654.46456465465464)));
    }
}
