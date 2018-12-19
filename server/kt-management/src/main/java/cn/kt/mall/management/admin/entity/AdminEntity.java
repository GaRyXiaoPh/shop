package cn.kt.mall.management.admin.entity;


import cn.kt.mall.common.util.IDUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 用户实体
 * Created by jerry on 2017/12/21.
 */
@Getter
@Setter
@NoArgsConstructor
public class AdminEntity implements Serializable {
    private static final long serialVersionUID = -1661830493897252605L;

    /** 主键 */
    private String id;

    /**账号*/
    private String account;

    /** 用户名 */
    private String username;
    //职位名称
    private String position;

    /** 角色id */
    private Integer roleId;

    /** 密码密文：sha256hash(sha256hash("123456"), salt) */
    private String password;

    /** SHA256加密盐值 */
    private String salt;

    /** 创建者 */
    private String creator;

    /** 备注 */
    private String remark;

    /** 用户状态：0 正常，1 禁用 */
    private String status;

    /** 创建时间 or 注册时间 */
    private Date createTime;

    /** 最后更新时间 */
    private Date lastTime;

    public AdminEntity(String account,String username, String password, String salt,String remark,String userId,String id) {
        this.id = id;
        this.account = account;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.remark = remark;
        this.creator = userId;
    }
}
