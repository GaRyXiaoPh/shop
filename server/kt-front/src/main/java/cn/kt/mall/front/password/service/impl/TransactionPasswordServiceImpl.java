package cn.kt.mall.front.password.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.sms.service.SmsService;
import cn.kt.mall.common.user.model.CurrentUser;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.util.PasswordUtil;
import cn.kt.mall.front.password.dao.TransactionPasswordDAO;
import cn.kt.mall.front.password.entity.TransactionPasswordEntity;
import cn.kt.mall.front.password.service.TransactionPasswordService;
import cn.kt.mall.front.password.vo.SetPasswordVO;
import cn.kt.mall.front.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交易密码服务实现类
 * Created by wqt on 2018/1/29.
 */
@Service
public class TransactionPasswordServiceImpl implements TransactionPasswordService {
    private Logger logger = LoggerFactory.getLogger(TransactionPasswordServiceImpl.class);

    private TransactionPasswordDAO transactionPasswordDAO;
    private SmsService smsService;
    private UserService userService;

    @Autowired
    public TransactionPasswordServiceImpl(UserService userService,
                                          SmsService smsService,
                                          TransactionPasswordDAO transactionPasswordDAO) {
        this.userService = userService;
        this.smsService = smsService;
        this.transactionPasswordDAO = transactionPasswordDAO;
    }

    @Override
    public String hasSettings(String userId) {
        return this.transactionPasswordDAO.getByUserId(userId) == null ? "0" : "1";
    }

    @Override
    public void setPassword(CurrentUser currentUser, SetPasswordVO password) {
        A.checkParam(password.getPassword() == null, "密码不能为空");
        A.checkParam(password.getCaptcha() == null, "验证码不能为空");
        A.check(!this.smsService.checkCaptcha(currentUser.getUsername(), password.getCaptcha()), "验证码错误");

        A.check("1".equals(this.hasSettings(currentUser.getId())), "你已经设置过交易密码");

        String[] pwd = PasswordUtil.getEncryptPassword(password.getPassword());

        this.transactionPasswordDAO.add(new TransactionPasswordEntity(IDUtil.getUUID(), currentUser.getId(), pwd[0], pwd[1]));
        this.userService.updateTransactionPassword(currentUser.getId());
    }

    @Override
    public void updatePassword(CurrentUser currentUser, SetPasswordVO passwordVO) {
        A.checkParam(passwordVO.getCaptcha() == null, "验证码不能为空");
        A.checkParam(passwordVO.getPassword() == null, "新密码不能为空");

        A.check(!this.smsService.checkCaptcha(currentUser.getUsername(), passwordVO.getCaptcha()), "验证码错误");

        TransactionPasswordEntity entity = this.transactionPasswordDAO.getByUserId(currentUser.getId());
        A.check(entity == null, "您还没有设置过交易密码，请先设置交易密码");

        String[] pwd = PasswordUtil.getEncryptPassword(passwordVO.getPassword());
        this.transactionPasswordDAO.update(entity.getId(), pwd[0], pwd[1]);
    }

    public void check(String userId, String password) {
        A.checkParam(password == null || password.length() == 0, "交易密码不能为空");

        TransactionPasswordEntity entity = this.transactionPasswordDAO.getByUserId(userId);
        A.check(entity == null, "您还没有设置过交易密码，请先设置交易密码");

        A.check(!PasswordUtil.check(password, entity.getPassword(), entity.getSalt()), "交易密码不正确");

        logger.info("支付授权信息验证成功[password:{}, operatorId:{}]", password, userId);
    }
}
