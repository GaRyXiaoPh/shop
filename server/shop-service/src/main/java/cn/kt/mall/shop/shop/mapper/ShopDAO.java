package cn.kt.mall.shop.shop.mapper;

import cn.kt.mall.shop.collect.vo.ShopCollectVO;
import cn.kt.mall.shop.shop.entity.ShopAuthdataEntity;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.vo.*;

import cn.kt.mall.shop.trade.entity.TradeItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface ShopDAO {

    List<ShopEntity> getUserIdListByShopIdList(@Param("shopIds") List<String> shopIds);

    // 获取店铺信息
    ShopEntity getShopEntityByShopId(@Param("shopId") String shopId);

    List<ShopEntity> listShopByIds(@Param("shopIds") List<String> shopIds, @Param("userId") String userId,
                                   @Param("status") String status);

    // 获取商城商圈
    List<ShopEntity> getMyShopEntity(@Param("userId") String userId);

    // 获取认证材料
    ShopAuthdataEntity getMyShopAuthdataEntity(@Param("shopId") String shopId);

    // 获取正在申请或者已经通过
    List<ShopEntity> getMyOnLineShopEntity(@Param("userId") String userId);

    List<ShopEntity> getMyOffLineShopEntity(@Param("userId") String userId);

    // 添加商城
    int addShopEntity(ShopEntity shopEntity);

    int addShopAuthdataEntity(ShopAuthdataEntity authdataEntity);

    int updateSelective(ShopEntity shopEntity);

    int updateShopRankAndConsume(ShopEntity shopEntity);

    int updateShopConsume(ShopEntity shopEntity);

    List<ShopAuthManageVO> listAuthData(@Param("userName") String userName, @Param("startTime") Date startTime,
                                        @Param("endTime") Date endTime, @Param("shopType") String shopType, @Param("status") String status,
                                        RowBounds rowBounds);

    List<ShopCollectVO> listCollectShopByIds(@Param("shopIds") List<String> shopIds);

    int countByType(@Param("shopType") String shopType);

    //根据用户查询shopid
    List<String> getShopListByUserId(@Param("userIds") List<String> userIds);

    //搜索商铺
    List<ShopEntity> searchShops(@Param("shopId") String shopId, @Param("shopName") String shopName,
                                 @Param("status") String status, @Param("startTime") Date startTime,
                                 @Param("endTime") Date endTime, RowBounds rowBounds);

    List<ShopEntity> getShopByShopNoAndLevel(@Param("shopNo") String shopNo,
                                             @Param("name") String name,
                                             @Param("shopLevel") String shopLevel,
                                             @Param("startTime") String startTime,
                                             @Param("endTime") String endTime,
                                             @Param("shopName") String shopName,
                                             @Param("mobile") String mobile,
                                             @Param("status") String status,
                                             RowBounds rowBounds);

    String getShopIdByShopNo(@Param("shopNo") String shopNo);

    List<ShopVO> getShopListByPid(@Param("shopIds") List<String> shopIds, @Param("name") String name, @Param("shopName") String shopName, @Param("shopNo") String shopNo, RowBounds rowBounds);

    ShopVO getShopIdGoodCountAndAchievement(@Param("shopId") String shopId);

    List<ShopEntity> getShopByShopNoAndShopNameAndName(@Param("shopNo") String shopNo,
                                                       @Param("shopName") String shopName,
                                                       @Param("name") String name,
                                                       RowBounds rowBounds);

    String getShopIdByUserId(@Param("userId") String userId);

    ShopEntity getShopByUserId(@Param("userId") String userId);

    String getShopByIDorPID(@Param("userId") String userId, @Param("userPid") String userPid);

    List<ShopSalesVO> shopSalesRecord(@Param("shopId") String shopId, @Param("startTime") String startTime,
                                      @Param("endTime") String endTime, RowBounds rowBounds);

    List<ShopTradeGoodSalesVO> getShopGoodsCountSales(@Param("shopId") String shopId,
                                                      @Param("goodName") String goodName,
                                                      @Param("startTime") String startTime,
                                                      @Param("endTime") String endTime, RowBounds rowBound);


    List<ShopStatisticsVO> getShopStatisticsList(@Param("shopIds") List<String> shopIds,
                                                 @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime,
                                                 @Param("mobile") String mobile,
                                                 RowBounds rowBound);


    List<ShopVO> getShopListByPidAndExport(@Param("shopIds") List<String> shopIds, @Param("name") String name, @Param("shopName") String shopName, @Param("shopNo") String shopNo);


    /**
     * 查询店铺业绩合并列表
     *
     * @return
     */
    List<ShopStatisticsVO> getShopStatisticss(@Param("shopType") String shopType,
                                              @Param("beginTime") String beginTime,
                                              @Param("endTime") String endTime,
                                              @Param("shopNo") String shopNo,
                                              @Param("shopName") String shopName,
                                              @Param("userName") String userName, RowBounds rowBounds);

    List<ShopStatisticsVO> getShopStatisticss(ShopStatisticsReqVO shopStatisticsReqVO);

    /**
     * 编辑店铺开启&关闭状态（批量）
     *
     * @param status  状态：1开启，3管理员禁用
     * @param idsList 店铺id
     * @return
     */
    int editShopStatus(@Param("status") String status,
                       @Param("idsList") List<String> idsList);

    /**
     * 查询商品销售记录
     *
     * @param shopType
     * @param shopName
     * @param skuName
     * @param startTime
     * @param endTime
     * @param rowBounds
     * @return
     */
    List<ShopTradeGoodSalesVO> shopSalesRecordExport(@Param("shopType") String shopType,
                                                     @Param("shopName") String shopName,
                                                     @Param("skuName") String skuName,
                                                     @Param("startTime") String startTime,
                                                     @Param("endTime") String endTime, RowBounds rowBounds);

    //导出商品销售记录
    List<ShopTradeGoodSalesVO> shopSalesRecordExport(ShopTradeGoodSalesReqVO shopTradeGoodSalesReqVO);


    // 导出商城后台-商品销售记录 begin
    List<ShopTradeGoodSalesVO> getShopGoodsCountSalesExport(@Param("shopId") String shopId,
                                                            @Param("goodName") String goodName,
                                                            @Param("startTime") String startTime,
                                                            @Param("endTime") String endTime);

    //导出商城后台-店铺销售记录
    List<ShopSalesVO> shopSalesRecordByShopExport(@Param("shopId") String shopId, @Param("startTime") String startTime,
                                                  @Param("endTime") String endTime);


    List<ShopStatisticsVO> getShopStatisticsListByShopExportt(@Param("shopIds") List<String> shopIds,
                                                              @Param("startTime") String startTime,
                                                              @Param("endTime") String endTime,
                                                              @Param("mobile") String mobile);
    //end

    //根据时间分组查询店铺业绩
    List<ShopSalesAndTimeVO> getShopSalesAndTime(@Param("shopId") String shopId,
                                                 @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime);


    //查询店铺总收入
    ShopSalesAndTimeVO getShopSalesAndPointAndCoupon(@Param("shopId") String shopId);

    //获得店铺商品总收益
    ShopTradeGoodSalesVO getShopGoodsSales(@Param("shopId") String shopId);

    //锁定店铺表
    List<ShopEntity> shopForUpdate();

    //查询所有店铺返回店铺ID
    List<String> getShopsList();

    //修改商铺名称与编码
    int updateShopInfo(ShopEntity shop);

    //随机获得店铺
    List<ShopEntity> randShop(@Param("num") Integer num);
    //获取所有店铺信息
    List<ShopEntity> getAllShop();

    List<String> getCloseShop();
    //统计商品销售总和
    BigDecimal shopSalesRecordTotalPrice(ShopTradeGoodSalesReqVO shopTradeGoodSalesReqVO);
}
