package cn.kt.mall.shop.good.mapper;

import java.util.Date;
import java.util.List;

import cn.kt.mall.shop.good.entity.GoodPayEntity;
import cn.kt.mall.shop.good.vo.GoodPayReqVO;
import cn.kt.mall.shop.good.vo.AdResVO;
import cn.kt.mall.shop.good.vo.GoodPayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.kt.mall.shop.good.entity.GoodEntity;
import cn.kt.mall.shop.good.vo.GoodRespVO;

@Mapper
public interface GoodDAO {

    // 获取商品根据商品ID
    GoodEntity getGoodEntityByGoodId(@Param("goodId") String goodId);

    // 获取商品根据商品ID
    GoodEntity getGoodEntityByGoodIdAndShopId(@Param("goodId") String goodId, @Param("shopId") String shopId);

    // 获取商品支付方式
    List<GoodPayEntity> getGoodPayEntityByGoodId(@Param("goodId") String goodId);


    // 获取商品列表
    int searchGoodsCount(@Param("goodName") String goodName, @Param("goodType") String goodType,
                         @Param("status") String status);

    List<GoodRespVO> searchGoods(@Param("shopId") String shopId, @Param("goodName") String goodName,
                                 @Param("goodType") String goodType,
                                 @Param("payTypes") String payTypes,
                                 @Param("searchMode") int searchMode,
                                 @Param("startTime") Date startTime, @Param("endTime") Date endTime, RowBounds rowBounds);

    List<GoodRespVO> searchGoodsFront(@Param("shopId") String shopId, @Param("goodName") String goodName,
                                      @Param("goodType") String goodType, @Param("status") String status, @Param("searchMode") String searchMode,
                                      @Param("startTime") Date startTime, @Param("endTime") Date endTime, RowBounds rowBounds);

    List<GoodRespVO> searchShopGood(@Param("shopId") String shopId, @Param("goodName") String goodName,
                                    @Param("goodType") String goodType, @Param("status") String status, @Param("searchMode") String searchMode,
                                    @Param("startTime") Date startTime, @Param("endTime") Date endTime, RowBounds rowBounds);

    List<GoodRespVO> searchGoodsFrontByShopId(@Param("shopId") String shopId);

    List<GoodRespVO> searchGoodsFrontByShopIdPid(@Param("shopId") String shopId);

    List<GoodRespVO> searchGoodsByType(@Param("shopId") String shopId, @Param("goodType") String goodType, @Param("status") String status, RowBounds rowBounds);

    int getGoodByShopIdCount(@Param("shopId") String shopId);

    List<GoodEntity> getGoodByShopId(@Param("shopId") String shopId, @Param("goodType") String goodType, @Param("sort") String sort, @Param("offset") int offset,
                                     @Param("pageSize") int pageSize);

    List<GoodEntity> listGoodByIds(@Param("goodIds") List<String> goodIds, @Param("userId") String userId,
                                   @Param("status") String status);

    List<GoodEntity> listGoodByGoodIdsAndShopIds(@Param("goodIds") List<String> goodIds, @Param("shopIds") List<String> shopIds, @Param("userId") String userId,
                                                 @Param("status") String status);

    List<AdResVO> getAdResVOList();

    // 添加修改商品
    int addGoodSelective(GoodEntity goodEntity);

    int updateGoodSelective(GoodEntity goodEntity);

    int updateGoodStatus(@Param("goodId") String goodId, @Param("status") String status);

    // 更新库存
    int updateStock(@Param("id") String id, @Param("num") int num, @Param("stock") int stock);

    int addStock(@Param("id") String id, @Param("num") int num);

    int reduceStock(@Param("id") String id, @Param("num") int num);

    // 更新销量
    int updateSales(@Param("id") String id, @Param("num") int num);

    // 更新销量
    int updateSalesCenter(@Param("shopId") String shopId, @Param("goodId") String goodId, @Param("num") int num);

    int modifyStatus(@Param("shopId") String shopId, @Param("goodId") String goodId,
                     @Param("currentStatus") String currentStatus, @Param("targetStatus") String targetStatus,
                     @Param("auditTime") Date auditTime);

    // 逻辑删除good
    int delGoods(@Param("goodId") String goodId);

    // 根据店铺id和商品状态统计商品数量
    int countByShopIdAndStatus(@Param("shopId") String shopId, @Param("status") String status);

    //添加商品支付信息
    int addGoodPay(GoodPayVO goodPayVO);

    //删除商品支付信息
    void delGoodPay(@Param("goodId") String goodId);

    List<GoodPayReqVO> getGoodPayByGoodId(@Param("goodId") String goodId);

    //获取商品支付信息列表
    List<GoodPayReqVO> getGoodPayByGoodIdList(@Param("goodId") String goodId);

    //根据支付方式Id获取商品支付方式信息
    GoodPayReqVO getGoodPayByGoodPayId(@Param("goodPayId") String goodPayId);

    GoodEntity getGoodEntityByShopIdAndGoodNo(@Param("shopId") String shopId, @Param("goodNo") String goodNo);

    GoodEntity getGoodById(@Param("id") String id);

    GoodEntity getGoodByGoodIdAndShopId(@Param("goodId") String goodId, @Param("shopId") String shopId);

    //店铺后台使用
    List<GoodRespVO> searchGoodByShopId(@Param("shopId") String shopId, @Param("goodName") String goodName,
                                        @Param("goodType") String goodType,
                                        @Param("payTypes") String payTypes,
                                        @Param("searchMode") int searchMode,
                                        @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("status") String status, RowBounds rowBounds);


    List<GoodEntity> getGoodsByKtList(@Param("shopId") String shopId);

    /**
     * 调整商品分类(批量)
     *
     * @param id      商品分类id
     * @param goodIds 商品id集合：使用,分隔"
     * @return
     */
    int adjustGoodType(@Param("id") String id, @Param("goodIds") List<String> goodIds);

    /**
     * 商品上架&下架
     *
     * @param goodIds 商品id集合：使用,分隔"
     * @param status  1上架，3下架
     */
    int editGoodState(@Param("goodIds") List<String> goodIds, @Param("status") String status);

    /**
     * 根据商品ID，查询该商品是否设置过支付方式
     *
     * @param goodId
     * @return
     */
    int queryPayTypeById(@Param("goodId") String goodId);

    /**
     * 商品支付方式修改
     *
     * @param payTypeId 支付方式id:1余额,2余额+优惠券
     * @param goodId    商品id
     */
    void updateGoodPayType(@Param("payTypeId") String payTypeId, @Param("goodId") String goodId);

    /********************以下为Admin商品增删改查接口*****6.28修改********/
    /**
     * 商品查询接口
     *
     * @param shopId
     * @param goodName
     * @param goodType
     * @param status
     * @param searchMode
     * @param rowBounds
     * @return
     */
    List<GoodRespVO> searchGoodss(@Param("shopId") String shopId,
                                  @Param("goodName") String goodName,
                                  @Param("goodType") String goodType,
                                  @Param("status") String status,
                                  @Param("searchMode") int searchMode, RowBounds rowBounds);

    /********************以上为Admin商品增删改查接口*****6.28修改********/


    int getGoodByShopIdAndGoodStatusCount(@Param("shopId") String shopId);

    /**
     * 修改商品专用--根据商品id查询商品详情
     *
     * @param goodId
     * @return
     */
    GoodEntity getGoodByGoodId(@Param("goodId") String goodId);

    /**
     * 项目初始化使用
     *
     * @param shopId
     * @param goodType
     * @param status
     * @return
     */
    List<GoodRespVO> initSearchGoodsByType(@Param("shopId") String shopId);

    /**
     * 获取指定商品的销量(web)
     *
     * @param shopId
     * @param goodId
     * @param
     * @return
     */
    int getShopGoodSales(@Param("shopId") String shopId, @Param("goodId") String goodId);

    /**
     * 重第三行查詢返回47行数据
     *
     * @return
     */
    List<GoodRespVO> initSearchGoodsByTypeAnd47(@Param("shopId") String shopId);

    /**
     * 从50行数据开始
     *
     * @return
     */
    List<GoodRespVO> initSearchGoodsStartFifty(@Param("shopId") String shopId, @Param("goodType") String goodType, RowBounds rowBounds);

    List<GoodRespVO> defaultSearchGoodsByType(@Param("shopId") String shopId, @Param("goodType") String goodType, @Param("status") String status);

    /**
     * 根据商品ID查询不等于商品ID的商品
     *
     * @param goodIds
     * @return
     */
    List<GoodRespVO> getGoodListByIds(@Param("goodIds") List<String> goodIds);


    void delGoodPayByGoodId(@Param("goodId") String goodId, @Param("payTypeId") String payTypeId);
}
