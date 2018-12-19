package cn.kt.mall.common.sms.sender;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InternationalReq {
    private String account;
    private String password;
    private String msg;
    private String mobile;

    public InternationalReq(String account, String password, String mobile, String msg) {
        this.account = account;
        this.password = password;
        this.msg = msg;
        this.mobile = mobile;
    }
}
