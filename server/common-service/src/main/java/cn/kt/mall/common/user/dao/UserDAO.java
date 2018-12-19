package cn.kt.mall.common.user.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.kt.mall.common.user.entity.BankCardEntity;
import cn.kt.mall.common.user.entity.CertificationEntity;
import cn.kt.mall.common.user.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import cn.kt.mall.common.user.entity.UserEntity;

/**
 * 用户数据操作类
 * Created by jerry on 2017/12/21.
 */
@Mapper
public interface UserDAO {
    int updateUserLevel(@Param("id") String id, @Param("level") String level);

    int updateUserTrueName(@Param("id") String id, @Param("trueName") String trueNam);

    int updateUserCertificationStatus(@Param("id") String id, @Param("status") String status);

    void add(UserEntity entity);

    void addBankMessage(BankCardEntity bankCardEntity);

    void updateBankMessage(BankCardEntity bankCardEntity);

    void addCertificationMessage(CertificationEntity certificationEntity);

    void updatCertificationMessage(CertificationEntity certificationEntity);

    void addTransactionPassword(@Param("id") String id, @Param("transactionPassword") String transactionPassword, @Param("transcationSalt") String transcationSalt);

    void editTransactionPassword(@Param("id") String id, @Param("transactionPassword") String transactionPassword, @Param("transcationSalt") String transcationSalt);

    void updatePassword(@Param("id") String id, @Param("password") String password, @Param("salt") String salt);

    UserEntity getById(@Param("id") String id);

    UserEntity getByUserId(@Param("id") String id);

    CertificationEntity getCertificationMessage(@Param("userId") String userId);

    UserConsumeVO getUserConsume(@Param("userId") String userId);

    int insertUserConsume(UserConsumeVO userConsumeVO);

    int updateUserConsume(UserConsumeVO userConsumeVO);

    /**
     * 获取好友信息
     *
     * @param id
     * @param userId
     * @return
     */
    UserEntity queryUserById(@Param("id") String id, @Param("userId") String userId);

    UserEntity getByUsername(@Param("username") String username);

    UserEntity getByMobile(@Param("mobile") String mobile);

    UserEntity getByMobileAndLevel(@Param("mobile") String mobile);

    void updatePersonal(EditVO editVO);

    void updateTransactionPassword(@Param("userId") String userId);

    void updateReferrer(@Param("id") String id, @Param("referrer") String referrer);

    long getUserCount(@Param("username") String key);

    List<UserEntity> getUserList(@Param("username") String key, @Param("offset") int offset, @Param("pageSize") int pageSize);

    int getSlaveAllCount(@Param("currentId") String currentId, @Param("mobile") String mobile);

    List<UserEntity> getSlavePage(@Param("currentId") String currentId, @Param("mobile") String mobile, @Param("offset") int offset, @Param("pageSize") int pageSize);

    List<UserEntity> getSlaveList(@Param("currentId") String currentId);

    int editUserLevel(@Param("userId") String userId, @Param("level") String level);

    int editMobile(@Param("userId") String userId, @Param("nationalCode") String nationalCode, @Param("mobile") String mobile);

    List<UserEntity> getByIds(@Param("userIds") List<String> userIds);

    /**
     * 根据用户id查询用户昵称
     *
     * @param userIds
     * @return
     */
    List<String> getNicks(@Param("userIds") String[] userIds);

    /**
     * 查询符合筛选条件的会员数量
     *
     * @param userMobile          用户手机号
     * @param referrerMobile      推荐人手机号
     * @param level               用户等级
     * @param shopType            店铺类型--2:零售店  3:批发店
     * @param shopMobile          所属商铺电话
     * @param status              用户状态,0已启用，1已禁用
     * @param certificationStatus 实名审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @return
     */
    int manageUserAndShopCount(@Param("userMobile") String userMobile,
                               @Param("referrerMobile") String referrerMobile,
                               @Param("level") String level,
                               @Param("shopType") String shopType,
                               @Param("shopMobile") String shopMobile,
                               @Param("status") String status,
                               @Param("certificationStatus") String certificationStatus);

    /**
     * 会员管理分页查询
     *
     * @param userMobile          用户手机号
     * @param referrerMobile      推荐人手机号
     * @param level               用户等级
     * @param shopType            店铺类型--2:零售店  3:批发店
     * @param shopMobile          所属商铺电话
     * @param status              用户状态,0已启用，1已禁用
     * @param certificationStatus 实名审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @param offset
     * @param pageSize
     * @return
     */
    List<UserManageRespVO> manageUserAndShop(@Param("userMobile") String userMobile,
                                             @Param("referrerMobile") String referrerMobile,
                                             @Param("level") String level,
                                             @Param("shopType") String shopType,
                                             @Param("shopMobile") String shopMobile,
                                             @Param("status") String status,
                                             @Param("certificationStatus") String certificationStatus,
                                             @Param("offset") Integer offset,
                                             @Param("pageSize") Integer pageSize);

    /**
     * 新查询符合筛选条件的会员数量
     *
     * @param userMobile          用户手机号
     * @param status              用户状态,0已启用，1已禁用
     * @param certificationStatus 实名审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @return
     */
    int queryUserListCount(@Param("userMobile") String userMobile,
                           @Param("status") String status,
                           @Param("certificationStatus") String certificationStatus);

    /**
     * 新查询符合筛选条件的会员列表
     *
     * @param userMobile          用户手机号
     * @param status              用户状态,0已启用，1已禁用
     * @param certificationStatus 实名审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @param offset
     * @param pageSize
     * @return
     */
    List<UserEntity> queryUserList(@Param("userMobile") String userMobile,
                                   @Param("status") String status,
                                   @Param("certificationStatus") String certificationStatus,
                                   @Param("offset") Integer offset,
                                   @Param("pageSize") Integer pageSize);

    /**
     * 修改用户状态
     *
     * @param userIds 用户id集合
     * @param status  用户状态,0已启用，1已禁用
     * @return
     */
    int editUserStatus(@Param("userIds") List<String> userIds,
                       @Param("status") String status);

    /**
     * 查询会员等级
     *
     * @return
     */
    List<String> queryUserLevel();

    /**
     * 根据用户id查询会员等级
     *
     * @return
     */
    String queryUserLevelByUserID(@Param("userId") String userId);

    int countAllUser();

    List<UserCountVO> registerCount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<UserEntity> getUserByPid(@Param("pid") String pid);

    List<UserEntity> getUserByReferee(@Param("referee") String referee);

    List<UserEntity> getSuperiorList();

    List<UserEntity> getByIdList(@Param("userIds") List<String> userIds);

    //统计下级会员数量 －－ 包括所有级别的层级
    Integer countReferee(@Param("userId") String userId);

    //获取该会员推荐的下级用户列表
    List<UserEntity> getRefereeUserByUserId(@Param("userId") String userId, RowBounds rowBounds);


    //根据PID查询 一下用户的shopId
    List<String> getUserShopIdByPid(@Param("pid") String pid);

    /**
     * 查询用户可用信用金
     *
     * @param userId
     * @return
     */
    BigDecimal queryAvailableBalance(@Param("userId") String userId);

    /**
     * 修改用户实名认证状态
     *
     * @param userIds             用户id集合
     * @param certificationStatus 实名认证状态，0未审核，1已通过，2已拒绝，3未实名
     * @return
     */
    int updateCertificationStatus(@Param("userIds") List<String> userIds,
                                  @Param("certificationStatus") String certificationStatus);

    UserEntity getUserBaseInfo(@Param("id") String id);
    /***********以下为定时任务更新团队总人数相关代码****begin********/
    /**
     * 查询userId列表
     *
     * @return
     */
    List<UserEntity> getUserids(@Param("begin") int begin, @Param("end") int end);

    /**
     * 更新用户团队总人数
     *
     * @param userList
     * @return
     */
    int updateUserTeamCountByIds(@Param("userList") List<UserEntity> userList);

    /***********以上为定时任务更新团队总人数相关代码****end********/

    List<UserEntity> getUserByLevel();

    /**
     * 根据用户Id与pid查询用户信息
     *
     * @param buyUserId
     * @param userId
     * @return
     */
    UserEntity getUserByIdAndPid(@Param("buyUserId") String buyUserId, @Param("userId") String userId);

    /**
     * 根据用户id查询团队增长人数
     *
     * @param userId
     * @return
     */
    int getCountByPid(@Param("userId") String userId);

    /**
     * 根据用户id作为pid查询用户id列表
     *
     * @param pid
     * @return
     */
    int getUserByPidStr(@Param("pid") String pid, @Param("level") String level);

    /**
     * 根据等级查询用户列表
     *
     * @param level
     * @return
     */
    List<UserEntity> getUserByLevelNum(@Param("level") int level);

    /**
     * 统计中心主任下直属会员
     *
     * @param pid
     * @return
     */
    int getlevelOneCountByPid(@Param("pid") String pid, @Param("level") String level);

    /**
     * 查询用户名称与手机号
     *
     * @param userId
     * @return
     */
    UserEntity getTrueNameById(@Param("userId") String userId);

    List<String> getShopIdByUser();

    List<UserEntity> getUserIdAndTrueName();

    /**
     * 获取到账账户用户名
     * @return
     */
    List<UserEntity> getUserIdAndTrueNameArrive();

    /**
     * 获取资金（充值扣除）被操作人相关信息
     * @return
     */
    List<UserEntity> getRechargeUserByUserId();
    /**
     * 获取资金（充值扣除）操作人相关信息
     * @return
     */
    List<UserEntity> getUserByUserId();

    /**
     * 获取优惠券转出用户信息
     * @return
     */
    List<UserEntity> getTransferOutByUserId();

    /**
     * 获取优惠券转入用户信息
     * @return
     */
    List<UserEntity> getTransferInByUserId();
}
