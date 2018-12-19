package cn.kt.mall.common.sms.entity;

import java.util.Date;

/**
 * 短信验证码实体类
 * Created by Administrator on 2017/6/18.
 */
public class SmsCaptcha {
    private String id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

    /**
     * 状态: 0 可用, 1 失效
     */
    private String status;

    /**
     * 创建时间
     */
    private Date lastTime;

    public SmsCaptcha() {
        super();
    }

    public SmsCaptcha(String mobile, String code) {
        this.mobile = mobile;
        this.code = code;
    }

    public SmsCaptcha(String id, String mobile, String code) {
        this.id = id;
        this.mobile = mobile;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
}
