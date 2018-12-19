package cn.kt.mall.management.admin.service;

import cn.kt.mall.shop.good.vo.GoodTypeVO;
import java.util.List;

public interface ShopGoodService {

    /**
     * 调整商品分类(批量)
     * @param id 商品分类id
     * @param goodIds 商品id集合：使用,分隔"
     * @return
     */
    void adjustGoodType(String id, String goodIds);

    /**
     * 商品分类列表
     * @param goodTypeName 商品分类名称
     * @return
     */
    List<GoodTypeVO> goodTypeList(String goodTypeName);

    /**
     * 添加商品分类
     * @param goodTypeName 商品分类名称
     */
    void addGoodType(String goodTypeName);

    /**
     * 修改商品分类
     * @param goodTypeId 商品分类id
     * @param goodTypeName 商品分类名称
     */
    void editGoodType(String goodTypeId, String goodTypeName);

    /**
     * 商品上架
     * @param goodIds 商品id集合：使用,分隔
     */
    void goodOnline(String goodIds);

    /**
     * 商品下架
     * @param goodIds 商品id集合：使用,分隔
     */
    void goodOffline(String goodIds);

    /**
     * 商品支付方式修改(批量)
     * @param payTypeId 支付方式id:1余额,2余额+优惠券
     * @param goodIds 商品id集合：使用,分隔
     */
    void editGoodPayType(String payTypeId, String goodIds);
}
