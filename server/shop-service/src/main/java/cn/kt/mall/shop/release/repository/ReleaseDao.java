package cn.kt.mall.shop.release.repository;

import cn.kt.mall.shop.coupon.vo.BasePointVO;
import cn.kt.mall.shop.coupon.vo.BasePopcVO;
import cn.kt.mall.shop.coupon.vo.TradePopcAndPointVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ReleaseDao {


    void callBasePopcRelease (@Param("list") List<BasePopcVO> list);

    void callBasePointRelease(@Param("list") List<BasePointVO> list);

    void callTradePopcRelease(@Param("list") List<TradePopcAndPointVO> list);

    void callTradePointRelease(@Param("list") List<TradePopcAndPointVO> list);

}
