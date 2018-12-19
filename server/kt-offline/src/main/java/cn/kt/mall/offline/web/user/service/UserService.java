package cn.kt.mall.offline.web.user.service;

import cn.kt.mall.common.jwt.JwtValidator;
import cn.kt.mall.common.jwt.PermissionValidator;
import cn.kt.mall.common.user.model.CurrentUser;
import cn.kt.mall.common.user.model.UserInfo;
import cn.kt.mall.common.user.vo.*;
import cn.kt.mall.common.user.vo.ForgetVO;
import cn.kt.mall.common.user.vo.LoginInfoVO;

/**
 * 用户业务接口类
 * Created by jerry on 2017/12/21.
 */
public interface UserService extends JwtValidator, PermissionValidator {

    void register(RegisterVO registerVO);

    String usernameIsValid(String username);

    void forget(ForgetVO forgetVO);

    void updatePassword(CurrentUser currentUser, PasswordVO passwordVO);

    LoginInfoVO login(LoginVO loginVO);

    boolean check(String accessToken);

    void logout(String accessToken);

    CurrentUser getCurrent(String accessToken);

    UserInfo getUserById(String id);

    UserInfo queryUserById(String id, String userId);

    UserInfo getUserByUsername(String username);

    void editPersonalInfo(EditVO editVO);

    void updateTransactionPassword(String userId);

    void changeMobileCaptcha(CurrentUser currentUser, String nationalCode, String mobile);

    void changeMobile(CurrentUser currentUser, ChangeMobileVO changeMobileVO);
}
