package cn.kt.mall.front.user.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kt.mall.common.certification.CertificationApi;
import cn.kt.mall.common.user.entity.CertificationEntity;
import cn.kt.mall.common.user.vo.*;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;
import io.shardingjdbc.core.api.HintManager;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.exception.ForbiddenException;
import cn.kt.mall.common.exception.UnauthorizedException;
import cn.kt.mall.common.jwt.AccessToken;
import cn.kt.mall.common.jwt.JWTConstant;
import cn.kt.mall.common.jwt.JWTRSAHelper;
import cn.kt.mall.common.jwt.SubjectInstance;
import cn.kt.mall.common.sms.service.SmsService;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.dao.UserLoginDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.user.entity.BankCardEntity;
import cn.kt.mall.common.user.entity.UserLoginEntity;
import cn.kt.mall.common.user.model.CurrentUser;
import cn.kt.mall.common.user.model.UserInfo;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.util.PasswordUtil;
import cn.kt.mall.common.util.RegexUtil;
import cn.kt.mall.front.user.service.UserService;


/**
 * 用户业务实现类
 * Created by jerry on 2017/12/21.
 */
@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String url = "https://v.apistore.cn/api/a1";
    private static final String APPKEY = "77ea2233ad9621b6ae2aa450166ad5a2";


    private SmsService smsService;

    private UserDAO userDAO;

    private UserLoginDAO loginDAO;

    private JWTRSAHelper jwtHelper;

    private ShopDAO shopDAO;

    @Autowired
    private UserAssetService userAssetService;


    @Autowired
    public UserServiceImpl(SmsService smsService, UserDAO userDAO, UserLoginDAO loginDAO, ShopDAO shopDAO, JWTRSAHelper jwtHelper) {
        this.smsService = smsService;
        this.userDAO = userDAO;
        this.loginDAO = loginDAO;
        this.jwtHelper = jwtHelper;
        this.shopDAO = shopDAO;
    }

    @Transactional
    @Override
    public void register(RegisterVO registerVO) {
        A.checkParam(registerVO.getMobile() == null, "手机号不能为空");
        //A.checkParam(registerVO.getNationalCode() == null, "地区编码不能为空");
        A.check(registerVO.getMobile() == null || registerVO.getMobile().trim().length() == 0, "手机号输入不正确");
        A.checkParam(registerVO.getPassword() == null, "密码不能为空");

        A.checkParam(registerVO.getCaptcha() == null, "验证码不能为空");
        A.check(!this.smsService.checkCaptcha(registerVO.getMobile(), registerVO.getCaptcha()), "验证码错误");
        //隶属上级id
        //A.check(registerVO.getPid().trim() == null || registerVO.getPid().trim().equals("") , "隶属上级不能为空");
        String pid = "";
        String standNo = "";
        if(registerVO.getPid() != null &&  !registerVO.getPid().equals("")){
            A.check(registerVO.getPid().trim().equals(registerVO.getMobile().trim()), "隶属上级不能为自己");
            A.check(this.userDAO.getByMobileAndLevel(registerVO.getPid().trim()) == null, "隶属上级不存在或不能同为会员");
            pid = this.userDAO.getByMobileAndLevel(registerVO.getPid().trim()).getId();
            standNo = this.userDAO.getByMobile(registerVO.getPid()).getStandNo();//站点编号
        }

        //邀请码不为空则判断格式
        String referrerId = "";
        if(registerVO.getReferrer() != null &&  !registerVO.getReferrer().equals("")){

            A.check(registerVO.getReferrer().trim().length() == 0, "邀请码输入不正确");
            A.check(registerVO.getReferrer().trim().equals(registerVO.getMobile().trim()), "邀请人不能为自己");
            A.check(this.userDAO.getByMobile(registerVO.getReferrer().trim()) == null, "邀请人不存在请重新填写");
            referrerId = this.userDAO.getByMobile(registerVO.getReferrer().trim()).getId();
        }
        UserEntity a = this.userDAO.getByMobile(registerVO.getMobile());
        A.check(this.userDAO.getByMobile(registerVO.getMobile()) != null, "此手机号已经注册");
        if (registerVO.getNick()==null ||registerVO.getNick().equals("")){
            registerVO.setNick(registerVO.getMobile());
        }
        registerVO.setStandNo(standNo);
        registerVO.setUsername(registerVO.getMobile());
        registerVO.setReferrer(referrerId);
        registerVO.setPid(pid);
        UserEntity userEntity = registerVO.getUserEntity();
        userEntity.setXin(BigDecimal.ZERO);
        // 注册成功后，初始化我的资产表
        userAssetService.addInternalAsset(userEntity.getId());
        this.userDAO.add(userEntity);
    }

    @Override
    public String usernameIsValid(String username) {
        A.checkParam(username == null, "用户名不能为空");
        username = username.trim();
        A.checkParam(!RegexUtil.checkUsername(username), "用户名必须为6-16位字母和数字的组合");
        return this.userDAO.getByUsername(username) != null ? "0" : "1";
    }

    @Override
    public void forget(ForgetVO forgetVO) {
        A.check(forgetVO.getMobile() == null || forgetVO.getMobile().trim().length() == 0, "手机号输入不正确");
        A.checkParam(forgetVO.getPassword() == null, "密码不能为空");
        A.checkParam(forgetVO.getCaptcha() == null, "验证码不能为空");
        A.check(!this.smsService.checkCaptcha(forgetVO.getMobile(), forgetVO.getCaptcha()), "验证码错误");

        UserEntity user = this.userDAO.getByMobile(forgetVO.getMobile());
        A.check(user == null, "此手机号未注册");
        String[] pwd = PasswordUtil.getEncryptPassword(forgetVO.getPassword());
        this.userDAO.updatePassword(user.getId(), pwd[0], pwd[1]);
    }

    @Override
    public void updatePassword(CurrentUser currentUser, PasswordVO passwordVO) {
        A.checkParam(passwordVO.getOldPassword() == null, "原密码不能为空");
        A.checkParam(passwordVO.getPassword() == null, "新密码不能为空");

        UserEntity entity = this.userDAO.getById(currentUser.getId());

        A.check(!PasswordUtil.check(passwordVO.getOldPassword(), entity.getPassword(), entity.getSalt()), "原密码输入错误");

        String[] pwd = PasswordUtil.getEncryptPassword(passwordVO.getPassword());
        this.userDAO.updatePassword(entity.getId(), pwd[0], pwd[1]);
    }
    @Override
    public void resetPassword(String mobile,String passwordVO) {
        String[] pwd = PasswordUtil.getEncryptPassword(passwordVO);
        UserEntity entity = this.userDAO.getByMobile(mobile);
        this.userDAO.updatePassword(entity.getId(), pwd[0], pwd[1]);
    }

    @Override
    public LoginInfoVO login(LoginVO loginVO) {
        String username = loginVO.getUsername();
        String password = loginVO.getPassword();
        A.checkParam(username == null, "用户名不能为空");
        A.checkParam(password == null, "密码不能为空");

        username = username.trim();
        password = password.trim();

        UserEntity entity = null;
        if(RegexUtil.checkNumber(username)) {
            entity = this.userDAO.getByMobile(username);
        } else {
            entity = this.userDAO.getByUsername(username);
        }

        A.check(entity == null, "用户名错误");
        A.check(!PasswordUtil.check(password, entity.getPassword(), entity.getSalt()), "用户名或密码错误，请重新输入");
        A.check("1".equals(entity.getStatus()), "你暂时不能登录已被禁用");

        String userId= entity.getId();

        long start = System.currentTimeMillis();
        String accessToken = jwtHelper.generate(AccessToken.get(JWTConstant.Type.USER, userId));
        logger.info("JWT RSA generate user time " + (System.currentTimeMillis() - start) + "ms");

        if(this.loginDAO.updateLogin(userId, accessToken, jwtHelper.getExpire()) == 0) {
            this.loginDAO.add(IDUtil.getUUID(),userId, accessToken, jwtHelper.getExpire());
        }

        logger.debug("登录成功：username:{}, access-token:{}", username, accessToken);

        return new LoginInfoVO(entity, accessToken);
    }

    @Override
    public boolean check(String accessToken) {
        long start = System.currentTimeMillis();
        AccessToken tokenInfo = this.jwtHelper.parserToken(accessToken);
        logger.info("JWT RSA parserToken user time " + (System.currentTimeMillis() - start) + "ms");
        if(tokenInfo == null) {
            return false;
        }
        UserLoginEntity entity = this.loginDAO.getByUserId(tokenInfo.getId());

        if(!"0".equals(entity.getStatus()) || !accessToken.equals(entity.getToken())) {
            return false;
        }

        if(new Date().compareTo(entity.getExpireTime()) >= 0) {
            return false;
        }

        return true;
    }

    @Override
    public void logout(String accessToken) {
        AccessToken tokenInfo = null;
        try {
            tokenInfo = jwtHelper.parserToken(accessToken);
        } catch (Exception e) {
            return;
        }
        if(tokenInfo == null) {
            return;
        }
        this.loginDAO.updateLogout(tokenInfo.getId());
    }

    @Override
    public CurrentUser getCurrent(String accessToken) {
        long start = System.currentTimeMillis();
        AccessToken tokenInfo = jwtHelper.parserToken(accessToken);
        logger.info("JWT RSA parserToken user time " + (System.currentTimeMillis() - start) + "ms");
        UserEntity entity = this.userDAO.getById(tokenInfo.getId());
        if(entity == null) {
            return null;
        }
        return getCurrentUser(accessToken, entity);
    }

    private CurrentUser getCurrentUser(String accessToken, UserEntity entity) {
        CurrentUser current = new CurrentUser();
        BeanUtils.copyProperties(entity, current);
        current.setUsername(entity.getMobile());
        current.setToken(accessToken);
        return current;
    }

    @Override
    public UserInfo getUserById(String id) {
        UserEntity entity = this.userDAO.getById(id);
        if(entity == null) {
            return null;
        }
        UserInfo user = new UserInfo();
        BeanUtils.copyProperties(entity, user);

        UserEntity refEntity = this.userDAO.getById(entity.getReferrer());
        if (refEntity!=null){
            user.setReferrerNick(refEntity.getNick());
        }
        return user;
    }
    public UserInfo getUserByUserId(String id) {
        UserEntity entity = this.userDAO.getByUserId(id);
        if(entity == null) {
            return null;
        }
        UserInfo user = new UserInfo();
        BeanUtils.copyProperties(entity, user);

        return user;
    }

    @Override
    public UserInfo queryUserById(String userId) {
        //UserEntity entity = this.userDAO.queryUserById(id,userId);
        UserEntity entity = this.userDAO.getById(userId);
        if(entity == null) {
            return null;
        }
        if(entity.getXin().equals("") || entity.getXin() == null){
            entity.setXin(BigDecimal.ZERO);
        }
        UserInfo user = new UserInfo();
        if(entity.getCertificationStatus() == null){
            entity.setCertificationStatus("3");
        }
        BeanUtils.copyProperties(entity, user);

        //UserEntity refEntity = this.userDAO.queryUserById(entity.getReferrer(),userId);
        UserEntity refEntity = this.userDAO.getById(entity.getReferrer());
        if (refEntity!=null){
            user.setReferrerNick(refEntity.getNick());
        }
        return user;
    }

    @Override
    public UserInfo getUserByUsername(String username) {
        UserEntity entity = this.userDAO.getByMobile(username);
        if(entity == null) {
            return null;
        }

        UserInfo user = new UserInfo();
        BeanUtils.copyProperties(entity, user);

        UserEntity refEntity = this.userDAO.getById(entity.getReferrer());
        if (refEntity!=null){
            user.setReferrerNick(refEntity.getNick());
        }
        return user;
    }

    @Override
    public void editPersonalInfo(EditVO editVO) {
        A.checkParam(editVO.getNick() == null, "昵称不能为空");
        this.userDAO.updatePersonal(editVO);
    }

    @Override
    public void updateTransactionPassword(String userId) {
        this.userDAO.updateTransactionPassword(userId);
    }

    @Override
    public void changeMobileCaptcha(CurrentUser currentUser, String nationalCode, String mobile) {
        A.check(mobile == null || mobile.trim().length() == 0, "手机号输入不正确");
        A.check(nationalCode == null, "国际地区代码不能为空");
        A.check(currentUser.getUsername() != null && currentUser.getUsername().equals(mobile), "新手机号不能与原手机号相同");

        UserEntity entity = this.userDAO.getByMobile(mobile);
        A.check(entity != null, "此手机号已被使用");

        this.smsService.sendSmsCaptcha(currentUser.getId(), nationalCode, mobile, "MOBILE_EDIT");
    }

    @Override
    public void changeMobile(CurrentUser currentUser, ChangeMobileVO changeMobileVO) {
        String mobile = changeMobileVO.getMobile();
        String nationalCode = changeMobileVO.getNationalCode();
        String captcha = changeMobileVO.getCaptcha();

        A.checkParam(captcha == null, "验证码不能为空");
        A.check(mobile == null || mobile.trim().length() == 0, "手机号输入不正确");
        A.check(nationalCode == null, "国际地区代码不能为空");
        A.check(currentUser.getUsername().equals(mobile), "新手机号不能与原手机号相同");

        A.check(!this.smsService.checkCaptcha(mobile, captcha), "验证码错误");

        UserEntity entity = this.userDAO.getByMobile(mobile);
        A.check(entity != null, "此手机号已被使用");

        this.userDAO.editMobile(currentUser.getId(), nationalCode, mobile);
    }

    @Override
    public void addBankMessage(BankCardVO bankCardVO) {

        String trueName =bankCardVO.getTrueName();
        String  bankCard = bankCardVO.getBankCard();
        String bankName = bankCardVO.getBankName();
        String openBank = bankCardVO.getOpenBank();
        A.check(trueName == null || trueName.equals(""), "真实姓名不能为空");
        A.check(bankCard == null || bankCard.equals(""), "银行卡号不能为空");
        A.check(bankName == null || bankName.equals(""), "银行名称不能为空");
        A.check(openBank == null || openBank.equals(""), "开户行不能为空");
        BankCardEntity bankCardEntity = bankCardVO.getBankCardEntity();
        this.userDAO.addBankMessage(bankCardEntity);
    }
    @Override
    public void updateBankMessage(BankCardVO bankCardVO){
        String trueName =bankCardVO.getTrueName();
        String  bankCard = bankCardVO.getBankCard();
        String bankName = bankCardVO.getBankName();
        String openBank = bankCardVO.getOpenBank();
        A.check(trueName == null || trueName.equals(""), "真实姓名不能为空");
        A.check(bankCard == null || bankCard.equals(""), "银行卡号不能为空");
        A.check(bankName == null || bankName.equals(""), "银行名称不能为空");
        A.check(openBank == null || openBank.equals(""), "开户行不能为空");
        BankCardEntity bankCardEntity = bankCardVO.getBankCardEntity();
        this.userDAO.updateBankMessage(bankCardEntity);
    }
    @Override
    public void addCertificationMessage(CertificationVO certificationVO) {

        String trueName =certificationVO.getTrueName();
        String cardId = certificationVO.getCardId();
        String faceImg =certificationVO.getFaceImg();
        String conImg = certificationVO.getConImg();
        String holdImg =certificationVO.getHoldImg();
        A.check(trueName == null || trueName.equals(""), "真实姓名不能为空");
        A.check(cardId == null || cardId.equals(""), "身份证不能为空");
        A.check(faceImg == null || faceImg.equals(""), "身份证正面照不能为空");
        A.check(conImg == null || conImg.equals(""), "身份证反面照不能为空");
        A.check(holdImg == null || holdImg.equals(""), "手持身份证照不能为空");
        if(certificationVO.getType().equals("0")){
            //请求参数
            Map params = new HashMap();
            params.put("key",APPKEY);
            params.put("cardNo",cardId);
            params.put("realName",trueName);
            params.put("information","");
            String result  = CertificationApi.doPost(url,CertificationApi.urlencode(params));
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code") != 0){//认证未通过
                A.check(object.get("reason").toString());
            }
        }

        CertificationEntity certificationEntity = certificationVO.getCertificationEntity();
        certificationEntity.setStatus("0");
        this.userDAO.addCertificationMessage(certificationEntity);

        this.userDAO.updateUserTrueName(certificationVO.getUserId(), trueName);
        this.userDAO.updateUserCertificationStatus(certificationVO.getUserId(),"0");

        List<ShopEntity> myShopList = shopDAO.getMyShopEntity(certificationVO.getUserId());
        if (!myShopList.isEmpty()) {
            ShopEntity entity = new ShopEntity();
            entity.setId(myShopList.get(0).getId());
            entity.setName(trueName);
            shopDAO.updateSelective(entity);
        }

    }

    @Override
    public void updatCertificationMessage(CertificationVO certificationVO){
        String trueName =certificationVO.getTrueName();
        String  cardId = certificationVO.getCardId();
        String faceImg =certificationVO.getFaceImg();
        String conImg = certificationVO.getConImg();
        String holdImg =certificationVO.getHoldImg();
        //A.check(trueName == null || trueName.equals(""), "真实姓名不能为空");
        //A.check(cardId == null || cardId.equals(""), "身份证不能为空");
        A.check(faceImg == null || faceImg.equals(""), "身份证正面照不能为空");
        A.check(conImg == null || conImg.equals(""), "身份证反面照不能为空");
        A.check(holdImg == null || holdImg.equals(""), "手持身份证照不能为空");
        CertificationEntity certificationEntity = certificationVO.getCertificationEntity();
        certificationEntity.setStatus("0");
        this.userDAO.updatCertificationMessage(certificationEntity);
        this.userDAO.updateUserCertificationStatus(certificationVO.getUserId(),"0");

        List<ShopEntity> myShopList = shopDAO.getMyShopEntity(certificationVO.getUserId());
        if (!myShopList.isEmpty()) {
            ShopEntity entity = new ShopEntity();
            entity.setId(myShopList.get(0).getId());
            entity.setName(trueName);
            shopDAO.updateSelective(entity);
        }
    }

    @Override
    public void addTransactionPassword(String userId,String transactionPassword) {
        A.check(transactionPassword == null || transactionPassword.equals(""), "交易密码不能为空");
        String[] pwd = PasswordUtil.getEncryptPassword(transactionPassword);
        this.userDAO.addTransactionPassword( userId,pwd[0],pwd[1]);
    }

    @Override
    public void editTransactionPassword(CurrentUser currentUser, PasswordVO passwordVO) {
        A.checkParam(passwordVO.getOldPassword() == null, "原密码不能为空");
        A.checkParam(passwordVO.getPassword() == null, "新密码不能为空");

        UserEntity entity = this.userDAO.getById(currentUser.getId());

        A.check(!PasswordUtil.check(passwordVO.getOldPassword(), entity.getTransactionPassword(), entity.getTranscationSalt()), "原密码输入错误");

        String[] pwd = PasswordUtil.getEncryptPassword(passwordVO.getPassword());
        this.userDAO.editTransactionPassword(entity.getId(), pwd[0], pwd[1]);
    }
    @Override
    public CertificationEntity getCertificationMessage(String userId) {
       return  userDAO.getCertificationMessage(userId);

    }
    @Override
    public SubjectInstance verify(String jwt) throws UnauthorizedException {
        A.checkAuth(StringUtils.isEmpty(jwt), "非法的客户端请求");
        long start = System.currentTimeMillis();
        AccessToken accessToken = this.jwtHelper.parserToken(jwt);
        logger.info("JWT RSA parserToken user time " + (System.currentTimeMillis() - start) + "ms");

        UserLoginEntity entity = getUserLoginFromMaster(accessToken.getId());

        A.checkAuth(entity == null, "用户登录状态异常");
        A.checkAuth(!"0".equals(entity.getStatus()) || !jwt.equals(entity.getToken()) || new Date().compareTo(entity.getExpireTime()) >= 0, "用户登录状态过期，请重新登录");

        UserEntity userEntity = this.userDAO.getById(entity.getUserId());
        A.checkAuth(userEntity == null, "用户未注册，请先注册");

        return getCurrentUser(jwt, userEntity);
    }
    @Override
    public UserConsumeVO getUserConsume(String userId){

        return userDAO.getUserConsume(userId);
    }
    //强制从主库读取登录信息
    private UserLoginEntity getUserLoginFromMaster(String userId) {
        HintManager hintManager = HintManager.getInstance();
        hintManager.setMasterRouteOnly();

        try {
            UserLoginEntity entity = this.loginDAO.getByUserId(userId);
            return entity;
        } finally {
           if (hintManager != null) {
               hintManager.close();
           }
        }
    }

    @Override
    public SubjectInstance verifyShop(String jwt) throws UnauthorizedException {
        return null;
    }

    @Override
    public void check(String userId, String[] roles, String[] privileges) throws ForbiddenException {

        //if(1 != 1) {
        //   throw new ForbiddenException();
        //}
    }
}
