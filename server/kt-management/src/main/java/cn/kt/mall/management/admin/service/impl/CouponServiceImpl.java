package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.common.UserRechargeConstant;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.vo.UserRechargeLogVO;
import cn.kt.mall.management.admin.dao.TbCouponDAO;
import cn.kt.mall.management.admin.entity.CouponEntity;
import cn.kt.mall.management.admin.service.CouponService;
import cn.kt.mall.management.admin.vo.CouponVO;
import cn.kt.mall.management.admin.vo.ExtractVO;
import cn.kt.mall.management.admin.vo.UserStatementVO;
import cn.kt.mall.shop.coupon.entity.UserCouponEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.good.vo.GoodRespVO;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private TbCouponDAO tbCouponDAO;
    @Autowired
    private UserDAO userDAO;
    private static Logger logger = Logger.getLogger(CouponServiceImpl.class);

    @Transactional
    public int deleteCouponByIds(List<String> idsList) {
        return tbCouponDAO.deleteCouponByIds(idsList);
    }

    @Transactional
    public int addCoupon(CouponEntity couponEntity) {
        return tbCouponDAO.addCoupon(couponEntity);
    }


    @Transactional
    public int updateCouponById(String couponName, BigDecimal ratio,
                                Integer couponType, String id,
                                Integer sendDays,
                                Integer isSend, Integer isDocking) {


        return tbCouponDAO.updateCouponById(couponName, ratio, couponType, id, sendDays,
                isSend, isDocking);
    }

    @Transactional
    public int updateCoupon4sendDays(String id, Integer sendDays) {
        return tbCouponDAO.updateCoupon4sendDays(id, sendDays);
    }


    public List<CouponEntity> getCouponsList() {

        return tbCouponDAO.getCouponsList();

    }


    public CouponEntity getCouponEntityById(String id) {
        return tbCouponDAO.getCouponEntityById(id);
    }

    public List<CouponEntity> getCouponEntityBySendDay() {
        List<CouponEntity> list = tbCouponDAO.getCouponEntityBySendDay();
        return list;
    }

    @Override
    public CommonPageVO<UserStatementVO> getReturnCoupon(String beginTime, String endTime, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<UserStatementVO> list = tbCouponDAO.getReturnCoupon(beginTime, endTime, rowBounds);
        for (UserStatementVO userStatementVO : list) {
            //获取推荐人信息
            UserEntity userEntity = userDAO.getById(userStatementVO.getUserId());
            if (userEntity != null) {
                userStatementVO.setReferrerName(userEntity.getTrueName());
                userStatementVO.setReferrerMobile(userEntity.getMobile());
            } else userStatementVO.setTrueName("未获取到推荐人用户信息");
            //获取消费用户信息
            UserEntity user = userDAO.getById(userStatementVO.getRelationUserId());
            if (user != null) {
                userStatementVO.setTrueName(user.getTrueName());
                userStatementVO.setMobile(user.getMobile());
            } else userStatementVO.setTrueName("未获取到消费用户信息");
            if (userStatementVO.getReturnRatio() != null) {
                userStatementVO.setReturnRatio(userStatementVO.getReturnRatio().multiply(new BigDecimal(100)));
            }
            if (userStatementVO.getTotalPrice() == null && userStatementVO.getTotalRaisePrice().compareTo(BigDecimal.ZERO) == 0) {
                userStatementVO.setTotalMoney("0");
            }
            if (userStatementVO.getTotalPrice() != null && userStatementVO.getTotalRaisePrice().compareTo(BigDecimal.ZERO) != 0) {
                userStatementVO.setTotalMoney(userStatementVO.getTotalPrice().setScale(2).toString() + "+" + userStatementVO.getTotalRaisePrice().setScale(2).toString());
            }
            if (userStatementVO.getTotalPrice() != null && userStatementVO.getTotalRaisePrice().compareTo(BigDecimal.ZERO) == 0) {
                userStatementVO.setTotalMoney(userStatementVO.getTotalPrice().setScale(2).toString());
            }
            if (userStatementVO.getTotalPrice() == null && userStatementVO.getTotalRaisePrice().compareTo(BigDecimal.ZERO) != 0) {
                userStatementVO.setTotalMoney(userStatementVO.getTotalRaisePrice().setScale(2).toString());
            }
        }
        PageInfo<UserStatementVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * 查询彩票、游戏积分提取记录
     *
     * @param beginTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public CommonPageVO<ExtractVO> getExtractList(String beginTime, String endTime, String mobile, String type, String status, String operateType, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<ExtractVO> list = tbCouponDAO.getExtractList(beginTime, endTime, mobile, type, status, operateType, rowBounds);
        //获取到账账户姓名
        /*Map<String, String> map = new HashMap<>();
        List<UserEntity> userLists = userDAO.getUserIdAndTrueNameArrive();
        for (UserEntity user : userLists) {
            map.put(user.getId(), user.getTrueName() + "," + user.getMobile());
        }*/
        for (ExtractVO vo : list) {
            //获取提取账户信息
            UserEntity userEntity = userDAO.getTrueNameById(vo.getUserId());
            if (userEntity != null) {
                vo.setTrueName(userEntity.getTrueName());
                vo.setMobile(userEntity.getMobile());
            }
            if(vo.getOperateType().equals("2")){
                UserEntity user = userDAO.getTrueNameById(vo.getArriveAccount());
                if(user != null){
                    if(user.getTrueName() != null){
                        vo.setArriveAccount( user.getTrueName());
                        if(user.getMobile() != null){
                            vo.setArriveAccount(user.getTrueName() + "," + user.getMobile());
                        }
                    }else {
                        if(user.getMobile() != null){
                            vo.setArriveAccount(user.getMobile());
                        }
                    }
                }
            }

        }
        PageInfo<ExtractVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    @Override
    public List<CouponEntity> getCouponListByIsDocking(String isDocking) {
        return tbCouponDAO.getCouponListByIsDocking(isDocking);
    }

    /**
     * 审核彩票、游戏积分提取记录
     */
    @Transactional
    public void passExtract(String ids, String status) {
        A.check(StringUtils.isBlank(ids) || StringUtils.isBlank(status), "获取参数失败");
        //校验该条信息是否存在
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            //根据主键ID查询提取记录是否存在
            ExtractVO vo = tbCouponDAO.getExtractById(idArray[i]);
            A.check(vo == null, "获取审核信息失败");
            /****************开始审核***********/
            /******************(0:未审核,1:通过,2:拒绝)***********/
            A.check(!vo.getStatus().equals("0"), "请检查选中列表,其中可能包含已经审核的信息行");
            ExtractVO extractVO = new ExtractVO();

            if (status.equals("2")) {
                //查询之前可用数量
                UserCouponEntity userCouponEntity = tbCouponDAO.getUserCouponByUserIdAndType(vo.getCouponId(), vo.getUserId());
                A.check(userCouponEntity == null, "获取用户优惠券信息失败");
                extractVO.setApplyNumBefore(userCouponEntity.getCouponNum());
                //提出申请时:可用减少，插入申请记录，审核：1，同意：无操作;2,拒绝：可用加回
                updateUserAsset(vo.getUserId(), vo.getCouponId(), vo.getAmount().add(vo.getPoundage()), new BigDecimal(0));
                //查询之后可用数量
                UserCouponEntity userCoupon = tbCouponDAO.getUserCouponByUserIdAndType(vo.getCouponId(), vo.getUserId());
                A.check(userCoupon == null, "获取用户优惠券信息失败");
                extractVO.setApplyNumAfter(userCoupon.getCouponNum());
            }

            extractVO.setId(idArray[i]);
            extractVO.setStatus(status);
            extractVO.setOperatorUserId(SubjectUtil.getCurrent().getId());
            extractVO.setOperatorTime(new Date());
            //修改日志表状态
            int rows = tbCouponDAO.updateLogStatus(extractVO);
            A.check(rows != 1, "更新日志出错");
        }

    }


    /**
     * 修改指定用户的优惠券
     *
     * @param userId
     * @param couponId              优惠券id
     * @param deltaAvailableBalance 可用优惠券的变动金额，负数表示减少
     * @param deltaReservedBalance  待审核优惠券总数的变动金额，负数表示减少
     */
    @Transactional
    public void updateUserAsset(String userId, String couponId,
                                BigDecimal deltaAvailableBalance,
                                BigDecimal deltaReservedBalance) {
        UserCouponEntity userCouponEntity = tbCouponDAO.getUserCouponByUserIdAndTypeForUpdate(couponId, userId);
        A.check(userCouponEntity == null, "优惠券不存在");
        if (userCouponEntity.getCouponNum().compareTo(BigDecimal.ZERO) < 0) {
            logger.info("couponNum is  " + userCouponEntity.getCouponNum() + "userId is   " + userId);
        }
        A.check(userCouponEntity.getCouponNum().compareTo(BigDecimal.ZERO) < 0, "优惠券余额不足");
        UserCouponEntity userCoupon = new UserCouponEntity();
        userCoupon.setCouponId(couponId);
        userCoupon.setUserId(userId);

        /*if (deltaReservedBalance != null && deltaReservedBalance.compareTo(BigDecimal.ZERO) != 0) {
            userCoupon.setCurrentExtractNum(userCouponEntity.getCurrentExtractNum().add(deltaReservedBalance));
            A.check(userCoupon.getCurrentExtractNum().compareTo(BigDecimal.ZERO) < 0, "待审核优惠券余额不足");
        }*/

        if (deltaAvailableBalance != null && deltaAvailableBalance.compareTo(BigDecimal.ZERO) != 0) {
            userCoupon.setCouponNum(userCouponEntity.getCouponNum().add(deltaAvailableBalance));
        }

        int rows = tbCouponDAO.updateCouponByUserIdAndCouponNum(userCoupon);
        A.check(rows == 0, "修改优惠券失败");

        //纪录流水
        /*StatementEntity statementEntity = new StatementEntity(userId, currency,
                userAssetEntity.getAvailableBalance(),
                assetNew.getAvailableBalance(),
                deltaAvailableBalance,
                userAssetEntity.getReservedBalance(),
                assetNew.getReservedBalance(),
                deltaReservedBalance, tradeType.getType(), referenceId);

        if (deltaReservedBalance == null || deltaReservedBalance.compareTo(BigDecimal.ZERO) == 0) {
            statementEntity.setReservedAfter(statementEntity.getReservedBefore());
            statementEntity.setReservedChange(new BigDecimal("0"));

        }

        if (deltaAvailableBalance == null || deltaAvailableBalance.compareTo(BigDecimal.ZERO) == 0) {
            statementEntity.setAvailableAfter(statementEntity.getAvailableBefore());
            statementEntity.setAvailableChange(new BigDecimal("0"));
        }

        statementService.addStatement(statementEntity);*/
    }

    /**
     * 查询游戏彩票积分提取记录--导出
     *
     * @param extractVO
     * @return
     */
    @Override
    public List<ExtractVO> getExportExtractList(ExtractVO extractVO) {
        List<ExtractVO> newList = new ArrayList<>();
        Map<String, String> userMap = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        try {
            //获取提取账户姓名
            logger.info("getUserIdAndTrueName----" + "being");
            List<UserEntity> userList = userDAO.getUserIdAndTrueName();
            for (UserEntity user : userList) {
                userMap.put(user.getId(), user.getTrueName() + user.getMobile());
            }
            logger.info("getUserIdAndTrueName----" + "end");
            //获取到账账户姓名
            List<UserEntity> userLists = userDAO.getUserIdAndTrueNameArrive();
            for (UserEntity user : userLists) {
                map.put(user.getId(), user.getTrueName() + user.getMobile());
            }

            logger.info("getExportExtractList----" + "being");
            List<ExtractVO> list = tbCouponDAO.getExtractList(extractVO);
            logger.info("getExportExtractList----" + "end");
            for (ExtractVO vo : list) {

                if (userMap.get(vo.getUserId()) != null) {
                    vo.setTrueName(userMap.get(vo.getUserId()));
                }
                //转换操作类型：1：提取彩票积分,2:兑换成余额
                if(vo.getOperateType().equals("1")){
                    vo.setOperateType("提取");
                }
                if(vo.getOperateType().equals("2")){
                    vo.setOperateType("兑换");
                    vo.setArriveAccount(map.get(vo.getArriveAccount()));
                }
                //转换操作状态：0：未审核,1:通过,2:拒绝
                if (vo.getStatus().equals("0")) {
                    vo.setStatus("未审核");
                } else if (vo.getStatus().equals("1")) {
                    vo.setStatus("通过");
                } else if (vo.getStatus().equals("2")) {
                    vo.setStatus("拒绝");
                } else vo.setStatus("未知状态");
                newList.add(vo);
            }
            return newList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newList;
    }
}
