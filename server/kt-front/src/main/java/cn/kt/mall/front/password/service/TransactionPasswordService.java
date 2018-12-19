package cn.kt.mall.front.password.service;

import cn.kt.mall.common.user.model.CurrentUser;
import cn.kt.mall.front.password.vo.SetPasswordVO;


/**
 * 交易密码服务接口
 * Created by wqt on 2018/1/29.
 */
public interface TransactionPasswordService {

    String hasSettings(String userId);

    void setPassword(CurrentUser currentUser, SetPasswordVO password);

    void updatePassword(CurrentUser currentUser, SetPasswordVO passwordVO);

    void check(String userId, String password);
}
