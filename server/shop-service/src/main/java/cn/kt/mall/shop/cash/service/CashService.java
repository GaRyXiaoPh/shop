package cn.kt.mall.shop.cash.service;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.shop.cash.entity.CashEntity;
import cn.kt.mall.shop.cash.mapper.CashDAO;
import cn.kt.mall.shop.cash.vo.*;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CashService {

    @Autowired
    CashDAO cashDAO;

    @Autowired
    UserAssetService userAssetService;

    private static Logger logger = Logger.getLogger(CashService.class);
    // 根据id获取提现记录
    public CashVO getCashByID(String cashId) {
        CashEntity cashEntity = cashDAO.getCashByID(cashId);
        A.check(cashEntity == null, "提现记录不存在");
        CashVO cashVO = new CashVO();
        return cashVO;
    }

    // 获取提现记录
    public CommonPageVO<CashRespVO> getCashList(CashReqVO cashReqVO, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<CashRespVO> list = cashDAO.getCashList(cashReqVO, rowBounds);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<CashRespVO> newList = new ArrayList<>();
        for(CashRespVO cashRespVO:list) {
            String whetherPay = null;
            if(cashRespVO.getPid() != null) {
                // 查询上级店铺权限
                whetherPay  = cashDAO.getShopWhetherPay(cashRespVO.getPid());
            }
            cashRespVO.setWhetherPay(whetherPay==null?"0":whetherPay);
            newList.add(cashRespVO);
        }

        PageInfo<CashRespVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, newList);
    }


    /**
     * 获取提现记录
     */
    public List<CashRespVO> getCashList(CashReqVO cashReqVO){
        List<CashRespVO> list = new ArrayList<>();
        try {
            list = cashDAO.getCashList(cashReqVO);
            return list;
        }catch (Exception e){
            logger.info(e);
        }
        return list;
    }

    /**
     *
     * @param cashId
     * @param status
     */

    // 修改提现状态
    @Transactional
    public void deliveCashes(String cashId, String status) {
        int rows = cashDAO.updateCashStatusById(cashId, status,new Date());
        A.check(rows != 1, "更新状态失败");
        if(rows > 0) {
            CashEntity cash = cashDAO.getCashByID(cashId);
            // 通过
            if(status.equals("1")) {
                userAssetService.updateUserAsset(cash.getUserId(),"popc",new BigDecimal(0),new BigDecimal("-"+cash.getCashAmount()), TradeType.WITHDRAWAL,cashId);
            }
            // 拒绝
            if(status.equals("2")) {
                userAssetService.updateUserAsset(cash.getUserId(),"popc",cash.getCashAmount(),new BigDecimal("-"+cash.getCashAmount()), TradeType.WITHDRAWAL, cashId);
            }
        }
    }

    /**
     * 批量修改提现状态
     *
     * @param deliveCashesVO
     */
    @Transactional
    public void deliveCashesBatch(DeliveCashesVO deliveCashesVO) {
        for (DeliveCashesItemVO deliveCashesItemVO : deliveCashesVO.getData()) {
            ((CashService) AopContext.currentProxy()).deliveCashes(deliveCashesItemVO.getCashId(),
                    deliveCashesItemVO.getStatus());
        }
    }

    /**
     * 新增提现记录
     *
     * @param userId
     * @param cashAmount
     */
    @Transactional
    public String addCash(String userId, BigDecimal cashAmount) {
        CashEntity cashEntity = new CashEntity();
        cashEntity.setCashAmount(cashAmount);
        cashEntity.setId(IDUtil.getUUID());
        cashEntity.setUserId(userId);
        cashEntity.setStatus("0");
        cashEntity.setCreateTime(new Date());
        cashDAO.addCash(cashEntity);
        return cashEntity.getId();
    }

}
