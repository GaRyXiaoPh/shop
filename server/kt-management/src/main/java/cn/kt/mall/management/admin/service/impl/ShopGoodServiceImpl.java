package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.management.admin.service.ShopGoodService;
import cn.kt.mall.shop.good.mapper.GoodDAO;
import cn.kt.mall.shop.good.mapper.GoodShopCenterDao;
import cn.kt.mall.shop.good.mapper.ShopGoodTypeDAO;
import cn.kt.mall.shop.good.vo.GoodPayVO;
import cn.kt.mall.shop.good.vo.GoodTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.kt.mall.shop.good.constant.Constants;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ShopGoodServiceImpl implements ShopGoodService {

    @Autowired
    private GoodDAO goodDAO;
    @Autowired
    private ShopGoodTypeDAO shopGoodTypeDAO;
    @Autowired
    private GoodShopCenterDao goodShopCenterDao;

    /**
     * 调整商品分类(批量)
     * @param id 商品分类id
     * @param goodIds 商品id集合：使用,分隔"
     * @return
     */
    public void adjustGoodType(String id, String goodIds) {
        String[] goodIdArr = goodIds.split(",");
        List<String> idsList = null;
        if (goodIdArr.length > 0) {
            idsList = Arrays.asList(goodIdArr);
        }
        int updateCount = goodDAO.adjustGoodType(id, idsList);
        A.check(updateCount <= 0, "更新失败");
    }

    /**
     * 商品分类列表
     * @param goodTypeName 商品分类名称
     * @return
     */
    public List<GoodTypeVO> goodTypeList(String goodTypeName) {
        return shopGoodTypeDAO.goodTypeList(goodTypeName);
    }

    /**
     * 添加商品分类
     * @param goodTypeName 商品分类名称
     */
    public void addGoodType(String goodTypeName) {
        GoodTypeVO goodTypeVO = new GoodTypeVO();
        goodTypeVO.setId(IDUtil.getUUID());
        goodTypeVO.setName(goodTypeName);
        shopGoodTypeDAO.addGoodType(goodTypeVO);
    }

    /**
     * 修改商品分类
     * @param goodTypeId 商品分类id
     * @param goodTypeName 商品分类名称
     */
    public void editGoodType(String goodTypeId, String goodTypeName) {
        shopGoodTypeDAO.editGoodType(goodTypeId, goodTypeName);
    }

    /**
     * 商品上架
     * @param goodIds 商品id集合：使用,分隔
     */
    public void goodOnline(String goodIds) {
        A.check(null == goodIds, "上架失败");
        String[] goodIdArr = goodIds.split(",");
        List<String> idsList = null;
        if (goodIdArr.length > 0) {
            idsList = Arrays.asList(goodIdArr);
        }
        int updateCount = goodDAO.editGoodState(idsList, Constants.GoodStatus.GOOD_PASS);
        A.check(updateCount <= 0, "上架失败");
        updateShopGoodStatus(idsList, Constants.GoodStatus.GOOD_PASS);
    }

    /**
     * 商品下架
     * @param goodIds 商品id集合：使用,分隔
     */
    public void goodOffline(String goodIds) {
        A.check(null == goodIds, "下架失败");
        String[] goodIdArr = goodIds.split(",");
        List<String> idsList = null;
        if (goodIdArr.length > 0) {
            idsList = Arrays.asList(goodIdArr);
        }
        int updateCount = goodDAO.editGoodState(idsList, Constants.GoodStatus.GOOD_DOWN);
        A.check(updateCount <= 0, "下架失败");
        updateShopGoodStatus(idsList, Constants.GoodStatus.GOOD_DOWN);
    }

    // 修改店铺商品上架、下架状态
    private void updateShopGoodStatus(List<String> goodIds, String status) {
        goodShopCenterDao.batchGoodStatus(goodIds, status);
    }

    /**
     * 商品支付方式修改(批量)
     * @param payTypeId 支付方式id:1余额,2余额+优惠券
     * @param goodIds 商品id集合：使用,分隔
     */
    public void editGoodPayType(String payTypeId, String goodIds) {
        A.check((null == payTypeId || null == goodIds), "修改失败");
        String[] goodIdArr = goodIds.split(",");
        for(String goodId : goodIdArr) {
            // 查询商品是否设置支付方式
            //1.删除原支付方式
            goodDAO.delGoodPayByGoodId(goodId,payTypeId);
            //2.添加新设置的支付方式
            GoodPayVO goodPayVO = new GoodPayVO();
            goodPayVO.setId(IDUtil.getUUID());
            goodPayVO.setGoodId(goodId);
            goodPayVO.setGoodPayId(payTypeId);
            goodPayVO.setCreateTime(new Date());
            goodPayVO.setPayType("");
            goodDAO.addGoodPay(goodPayVO);
            /*int goodCount = goodDAO.queryPayTypeById(goodId);
            if(goodCount > 0) {
                // 已设置，修改产品支付方式
                goodDAO.updateGoodPayType(payTypeId, goodId);
            } else {
                // 未设置，增加产品支付方式
                GoodPayVO goodPayVO = new GoodPayVO();
                goodPayVO.setId(IDUtil.getUUID());
                goodPayVO.setGoodId(goodId);
                goodPayVO.setGoodPayId(payTypeId);
                goodPayVO.setCreateTime(new Date());
                goodPayVO.setPayType("");
                goodDAO.addGoodPay(goodPayVO);
            }*/
        }
    }
}
