package cn.kt.mall.im.group.constant;

/**
 * 好友模块常量类
 * Created by wangjie on 2017/7/25.
 */
public class Constants {

    /** 邀请状态 */
    public class InvitationStatus {
        /** 好友-邀请状态-已提交 */
        public static final String NORMAL = "0";

        /** 好友-邀请状态-同意 */
        public static final String AGREEMENT = "1";

        /** 好友-邀请状态-拒绝 */
        public static final String REJECT = "2";
    }

    /** 邀请结果 */
    public class InvitationResult {
        /** 好友-邀请结果-同意 */
        public static final String AGREEMENT = "1";

        /** 好友-邀请结果-拒绝 */
        public static final String REJECT = "2";
    }
}
