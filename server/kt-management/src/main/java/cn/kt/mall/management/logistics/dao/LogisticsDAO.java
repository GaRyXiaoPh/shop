package cn.kt.mall.management.logistics.dao;

import cn.kt.mall.management.logistics.entitys.TradeItemEntity;
import cn.kt.mall.management.logistics.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface LogisticsDAO {

    /**
     * 查询出对应的内部编号,面对所有
     */
    List<InneriornoVO> getInteriorNoList(CurrentTimeResqVO vo);

    //1.待发货 2.待收货 3.已完成 4.部分待收货（部分发货）',
    List<CurrentTimeVO> getCurrentTimeVOList(LogisticsRquestVO vo, RowBounds rowBounds);

    //1.待发货 2.待收货 3.已完成 4.部分待收货（部分发货）',


    /**
     * 查询未发货的1.待发货
     * @param vo
     * @return
     */
    List<CurrentTimeVO> getInteriorNoListNoSend(LogisticsRquestVO vo, RowBounds rowBounds);
    /**
     * 查询的2.已发货
     * @param vo
     * @return
     */
    List<CurrentTimeVO> getInteriorNoListSended(LogisticsRquestVO vo, RowBounds rowBounds);
    /**
     * 查询已完成3.已完成
     * @param vo
     * @return
     */
    List<CurrentTimeVO> getInteriorNoListFinish(LogisticsRquestVO vo, RowBounds rowBounds);

    /**
     * 查询部分发货4.部分发货
     * @param vo
     * @return
     */
    List<CurrentTimeVO> getInteriorNoListPartSend (LogisticsRquestVO vo, RowBounds rowBounds);


    //==================物流导出==




    //查询物流信息
    List<LogisticsVO> getLogisticsVOList(InneriornoResqVO vo);

    int updateShopTradeItem(TradeItemEntity entity);

    int updateShopTrade(@Param("id") String tradeId,@Param("sendStatus") String sendStatus,@Param("status") String status);

    int selectTradeGoodNum(@Param("tradeId") String tradeId,@Param("shopId") String shopId);

    /**
     * 查询订单
     * @param tradeId
     * @param shopId
     * @return
     */

     List<LogisticsVO> getLogisticsVOByInteriorNo(@Param("interiorNo") String interiorNo);

    /**
     * 根据Id查询出
     */
    ShopGoodInfoVO getGoodTypeNameByGoodId(@Param("goodId") String goodId);

    /**
     * 根据商店Id和订单Id查询出订单详情列表
     *
     */
    List<ShopTradeItemVO> getShopTradItemList(@Param("tradeId") String tradeId, @Param("shopId") String shopId);

    //查询物流信息


    List<TradeItemEntity> getTradeItemEntityList(@Param("tradeId") String tradeId, @Param("goodNames")List<String> goodNames);


    List<CoupNamesVO> getCoupNameListByGoodId(@Param("goodId") String goodId);

    List<LogisticsExcelVO> getLogisticsExcelVOList(@Param("list") List<LogisticsIdsVO> list);

    int getGoodNumByinteriorNo(@Param("interiorNo") String interiorNo);

    int getNoSendGoodNumByinteriorNo(@Param("interiorNo") String interiorNo);

    ShopUserBaseInfoVO getShopUserBaseInfo(@Param("shopId") String shopId);


   // int getShopTradeItemNumByShopIdAndGoodStatus(@Param("tradeId") String tradeId,@Param("shopId") String shopId);

    //int getShopTradeItemNumByShopIdAndGood(@Param("tradeId") String tradeId,@Param("shopId") String shopId);


    int getNoSendGoodSended(@Param("interiorNo") String interiorNo);




}
