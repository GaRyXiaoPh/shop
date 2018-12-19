package cn.kt.mall.common.user.entity;


import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.user.model.UserLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户实体
 * Created by jerry on 2017/12/21.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserEntity implements Serializable {
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

    /** SHA256加密盐值 */
    private String transcationSalt;

    //头像URL
    private String avatar;

    /** 创建时间 or 注册时间 */
    private Date createTime;

    /** 最后更新时间 */
    private Date lastTime;
    //备注名
    private String remarkName;

    private String trueName;

    //隶属站长/主任ID
    private String pid;

    //会员信用金
    private BigDecimal point;
    //用户等级
    private int userlevel;
    //站点编号
    private String standNo;

    //实名认证状态
    private String  certificationStatus;
    //实名认证类别
    private String  certificateType;
    //信用分
    private BigDecimal  xin;

    //团队总人数
    private int teamCount;

    public UserEntity(String username, String nationalCode, String mobile, String password, String salt, String nick, String avatar, String referrer,String pid,String standNo) {
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
        this.pid =pid;
        this.standNo =standNo;
    }
}
