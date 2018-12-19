package cn.kt.mall.management.admin.service;

import cn.kt.mall.common.admin.model.AdminInfo;
import cn.kt.mall.common.admin.model.CurrentAdmin;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.JwtValidator;
import cn.kt.mall.common.jwt.PermissionValidator;
import cn.kt.mall.common.user.vo.PasswordVO;
import cn.kt.mall.management.admin.vo.AddAdminVO;
import cn.kt.mall.management.admin.vo.LoginInfoVO;
import cn.kt.mall.management.admin.vo.LoginVO;

import java.util.List;

/**
 * 用户业务接口类
 * Created by jerry on 2017/12/21.
 */
public interface AdminService extends JwtValidator, PermissionValidator {

    void add(AddAdminVO adminVO);

    void updatePassword(CurrentAdmin currentAdmin, PasswordVO passwordVO);

    LoginInfoVO login(LoginVO loginVO);

    boolean check(String accessToken);

    void logout(String accessToken);

    CurrentAdmin getCurrent(String accessToken);

    AdminInfo getUserById(String id);

    AdminInfo getUserByUsername(String username);

    void check(String userId, String[] roles, String[] privileges);

    List<AdminInfo> getUserList();

    void deleteUser(String[] userIds);
}
