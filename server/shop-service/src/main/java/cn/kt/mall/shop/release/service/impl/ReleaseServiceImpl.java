package cn.kt.mall.shop.release.service.impl;

import cn.kt.mall.shop.coupon.vo.BasePointVO;
import cn.kt.mall.shop.coupon.vo.BasePopcVO;
import cn.kt.mall.shop.coupon.vo.TradePopcAndPointVO;
import cn.kt.mall.shop.release.repository.ReleaseDao;
import cn.kt.mall.shop.release.service.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReleaseServiceImpl implements ReleaseService {
    @Autowired
    private ReleaseDao releaseDao;

    @Override
    public void releaseBasePopc(List<BasePopcVO> list) {
        releaseDao.callBasePopcRelease(list);
    }

    @Override
    public void releaseBasePoint(List<BasePointVO> list) {
        releaseDao.callBasePointRelease(list);
    }

    @Override
    public void callTradePopcRelease(List<TradePopcAndPointVO> list) {
        releaseDao.callTradePopcRelease(list);
    }

    @Override
    public void callTradePointRelease(List<TradePopcAndPointVO> list) {
        releaseDao.callTradePointRelease(list);
    }
}
