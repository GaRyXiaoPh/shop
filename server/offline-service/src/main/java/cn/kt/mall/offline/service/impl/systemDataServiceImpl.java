package cn.kt.mall.offline.service.impl;

import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.offline.dao.GoodsDAO;
import cn.kt.mall.offline.dao.OfflineShopDAO;
import cn.kt.mall.offline.dao.OfflineUserDAO;
import cn.kt.mall.offline.dao.OrderDAO;
import cn.kt.mall.offline.entity.DataEntity;
import cn.kt.mall.offline.entity.MerInfoEntity;
import cn.kt.mall.offline.service.SystemDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/5/25.
 */
@Service("systemDataService")
public class systemDataServiceImpl implements SystemDataService {

    @Autowired
    private OfflineShopDAO offlineShopDAO;

    @Autowired
    private GoodsDAO goodsDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private OfflineUserDAO offlineUserDAO;

    @Override
    public Map homePage(String userId) {
        String shopId = offlineUserDAO.getShopId(userId);
        Map map = new HashMap();
        //店铺信息
        MerInfoEntity merInfo = offlineShopDAO.getMerInfo(shopId);
        //获取商品管理信息
        List<DataEntity> goodList = goodsDAO.getGoodNum(userId);
        for(DataEntity l:goodList){
            if(l.getStatus()==1){
                l.setStatusName("待审核");
            }
            if(l.getStatus()==2){
                l.setStatusName("已上架");
            }
            if(l.getStatus()==3){
                l.setStatusName("已下架");
            }
            if(l.getStatus()==4){
                l.setStatusName("未通过");
            }
        }


        //获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String today = sdf.format(now);
        //获取昨天的日期
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(now);
        theCa.add(theCa.DATE, -1);
        now = theCa.getTime();
        String yesterday = sdf.format(now);
        //获取最近
        theCa.setTime(now);
        theCa.add(theCa.DATE, -6);
        now = theCa.getTime();
        String hisDay = sdf.format(now);
        //获取最近七天的销售总额
        String startTime = hisDay +" 00:00:00";
        String endTime = yesterday+" 23:59:59";
        List<DataEntity> transData = orderDAO.getTransData(startTime,endTime,null,shopId);

        //获取昨天的交易订单和莱姆币
        startTime = yesterday +" 00:00:00";
        endTime = yesterday +" 23:59:59";
        List<DataEntity> yesterData = orderDAO.getTransData(startTime,endTime,yesterday,shopId);
        int yesterNum = 0;
        double yesterLem = 0.0;
        if (yesterData.size()>0){
            yesterNum = yesterData.get(0).getNum();
            yesterLem = yesterData.get(0).getLyme();
        }

        //获取今天的交易订单和莱姆币
        startTime = today +" 00:00:00";
        endTime = today +" 23:59:59";
        List<DataEntity> todayData = orderDAO.getTransData(startTime,endTime,today,shopId);
        int todayNum = 0;
        double todayLem = 0.0;
        if(todayData.size()>0){
            todayNum = todayData.get(0).getNum();
            todayLem = todayData.get(0).getLyme();
        }

        //店铺图片
        map.put("companyImg",merInfo.getCompanyImg());
        //店铺名称
        map.put("shopName",merInfo.getShopName());
        //商品管理
        map.put("goodManage",goodList);
        //今天的交易概况
        map.put("todayNum",todayNum);
        map.put("todayLem",todayLem);
        //昨天的交易概况
        map.put("yesterNum",yesterNum);
        map.put("yesterLem",yesterLem);
        //店铺累计收益
        map.put("income",orderDAO.getShopIncome(shopId));

        UserAssetEntity wallet = userAssetService.getUserAssetByCurrency(SubjectUtil.getCurrent().getId(), AssetType.CREDIT.getStrType());
        double balance =0.0;
        if(wallet != null){
            balance = wallet.getAvailableBalance().doubleValue();
        }
        //账户莱姆余额
        map.put("balance",balance);
        //交易分析
        map.put("transData",transData);

        return map;
    }

    @Override
    public Map dataStatistics(String userId) {
        String shopId = offlineUserDAO.getShopId(userId);
        Map map = new HashMap();

        //获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();

        //获取昨天的日期
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(now);
        theCa.add(theCa.DATE, -1);
        now = theCa.getTime();
        String yesterday = sdf.format(now);
        //获取最近
        theCa.setTime(now);
        theCa.add(theCa.DATE, -6);
        now = theCa.getTime();
        String hisDay = sdf.format(now);
        //获取最近七天的销售总额
        String startTime = hisDay +" 00:00:00";
        String endTime = yesterday+" 23:59:59";
        List<DataEntity> transData = orderDAO.getTransData(startTime,endTime,null,shopId);


        map.put("transData",transData);
        return map;
    }
}
