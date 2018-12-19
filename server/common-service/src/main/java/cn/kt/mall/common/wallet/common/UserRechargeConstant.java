package cn.kt.mall.common.wallet.common;

public class UserRechargeConstant {
    //1:充值 2:扣除
    public static String USER_RECHARGE = "1";
    public static String USER_DEDUCTION = "2";
    //充值人 区分
    public static String USER_ADMIN_SHOP = "1";
    public static String USER_USER_SHOP = "2";

    //日志状态
    public static String LOG_DEFAULT = "0";
    public static String LOG_TAKE_EFFECT = "1";
    public static String LOG_INVALID = "2";
    //操作人类型 1：后台管理员;2:商户后台
    public static String TYPE_ADMIN = "1";
    public static String  TYPE_SHOP_USER = "2";
}
