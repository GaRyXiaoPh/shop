package cn.kt.mall.offline.service;


import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.offline.entity.GoodEntity;
import cn.kt.mall.offline.vo.GoodUpVO;
import cn.kt.mall.offline.vo.GoodVO;
import cn.kt.mall.offline.vo.OffGoodVO;

import java.util.List;


/**
 * 商品管理接口
 *
 * Created by chenhong on 2018/4/26.
 */
public interface GoodsService {
    /**
     * 添加商品信息
     *
     * @param goodEntity
     */
    void addGoods(GoodVO goodEntity,String userId);

    /**
     * 修改商品信息
     *
     * @param goodUpVO
     */
    void updateGood(GoodUpVO goodUpVO);

    /**
     * 修改商品信息
     *
     * @param goodEntity
     */
    int updateGoodInfo(GoodEntity goodEntity);

    /**
     * 查询商户产品列表
     *
     * @param userId
     * @return
     */
    PageVO<GoodEntity> getGoodsList(String userId, Integer pageNo, Integer pageSize, Integer status, String name);

    /**
     * 查询商品详情
     *
     * @param id
     * @return
     */
    GoodEntity getGoodDetail(String id);

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    int delGood(String id);

    /**
     * 获取线下商品管理列表
     *
     * @param offGoodVO
     * @return
     */
    PageVO<GoodEntity> getOffGoods(OffGoodVO offGoodVO);
}
