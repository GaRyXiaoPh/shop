package cn.kt.mall.common.sms.entity;

import cn.kt.mall.common.util.IDUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信发送记录
 * Created by wqt on 2017/11/27.
 */
@Getter
@Setter
@NoArgsConstructor
public class SmsRecordEntity implements Serializable {
    private static final long serialVersionUID = -2500243324273257903L;

    /** 主键Id */
    private String id;

    /** 手机号 */
    private String mobile;

    /** 短信类型：01 验证码短信 */
    private String type;

    /** 短信内容 */
    private String content;

    /** 创建人Id */
    private String creator;

    /** 创建时间 */
    private Date createTime;

    public SmsRecordEntity(String mobile, String type, String content, String creator) {
        super();
        this.id = IDUtil.getUUID();
        this.mobile = mobile;
        this.type = type;
        this.content = content;
        this.creator = creator;
    }
}
