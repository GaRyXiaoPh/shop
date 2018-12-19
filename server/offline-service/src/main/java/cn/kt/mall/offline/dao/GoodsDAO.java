package cn.kt.mall.offline.dao;

import cn.kt.mall.offline.entity.DataEntity;
import cn.kt.mall.offline.entity.GoodEntity;
import cn.kt.mall.offline.entity.PicEntity;
import cn.kt.mall.offline.vo.GoodUpVO;
import cn.kt.mall.offline.vo.OffGoodVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 商圈商品管理接口
 *
 * Created by chenhong on 2018/4/26.
 */
@Mapper
public interface GoodsDAO {

    /**
     * 添加商品信息
     *
     * @param goodEntity
     */
    int addGoods(GoodEntity goodEntity);

    /**
     * 添加商品图片信息
     *
     * @param goodEntity
     */
    int addGoodPic(GoodEntity goodEntity);

    /**
     * 更新商品信息
     *
     * @param goodEntity
     * @return
     */
    int updateGoodInfo(GoodEntity goodEntity);

    /**
     * 删除商品信息
     *
     * @param id
     * @return
     */
    int delGood(@Param("id") String id);

    /**
     * 删除产品图片
     *
     * @param id
     * @return
     */
    int deleteGoodPic(@Param("id") String id);


    /**
     * 查询商户产品列表
     * @param userId
     * @param status
     * @param name
     * @return
     */
    List<GoodEntity> getGoodsList(@Param("userId") String userId,
                                  @Param("status") Integer status,
                                  @Param("name") String  name,
                                  @Param("offset") Integer offset,
                                  @Param("pageSize") Integer pageSize);

    /**
     * 查询商户商品列表的长度
     * @param userId
     * @param status
     * @param name
     * @return
     */
    int getGoodsListCount(@Param("userId") String userId,
                          @Param("status") Integer status,
                          @Param("name") String  name);

    /**
     * 查询商品详情
     *
     * @param id
     * @return
     */
    GoodEntity getGoodDetail(@Param("id") String id);

    /**
     * 获取图片列表
     *
     * @param id
     * @param type
     * @return
     */
    List<PicEntity> getPicList(@Param("id") String id, @Param("type") Integer type);

    /**
     * 获取商品数量
     *
     * @param userId
     * @return
     */
    List<DataEntity> getGoodNum(@Param("userId") String userId);

    /**
     * 获取线下商品管理列表
     *
     * @param offGoodVO
     * @return
     */
    List<GoodEntity> getOffGoods(OffGoodVO offGoodVO);

    /**
     * 查询线下商品列表长度
     *
     * @return
     */
    int getOffGoodsCount();

	int countByStatus(@Param("status") Integer status);

}
