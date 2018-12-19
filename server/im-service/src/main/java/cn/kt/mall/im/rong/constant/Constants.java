package cn.kt.mall.im.rong.constant;

/**
 * 融云推送常量类
 * Created by Administrator on 2017/6/29.
 */
public class Constants {

    /** 融云推送类型 */
    public final class Type {

        /** 群创建 */
        public static final String GROUP_CREATE = "GROUP_CREATE";

        /** 修改群名称 */
        public static final String GROUP_RENAME = "GROUP_RENAME";

        /** 修改群公告 */
        public static final String GROUP_BROAD = "GROUP_BROAD";

        /** 增加群成员 */
        public static final String GROUP_ADD_MEMBER = "GROUP_ADD_MEMBER";
    }

    public final class TokenStatus {
        //token 正常
        public static final String TOKEN_NORMAL = "0";
        //token 无效
        public static final String TOKEN_INVALID = "1";
    }
}
