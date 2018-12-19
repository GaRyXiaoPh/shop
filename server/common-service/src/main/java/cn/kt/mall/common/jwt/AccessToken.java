package cn.kt.mall.common.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户访问令牌
 * Created by jerry on 2017/12/29.
 */
@Getter
@Setter
@NoArgsConstructor
public class AccessToken implements Serializable {
    private static final long serialVersionUID = 8031020212771186266L;

    /** 主体唯一标识：用户主键 */
    private String id;

    /** 令牌类型：management 前端用户, management 管理员用户 */
    private String type;

    AccessToken(String type, String id) {
        this.id = id;
        this.type = type;
    }

    public static AccessToken get(String type, String id) {
        return new AccessToken(type, id);
    }
}
