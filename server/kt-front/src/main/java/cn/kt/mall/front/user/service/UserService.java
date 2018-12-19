package cn.kt.mall.front.user.service;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.JwtValidator;
import cn.kt.mall.common.jwt.PermissionValidator;
import cn.kt.mall.common.user.entity.CertificationEntity;
import cn.kt.mall.common.user.model.CurrentUser;
import cn.kt.mall.common.user.model.UserInfo;
import cn.kt.mall.common.user.vo.*;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.shop.coupon.vo.CouponsVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 用户业务接口类
 * Created by jerry on 2017/12/21.
 */
public interface UserService extends JwtValidator, PermissionValidator {

    void register(RegisterVO registerVO);

    String usernameIsValid(String username);

    void forget(ForgetVO forgetVO);

    void updatePassword(CurrentUser currentUser, PasswordVO passwordVO);

    void resetPassword(String mobile,String passwordVO);

    LoginInfoVO login(LoginVO loginVO);

    boolean check(String accessToken);

    void logout(String accessToken);

    CurrentUser getCurrent(String accessToken);

    UserInfo getUserById(String id);

    public UserInfo getUserByUserId(String id);

    UserInfo queryUserById(String userId);

    UserInfo getUserByUsername(String username);

    void editPersonalInfo(EditVO editVO);

    void updateTransactionPassword(String userId);

    void changeMobileCaptcha(CurrentUser currentUser, String nationalCode, String mobile);

    void changeMobile(CurrentUser currentUser, ChangeMobileVO changeMobileVO);

    void addBankMessage(BankCardVO bankCardVO);

    void updateBankMessage(BankCardVO bankCardVO);

    void addCertificationMessage(CertificationVO certificationVO);

    void updatCertificationMessage(CertificationVO bankCardVO);

    void addTransactionPassword(String userId,String transactionPassword);

    void editTransactionPassword(CurrentUser currentUser, PasswordVO passwordVO);

    CertificationEntity getCertificationMessage(String userId);

    UserConsumeVO getUserConsume(String userId);
}
