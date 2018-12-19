package cn.kt.mall.management.logistics.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.management.admin.dao.AdminDAO;
import cn.kt.mall.management.admin.service.impl.UserOperatorLogServiceImpl;
import cn.kt.mall.management.logistics.dao.LogisticsDAO;
import cn.kt.mall.management.logistics.dao.ShopTransportDAO;
import cn.kt.mall.management.logistics.entitys.TradeItemEntity;
import cn.kt.mall.management.logistics.service.ShopTradePatchService;
import cn.kt.mall.management.logistics.vo.*;
import cn.kt.mall.shop.logistics.entity.Logistics;
import cn.kt.mall.shop.logistics.mapper.LogisticsMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ShopTradePatchServiceImpl implements ShopTradePatchService {

    @Autowired
    private LogisticsDAO logisticsDAO;

    @Autowired
    private ShopTransportDAO shopTransportDAO;

    /**
     * 物流详情操作
     */
    @Autowired
    private LogisticsMapper logisticsMapper;

    @Autowired
    private AdminDAO adminDAO;
    @Autowired
    private UserOperatorLogServiceImpl userOperatorLogServiceImpl;

    @Override
    @Transactional
    public Success mergeGoodTrade(MergeTradeBaseInfoVO baseInfoVO) {

        String transportNo = baseInfoVO.getTransportNo();
        String transportCompany = baseInfoVO.getTransportCompany();
        BigDecimal freightFree = baseInfoVO.getFreightFree();
        A.check(transportCompany == null, "未选择物流公司");
        A.check(transportCompany .equals("") , "未选择物流公司");
        for(MergeTradeReqsVO vo:baseInfoVO.getTradeReqsVOList()){

            int i = shopTransportDAO.getShopTranSportByShopIdAndTradeIdAndGoodId(vo.getShopId(),vo.getTradeNo(),vo.getGoodId());

                A.check(i>0,"对不起，你选中的商品存在已经发货的，不能重复发货");

        }

        for (MergeTradeReqsVO vo:baseInfoVO.getTradeReqsVOList()) {
            String shopId = vo.getShopId();
            String tradeNo = vo.getTradeNo();
            String tradeId = vo.getTradeId();
            String goodId = vo.getGoodId();
            A.check(shopId == null, "未传入商店Id");
            A.check(tradeNo == null, "未传入订单编号");
            A.check(tradeId == null, "未传入订单Id");
            A.check(goodId == null, "请选择要发货的商品");

            int i = logisticsMapper.getLogisticsCount(transportNo, transportCompany);
            A.check(i > 0, "请勿重复发货");

            Date now = new Date();

            //同时修改商品详情表的发货状态
            TradeItemEntity entity = new TradeItemEntity();
            entity.setShopId(shopId);
            entity.setTradeId(tradeId);
            entity.setGoodId(goodId);
            entity.setGoodStatus("1");
            entity.setLastTime(now);
            entity.setFreightFree(freightFree);

            logisticsDAO.updateShopTradeItem(entity);
            ShopTransportVO shopTransportVO = new ShopTransportVO();
            shopTransportVO.setGoodId(goodId);
            shopTransportVO.setId(IDUtil.getUUID());
            shopTransportVO.setOperatorTime(now);
            shopTransportVO.setTotalFreightFree(freightFree);
            shopTransportVO.setShopId(shopId);
            shopTransportVO.setTradeNo(tradeNo);
            shopTransportVO.setTransportNo(transportNo);
            shopTransportVO.setLogisticsName(transportCompany);
            shopTransportVO.setUserId(SubjectUtil.getCurrent().getId());
            shopTransportDAO.addShopTranSport(shopTransportVO);

            int tradeNum = logisticsDAO.selectTradeGoodNum(tradeId, shopId);
            //1.待发货 2.待收货 3.已完成 4.部分待收货（部分发货）',
            String sendStatus = "1";
            String status = "4";
            if (tradeNum == 0) {
                sendStatus = "2";
                status = "2";
            }
            logisticsDAO.updateShopTrade(tradeId, sendStatus, status);
        }

        //编辑物流公司物流信息
        Logistics logistics = new Logistics();
        logistics.setCom(transportCompany);
        logistics.setLogisticsNo(transportNo);
        logistics.setLogisticsStatus(0);
        logisticsMapper.addLogistics(logistics);



        return Response.SUCCESS;
    }


    @Override
    @Transactional
    public Success patchSendLogistics(PatchSendBaseVO patchSend) {

        A.check(patchSend==null,"请选择需要发货商品");
        String transportNo = patchSend.getTransportNo();
        String transportCompany = patchSend.getTransportCompany();
        BigDecimal freightFree = patchSend.getFreightFree();
        if(patchSend.getSendReqsVOList()!=null&&patchSend.getSendReqsVOList().size()>0){
            Date now =new  Date();
            int i = logisticsMapper.getLogisticsCount(patchSend.getTransportNo(),patchSend.getTransportCompany());
            A.check(i>0 , "重复发货或该2物流号已经被使用");
            List<PatchSendReqsVO> patchSendList = patchSend.getSendReqsVOList();
            if(patchSendList!=null&&patchSendList.size()>0){
                for(PatchSendReqsVO vo:patchSendList){
                    //查询玩家订单表的信息
                    LogisticsVO logisticsVO = null;
                    //logisticsDAO.getLogisticsVOByTradeIdAndshopId(tradeId, null);
                    //查询出物品详情表里的信息，根据
                   /* BigDecimal freightFree = new BigDecimal(0);
                    if(patchSend.getFreightFree()!=null) {
                        freightFree =patchSend.getFreightFree();
                    }*/
                    TradeItemEntity entity = new TradeItemEntity();
                    entity.setShopId(logisticsVO.getShopId());
                    entity.setTradeId(vo.getTradeId());
                    entity.setGoodId(vo.getGoodId());
                    entity.setGoodStatus("1");
                    entity.setLastTime(now);
                    entity.setFreightFree(freightFree);
                    logisticsDAO.updateShopTradeItem(entity);

                    ShopTransportVO shopTransportVO = new ShopTransportVO();
                    shopTransportVO.setGoodId(vo.getGoodId());
                    shopTransportVO.setId(IDUtil.getUUID());
                    shopTransportVO.setOperatorTime(now);
                    shopTransportVO.setTotalFreightFree(freightFree);
                    shopTransportVO.setShopId(logisticsVO.getShopId());
                    shopTransportVO.setTransportNo(patchSend.getTransportNo());
                    shopTransportVO.setUserId(SubjectUtil.getCurrent().getId());
                    shopTransportVO.setLogisticsName(patchSend.getTransportCompany());
                    shopTransportDAO.addShopTranSport(shopTransportVO);
                    int tradeNum = logisticsDAO.selectTradeGoodNum(vo.getTradeId(),logisticsVO.getShopId());
                    //1.待发货 2.待收货 3.已完成 4.部分待收货（部分发货）',
                    String sendStatus ="1";
                    String status = "4";
                    if(tradeNum == 0){
                        sendStatus="2";
                        status="2";
                    }
                    logisticsDAO.updateShopTrade(vo.getTradeId(),sendStatus,status);
                }
            }
            Logistics logistics =new Logistics();
            logistics.setCom(patchSend.getTransportCompany());
            logistics.setLogisticsNo(patchSend.getTransportNo());
            logistics.setLogisticsStatus(0);
            logisticsMapper.addLogistics(logistics);

        }else{
            A.check(patchSend==null,"请选择需要发货商品");
        }

        return Response.SUCCESS;
    }
}
