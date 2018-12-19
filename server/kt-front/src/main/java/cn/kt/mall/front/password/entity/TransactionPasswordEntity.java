package cn.kt.mall.front.password.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 交易密码实体类
 * Created by wqt on 2018/1/29.
 */
@Getter
@Setter
@NoArgsConstructor
public class TransactionPasswordEntity implements Serializable {

	private static final long serialVersionUID = -1788004011208004009L;

	private String id;

    /** 密码密文 */
    private String password;

    /** 密码盐 */
    private String salt;

    private String creator;

    public TransactionPasswordEntity(String id, String creator, String password, String salt) {
        this.id = id;
        this.password = password;
        this.salt = salt;
        this.creator=creator;
    }
}
