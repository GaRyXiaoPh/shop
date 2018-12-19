package cn.kt.mall.common.user.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户信息VO
 * Created by jerry on 2017/12/21.
 */
@Getter
@Setter
@NoArgsConstructor
public class AdminVO {
    private String username;
    private String password;
    private String nick;
}
