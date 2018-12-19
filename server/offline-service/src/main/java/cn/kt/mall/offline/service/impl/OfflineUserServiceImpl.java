package cn.kt.mall.offline.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.jwt.AccessToken;
import cn.kt.mall.common.jwt.JWTConstant;
import cn.kt.mall.common.jwt.JWTRSAHelper;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.dao.UserLoginDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.util.PasswordUtil;
import cn.kt.mall.common.util.RegexUtil;
import cn.kt.mall.offline.dao.OfflineUserDAO;
import cn.kt.mall.offline.entity.OfflineUserEntity;
import cn.kt.mall.offline.service.OfflineUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/28.
 */
@Service("offlineUserService")
public class OfflineUserServiceImpl implements OfflineUserService{

    private static Logger logger = LoggerFactory.getLogger(OfflineUserServiceImpl.class);

    @Autowired
    private OfflineUserDAO userDAO;

    @Autowired
    private JWTRSAHelper jwtHelper;

    @Autowired
    private UserLoginDAO loginDAO;

    /**
     * 商圈用户登录
     *
     * @param username
     * @param pwd
     */
    @Override

    public Map login(String username, String pwd) {
        A.checkParam(username == null, "用户名不能为空");
        A.checkParam(pwd == null, "密码不能为空");
        A.check(!RegexUtil.checkNumber(username),"手机号格式不正确");
        /**根据用户名查询商户信息*/
        OfflineUserEntity userEntity = userDAO.queryByMobile(username);
        A.check(userEntity == null, "用户名错误");
        A.check(!PasswordUtil.check(pwd, userEntity.getPassword(), userEntity.getSalt()), "用户名或密码错误，请重新输入");
        A.check("1".equals(userEntity.getStatus()), "该账号已禁用,请联系管理员启用后再登录");
        String userId= userEntity.getId();

        long start = System.currentTimeMillis();
        String accessToken = jwtHelper.generate(AccessToken.get(JWTConstant.Type.USER, userId));
        logger.info("JWT RSA generate user time " + (System.currentTimeMillis() - start) + "ms");

        if(this.loginDAO.updateLogin(userId, accessToken, jwtHelper.getExpire()) == 0) {
            this.loginDAO.add(IDUtil.getUUID(),userId, accessToken, jwtHelper.getExpire());
        }
        logger.debug("登录成功：username:{}, access-token:{}", username, accessToken);
        Map map = new HashMap<>();
        map.put("username",userEntity.getUsername());
        map.put("accessToken",accessToken);
        return map;
    }
}
