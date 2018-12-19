package cn.kt.mall.common.sms.mapper;

import cn.kt.mall.common.sms.entity.SmsCaptcha;
import cn.kt.mall.common.sms.entity.SmsRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 短信Mapper
 * Created by Administrator on 2017/6/18.
 */
@Mapper
public interface SmsDAO {

    /**
     * 根据验证码和手机号查询记录数量(30分钟内)
     *
     * @param mobile 手机号
     * @param code 验证码
     * @return 更新结果
     */
    int checkMobileCaptcha(@Param("mobile") String mobile, @Param("code") String code);

    /**
     * 新增短信验证码记录
     *
     * @param smsCaptcha 短信验证码
     */
    void addSmsCaptcha(SmsCaptcha smsCaptcha);

    /**
     * 更新用户的短信验证码(创建新的短信验证码)
     *
     * @param smsCaptcha 短信验证码
     * @return 更新结果
     */
    int updateSmsCaptcha(SmsCaptcha smsCaptcha);

    /**
     * 将手机对应的验证码设为无效
     *
     * @param mobile 手机号
     */
    int updateSmsCaptchaDisable(@Param("mobile") String mobile);

    /**
     * 通过手机号获取1分钟内可用的短信验证码数量
     *
     * @param mobile 手机号
     */
    int getAvailableCaptchaCntByMobile(@Param("mobile") String mobile);

    /**
     * 新增短信发送记录
     * @param entity 短信记录
     */
    void addSmsRecord(SmsRecordEntity entity);

    int getAllCount(@Param("userId") String userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<SmsRecordEntity> getManagementList(@Param("userId") String userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("offset") int offset, @Param("pageSize") int pageSize);
}
