package cn.kt.mall.shop.cash.mapper;

import cn.kt.mall.shop.cash.entity.CashEntity;
import cn.kt.mall.shop.cash.vo.CashReqVO;
import cn.kt.mall.shop.cash.vo.CashRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

@Mapper
public interface CashDAO {

    // 根据id获取提现记录
    CashEntity getCashByID(@Param("cashId") String cashId);

    // 获取提现记录
    List<CashRespVO> getCashList(CashReqVO cashReqVO, RowBounds rowBound);

    List<CashRespVO> getCashList(CashReqVO cashReqVO);
    // 修改提现记录状态
    int updateCashStatusById(@Param("id") String id, @Param("status") String status, @Param("currentTime") Date currentTime);

    // 新增提现记录
    int addCash(CashEntity cashEntity);

    // 查询上级店铺权限
    String getShopWhetherPay(@Param("pid") String pid);

}
