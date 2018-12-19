package cn.kt.mall.shop.trade.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.kt.mall.shop.trade.entity.TradeEntity;
import cn.kt.mall.shop.trade.vo.TradeAbnormalRespVO;
import cn.kt.mall.shop.trade.vo.TradeCountVO;
import cn.kt.mall.shop.trade.vo.TradeManageReqVO;

@Mapper
public interface TradeDAO {

    // 添加交易记录
    int addTrade(TradeEntity tradeEntity);

    int delTrade(@Param("id") String id, @Param("flag") Short flag);

    // 批量新增
    int insertBatch(@Param("list") List<TradeEntity> list);
    // 新增
    int insertTradeEntity( TradeEntity tradeEntity);
    int myTradeCount(@Param("userId") String userId, @Param("status") String status);

    List<TradeEntity> myTrade(@Param("userId") String userId, @Param("status") String status,
                              @Param("offset") int offset, @Param("pageSize") int pageSize);

    List<TradeCountVO> tradeByShopIdCount(@Param("shopId") String shopId, @Param("status") String status,
                                          @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<TradeEntity> tradeByShopId(@Param("shopId") String shopId, @Param("status") String status,
                                    @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 获取交易根据tradeNo
    TradeEntity getTradeByTradeNo(@Param("userId") String userId, @Param("tradeNo") String tradeNo);

    // 获取交易根据interiorNo
    List<TradeEntity> getTradeByInteriorNo(@Param("userId") String userId, @Param("interiorNo") String interiorNo);

    // 获取交易根据id
    TradeEntity getTradeById(@Param("userId") String userId, @Param("id") String id);

    // 更新订单状态
    int updateTradeStatusById(@Param("id") String id, @Param("status") String status);

    int insertTrade(@Param("tradeEntity") TradeEntity tradeEntity);

    int updateTradeCoinById(@Param("id") String id, @Param("coin") BigDecimal coin);

    int updateTradeCoinAndStatus(@Param("id") String id, @Param("coin") BigDecimal coin,
                                 @Param("status") String status);

    // 设置订单状态
    int setTradeStatus(@Param("id") String id, @Param("currentStatus") String currentStatus,
                       @Param("targetStatus") String targetStatus);

    // 获取订单列表
    List<TradeEntity> listTrade(TradeManageReqVO tradeManageReqVO, RowBounds rowBounds);

    int updateSelective(TradeEntity tradeEntity);

    // 退货订单列表
    List<TradeAbnormalRespVO> drawTradeList(TradeManageReqVO tradeManageReqVO, RowBounds rowBounds);

    int countByShopIdAndStatus(@Param("shopId") String shopId, @Param("status") String status);

    BigDecimal manageCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int delShopTrade(@Param("shopId") String shopId, @Param("shopFlag") Short shopFlag);


    // 获取子级订单列表
    List<TradeEntity> listTradeByShopId(TradeManageReqVO tradeManageReqVO, RowBounds rowBounds);

    List<TradeEntity> listTradeByShopId(TradeManageReqVO tradeManageReqVO);
}
