package cn.kt.mall.web.shop.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.exception.ForbiddenException;
import cn.kt.mall.common.exception.UnauthorizedException;
import cn.kt.mall.common.jwt.AccessToken;
import cn.kt.mall.common.jwt.JWTConstant;
import cn.kt.mall.common.jwt.JWTRSAHelper;
import cn.kt.mall.common.jwt.SubjectInstance;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.dao.UserLoginDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.user.entity.UserLoginEntity;
import cn.kt.mall.common.user.model.CurrentShop;
import cn.kt.mall.common.user.model.CurrentUser;
import cn.kt.mall.common.user.vo.LoginInfoVO;
import cn.kt.mall.common.user.vo.LoginVO;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.util.PasswordUtil;
import cn.kt.mall.common.util.RegexUtil;
import cn.kt.mall.web.shop.service.ShopLoginService;
import cn.kt.mall.shop.shop.constant.Constants;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;
import cn.kt.mall.web.shop.vo.ShopInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 线上商户登录
 */
@Service
public class ShopLoginServiceImpl implements ShopLoginService {

    private static final Logger logger = LoggerFactory.getLogger(ShopLoginServiceImpl.class);

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserLoginDAO loginDAO;
    @Autowired
    private JWTRSAHelper jwtHelper;
    @Autowired
    private ShopDAO shopDAO;

    @Override
    public ShopInfoVO shopLogin(LoginVO loginVO) {
        String username = loginVO.getUsername();
        String password = loginVO.getPassword();
        A.checkParam(username == null, "用户名不能为空");
        A.checkParam(password == null, "密码不能为空");

        username = username.trim();
        password = password.trim();

        UserEntity entity;
        if (RegexUtil.checkNumber(username)) {
            entity = this.userDAO.getByMobile(username);
        } else {
            entity = this.userDAO.getByUsername(username);
        }

        A.check(entity == null, "用户名错误");
        A.check(!PasswordUtil.check(password, entity.getPassword(), entity.getSalt()), "用户名或密码错误，请重新输入");
        A.check("1".equals(entity.getStatus()), "你暂时不能登录");

        String userId = entity.getId();

        List<ShopEntity> shopList = shopDAO.getMyOnLineShopEntity(userId);
        A.check(shopList == null || CollectionUtils.isEmpty(shopList), "您没有开通线上商城");
        A.check(!shopList.get(0).getStatus().equals(Constants.ShopStatus.SHOP_PASS), "您的店铺已经被关闭");

        long start = System.currentTimeMillis();
        String accessToken = jwtHelper.generate(AccessToken.get(JWTConstant.Type.USER, userId));
        logger.info("JWT RSA generate user time " + (System.currentTimeMillis() - start) + "ms");

        if (this.loginDAO.updateLogin(userId, accessToken, jwtHelper.getExpire()) == 0) {
            this.loginDAO.add(IDUtil.getUUID(), userId, accessToken, jwtHelper.getExpire());
        }

        logger.debug("登录成功：username:{}, access-token:{}", username, accessToken);
        ShopInfoVO shopInfoVO = new ShopInfoVO();
        shopInfoVO.setBase(new LoginInfoVO(entity, accessToken));
        shopInfoVO.setShopData(shopList.get(0));
        return shopInfoVO;
    }

    @Override
    public void logout(String accessToken) {
        AccessToken tokenInfo;
        try {
            tokenInfo = jwtHelper.parserToken(accessToken);
        } catch (Exception e) {
            return;
        }
        if (tokenInfo == null) {
            return;
        }
        this.loginDAO.updateLogout(tokenInfo.getId());
    }

    @Override
    public SubjectInstance verify(String jwt) throws UnauthorizedException {
        A.checkAuth(StringUtils.isEmpty(jwt), "非法的客户端请求");
        long start = System.currentTimeMillis();
        AccessToken accessToken = this.jwtHelper.parserToken(jwt);
        logger.info("JWT RSA parserToken user time " + (System.currentTimeMillis() - start) + "ms");
        UserLoginEntity entity = this.loginDAO.getByUserId(accessToken.getId());

        A.checkAuth(entity == null, "用户登录状态异常");
        A.checkAuth(!"0".equals(entity.getStatus()) || !jwt.equals(entity.getToken()) || new Date().compareTo(entity.getExpireTime()) >= 0, "用户登录状态过期，请重新登录");

        UserEntity userEntity = this.userDAO.getById(entity.getUserId());
        A.checkAuth(userEntity == null, "用户不存在");

        return getCurrentUser(jwt, userEntity);
    }

    @Override
    public CurrentShop verifyShop(String userId) throws UnauthorizedException {
        A.checkAuth(StringUtils.isEmpty(userId), "非法的客户端请求");
        List<ShopEntity> shopList = shopDAO.getMyOnLineShopEntity(userId);
        A.checkAuth(shopList == null || shopList.size() > 1, "没有开通线上商城");
        A.checkAuth(!shopList.get(0).getStatus().equals(Constants.ShopStatus.SHOP_PASS), "申请开通的线上商城正在审核中");
        CurrentShop currentShop = new CurrentShop();
        currentShop.setId(shopList.get(0).getId());
        return currentShop;
    }

    private CurrentUser getCurrentUser(String accessToken, UserEntity entity) {
        CurrentUser current = new CurrentUser();
        BeanUtils.copyProperties(entity, current);
        current.setUsername(entity.getMobile());
        current.setToken(accessToken);
        return current;
    }

    @Override
    public void check(String userId, String[] roles, String[] privileges) throws ForbiddenException {

    }
}
