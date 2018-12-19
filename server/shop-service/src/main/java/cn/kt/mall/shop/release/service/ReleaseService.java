package cn.kt.mall.shop.release.service;

import cn.kt.mall.shop.coupon.vo.BasePointVO;
import cn.kt.mall.shop.coupon.vo.BasePopcVO;
import cn.kt.mall.shop.coupon.vo.TradePopcAndPointVO;

import java.util.List;

public interface ReleaseService {

    void releaseBasePopc(List<BasePopcVO> list);

    void releaseBasePoint(List<BasePointVO> list);

    void callTradePopcRelease(List<TradePopcAndPointVO> list);

    void callTradePointRelease(List<TradePopcAndPointVO> list);
}
