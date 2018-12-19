package cn.kt.mall.offline.entity;

import cn.kt.mall.common.user.model.UserLevel;
import cn.kt.mall.common.util.IDUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/28.
 */
@Setter
@Getter
@NoArgsConstructor
public class OfflineUserEntity  implements Serializable{
        private static final long serialVersionUID = -1661830493897252605L;

        /** 主键 */
        private String id;

        /** 用户名 */
        private String username;

        /** 手机号 */
        private String mobile;

        /** 国家地区编码 */
        private String nationalCode;

        /** 密码密文：sha256hash(sha256hash("123456"), salt) */
        private String password;

        /** SHA256加密盐值 */
        private String salt;

        /** 昵称 */
        private String nick;

        /** 推荐人 */
        private String referrer;

        /** 用户等级 */
        private String level;

        /** 用户状态：0 正常，1 禁用 */
        private String status;

        /** 是否设置交易密码：0 否，1 是 */
        private String transactionPassword;

        //头像URL
        private String avatar;

        /** 创建时间 or 注册时间 */
        private Date createTime;

        /** 最后更新时间 */
        private Date lastTime;

        public OfflineUserEntity(String username, String nationalCode, String mobile, String password, String salt, String nick, String avatar, String referrer) {
            this.id = IDUtil.getUUID();
            this.username = username;
            this.nationalCode = nationalCode;
            this.mobile = mobile;
            this.password = password;
            this.salt = salt;
            this.nick = nick;
            this.referrer = referrer;
            this.avatar = avatar;
            this.level = UserLevel.DEFAULT.getCode();
        }

}
