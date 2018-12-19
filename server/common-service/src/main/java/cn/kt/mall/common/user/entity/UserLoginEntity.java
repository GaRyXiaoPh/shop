package cn.kt.mall.common.user.entity;

import cn.kt.mall.common.util.IDUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体
 * Created by jerry on 2017/12/21.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserLoginEntity implements Serializable {
    private static final long serialVersionUID = 1258933556810658056L;

    /** 主键 */
    private String id;

    /** 用户名 */
    private String userId;

    /** 令牌内容 */
    private String token;

    /** 密码密文：sha256hash(sha256hash("123456"), salt) */
    private Date loginTime;

    /** SHA256加密盐值 */
    private Date expireTime;

    /** 用户状态：0 登入，登出  */
    private String status;

    /** 最后更新时间 */
    private Date lastTime;

    public UserLoginEntity(String userId) {
        super();
        this.id = IDUtil.getUUID();
        this.userId = userId;
    }
}
