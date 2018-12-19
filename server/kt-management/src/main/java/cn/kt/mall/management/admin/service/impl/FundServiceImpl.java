package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.model.CurrentUser;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.common.UserRechargeConstant;
import cn.kt.mall.common.wallet.mapper.UserAssetDAO;
import cn.kt.mall.common.wallet.mapper.UserRechargeLogDAO;
import cn.kt.mall.common.wallet.service.ShopSalesAmountService;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.common.wallet.vo.UserRechargeLogVO;
import cn.kt.mall.management.admin.dao.FundDAO;
import cn.kt.mall.management.admin.service.FundService;
import cn.kt.mall.management.admin.vo.FundVO;
import cn.kt.mall.management.admin.vo.fund.DeliveFundsItemVO;
import cn.kt.mall.management.admin.vo.fund.DeliveFundsVO;
import cn.kt.mall.management.admin.vo.fund.FundReqVO;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FundServiceImpl implements FundService {

    @Autowired
    private UserRechargeLogDAO userRechargeLogDAO;

    @Autowired
    private FundDAO fundDAO;
    @Autowired
    private UserAssetService userAssetService;
    @Autowired
    UserAssetDAO userAssetDAO;
    @Autowired
    private UserOperatorLogServiceImpl userOperatorLogServiceImpl;
    @Autowired
    private ShopSalesAmountService shopSalesAmountService;

    @Override
    public CommonPageVO<FundVO> getFundList(FundReqVO fundReqVO, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<FundVO> list = fundDAO.getFundList(fundReqVO, rowBounds);
        PageInfo<FundVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    @Transactional
    public String passFund(String id, String status) {
        A.check(StringUtils.isBlank(id) || StringUtils.isBlank(status), "获取参数失败");
        //校验该条信息是否存在
        UserRechargeLogVO urlVO = userAssetService.getUserRechargeLogListById(id);
        A.check(!urlVO.getStatus().equals("0"), "该条记录已审核，不能再次进行审核！");
        A.check(urlVO == null, "获取审核信息失败");
        /************充值开始***************/
        if (urlVO.getOperationType().equals(UserRechargeConstant.USER_RECHARGE)) {
            /************通过充值***************/
            if (status.equals(UserRechargeConstant.LOG_TAKE_EFFECT)) {
                //以A对B操作为例
                //A扣除冻结    可用余额的变动金额，负数表示减少,冻结余额的变动金额，负数表示减少
                if (urlVO.getCustomerType().equals(UserRechargeConstant.TYPE_SHOP_USER)) {
                    //A冻结减少
                    userAssetService.updateUserAsset(urlVO.getUserId(), urlVO.getRechargeType(), new BigDecimal(0), new BigDecimal("-" + urlVO.getRechargeAmount()), TradeType.SHOP_FREEZE_LESS, urlVO.getId());
                }
                //B可用增加
                userAssetService.updateUserAsset(urlVO.getRechargeUser(), urlVO.getRechargeType(), urlVO.getRechargeAmount(), new BigDecimal(0), TradeType.MEMBER_AVAILABLE_SUB, urlVO.getId());
                shopSalesAmountService.updateShopSales(urlVO.getRechargeUser(), urlVO.getRechargeAmount(), TradeType.MEMBER_AVAILABLE_SUB, urlVO.getId());
            }

            /************拒绝充值***************/
            else if (status.equals(UserRechargeConstant.LOG_INVALID) && urlVO.getCustomerType().equals(UserRechargeConstant.TYPE_SHOP_USER)) {
                userAssetService.updateUserAsset(urlVO.getUserId(), urlVO.getRechargeType(), urlVO.getRechargeAmount(), new BigDecimal("-" + urlVO.getRechargeAmount()), TradeType.SHOP_AVAILABLE_SUB, urlVO.getId());
            }

        }
        /************扣除开始***************/
        else if (urlVO.getOperationType().equals(UserRechargeConstant.USER_DEDUCTION)) {
            /************通过扣除***************/
            if (status.equals(UserRechargeConstant.LOG_TAKE_EFFECT)) {
                //无论是店铺或admin操作，都将会员的冻结减少
                userAssetService.updateUserAsset(urlVO.getRechargeUser(), urlVO.getRechargeType(), new BigDecimal(0), new BigDecimal("-" + urlVO.getRechargeAmount()), TradeType.REDUCE, urlVO.getId());
                shopSalesAmountService.updateShopSales(urlVO.getRechargeUser(), new BigDecimal("-" + urlVO.getRechargeAmount()), TradeType.REDUCE, urlVO.getId());
                // 店铺增加可用
                if (urlVO.getCustomerType().equals(UserRechargeConstant.TYPE_SHOP_USER)) {
                    userAssetService.updateUserAsset(urlVO.getUserId(), urlVO.getRechargeType(), urlVO.getRechargeAmount(), new BigDecimal(0), TradeType.SHOP_AVAILABLE_SUB_K, urlVO.getId());
                }
            }
            /************拒绝扣除***************/
            else if (status.equals(UserRechargeConstant.LOG_INVALID)) {
                //无论是店铺或admin操作，都将会员的冻结减少，可用的增加
                userAssetService.updateUserAsset(urlVO.getRechargeUser(), urlVO.getRechargeType(), urlVO.getRechargeAmount(), new BigDecimal("-" + urlVO.getRechargeAmount()), TradeType.MEMBER_AVAILABLE_SUB_K, urlVO.getId());
            }
        }
        //修改充值记录状态
        int rows = userRechargeLogDAO.updateLogStatus(id, status);
        A.check(rows != 1, "更新日志出错");
        userOperatorLogServiceImpl.addUserOperatorLog(SubjectUtil.getCurrent().getId(), Integer.parseInt(urlVO.getOperationType()), urlVO.getOperationType().equals("1") ? "充值" : "扣除");
        return id;
    }

    /**
     * 批量修改提现状态
     *
     * @param deliveFundsVO
     */
    @Transactional
    public void deliveFundsBatch(DeliveFundsVO deliveFundsVO) {
        for (DeliveFundsItemVO deliveFundsItemVO : deliveFundsVO.getData()) {
            ((FundService) AopContext.currentProxy()).passFund(deliveFundsItemVO.getFundId(),
                    deliveFundsItemVO.getStatus());
        }
    }

}
