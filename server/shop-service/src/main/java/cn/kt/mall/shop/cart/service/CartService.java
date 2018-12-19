package cn.kt.mall.shop.cart.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.shop.good.entity.GoodCouponCenterEntity;
import cn.kt.mall.shop.good.mapper.GoodCouponCenterDao;
import cn.kt.mall.shop.good.vo.GoodPayReqVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.shop.cart.entity.CartEntity;
import cn.kt.mall.shop.cart.mapper.CartDAO;
import cn.kt.mall.shop.cart.vo.BuyGoodVO;
import cn.kt.mall.shop.cart.vo.CartBatchVO;
import cn.kt.mall.shop.cart.vo.CartGoodsItem;
import cn.kt.mall.shop.cart.vo.CartVO;
import cn.kt.mall.shop.cart.vo.UpdateCartVO;
import cn.kt.mall.shop.good.entity.GoodEntity;
import cn.kt.mall.shop.good.mapper.GoodDAO;
import cn.kt.mall.shop.good.mapper.GoodPropertyDAO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;

@Service
public class CartService {

    @Autowired
    ShopDAO shopDAO;
    @Autowired
    GoodDAO goodDAO;
    @Autowired
    CartDAO cartDAO;
    @Autowired
    GoodPropertyDAO goodPropertyDAO;
    @Autowired
    private GoodCouponCenterDao goodCouponCenterDao;
    @Autowired
    private SysSettingsService sysSettingsService;
    @Autowired
    private UserAssetService userAssetService;

    //添加购物车
    @Transactional
    public void addCart(String userId, BuyGoodVO buyGoodVO,String pidFlag){
        ShopEntity shopEntity = shopDAO.getShopEntityByShopId(buyGoodVO.getShopId());
        A.check(shopEntity==null, "店铺非法或不存在");
        //商铺信用金低于1000提示库存不足
        //BigDecimal shopPointBase = sysSettingsService.getShopPointBase();
        GoodEntity goodEntity = goodDAO.getGoodEntityByGoodIdAndShopId(buyGoodVO.getGoodId(),buyGoodVO.getShopId());
        A.check(goodEntity==null || !buyGoodVO.getShopId().equals(goodEntity.getShopShopId()), "商品非法或不存在");
        
        List<CartEntity> list = cartDAO.getByShopIdAndUserId(buyGoodVO.getShopId(), buyGoodVO.getGoodId(), userId);
        if(CollectionUtils.isEmpty(list)) {
            //A.check(goodEntity.getGoodStock() < buyGoodVO.getBuyNum(), "商品库存不够");
            //A.check(userAssetService.getUserAssetByCurrency(shopEntity.getUserId(), AssetType.CREDIT.getStrType()).getAvailableBalance().compareTo(shopPointBase) < 0, "商品库存不足");
            CartEntity cartEntity = new CartEntity();
            cartEntity.setId(IDUtil.getUUID());
            cartEntity.setShopId(buyGoodVO.getShopId());
            cartEntity.setGoodId(buyGoodVO.getGoodId());
            cartEntity.setBuyPrice(goodEntity.getGoodPrice());
            cartEntity.setRaisePrice(goodEntity.getRaisePrice());
            cartEntity.setBuyNum(buyGoodVO.getBuyNum());
            cartEntity.setPidFlag(pidFlag);
            cartEntity.setBuyUserId(userId);
            cartDAO.addCart(cartEntity);
        }else {
        	int buyNum = buyGoodVO.getBuyNum();
        	String[] ids = new String[list.size()];
        	for (int i = 0; i < list.size(); i++) {
        		ids[i] = list.get(i).getId();
        		buyNum += list.get(i).getBuyNum();
            }
        	//A.check(goodEntity.getGoodStock() < buyNum, "商品库存不够");
           // A.check(userAssetService.getUserAssetByCurrency(shopEntity.getUserId(), AssetType.CREDIT.getStrType()).getAvailableBalance().compareTo(shopPointBase) < 0, "商品库存不足");
            CartEntity cartEntity = new CartEntity();
            cartEntity.setId(IDUtil.getUUID());
            cartEntity.setShopId(buyGoodVO.getShopId());
            cartEntity.setGoodId(buyGoodVO.getGoodId());
            cartEntity.setBuyPrice(goodEntity.getGoodPrice());
            cartEntity.setBuyNum(buyNum);
            cartEntity.setBuyUserId(userId);
            cartEntity.setPidFlag(pidFlag);
            cartDAO.delCartByBatch(userId, ids);
            cartDAO.addCart(cartEntity);
        }
    }

    //删除购物车
    public void delCart(String userId, String id){
        cartDAO.delCart(id);
    }

    //批量删除购物车
    @Transactional
    public void delCartByBatch(String userId, String[] ids){
        for (int i = 0; i < ids.length; i++) {
            cartDAO.delCart(ids[i]);
        }
    }

    //购物车列表
    public List<CartVO> myCart(String userId){
        List<CartVO> list = new ArrayList<>();
        List<CartEntity> ll = cartDAO.getCartByBuyUserId(userId);
        if(!CollectionUtils.isEmpty(ll)){
            Set<String> shopIdSet = new HashSet<>();
            Set<String> goodIdSet = new HashSet<>();
            for (CartEntity cartEntity:ll) {
                shopIdSet.add(cartEntity.getShopId());
                goodIdSet.add(cartEntity.getGoodId());
            }
            List<String> shopIds = new ArrayList<>();
            shopIds.addAll(shopIdSet);
            List<String> goodIds = new ArrayList<>();
            goodIds.addAll(goodIdSet);
            List<ShopEntity> shopList = shopDAO.listShopByIds(shopIds,null,null);
            //List<GoodEntity> goodList = goodDAO.listGoodByIds(goodIds,null,null);
            List<GoodEntity> goodList = goodDAO.listGoodByGoodIdsAndShopIds(goodIds,shopIds,null,null);
            List<GoodCouponCenterEntity> goodCouponCenterEntityList =  goodCouponCenterDao.getGoodCollectEntityByGoodIds(goodIds);
            List<GoodCouponCenterEntity> couponlist;
            for(GoodEntity goodEntity : goodList){
                couponlist = new ArrayList<GoodCouponCenterEntity>();
                for(GoodCouponCenterEntity goodCouponCenterEntity:goodCouponCenterEntityList) {
                    if (goodCouponCenterEntity.getGoodId().equals(goodEntity.getId())) {
                        couponlist.add(goodCouponCenterEntity);
                    }
                }
                goodEntity.setGoodCouponCenterEntityList(couponlist);
               //支付方式列表
                List<GoodPayReqVO> goodPayEntityList = goodDAO.getGoodPayByGoodIdList(goodEntity.getId());
                goodEntity.setGoodPayEntityList(goodPayEntityList);
            }

            buildCart(list,shopList,goodList,ll);
        }

        return list;
    }

    private void buildCart(List<CartVO> carList,List<ShopEntity> shopList,List<GoodEntity> goodList,List<CartEntity> cartList){
    /*    for (ShopEntity shopEntity:shopList) {
            CartVO cartVO = new CartVO();
            BeanUtils.copyProperties(shopEntity,cartVO);
            List<CartGoodsItem> cartGoodList = new ArrayList<>();
            for (GoodEntity goodEntity:goodList) {
                if(goodEntity.getShopId().equals(shopEntity.getId())){
                    for (CartEntity cartEntity:cartList) {
                        if(cartEntity.getGoodId().equals(goodEntity.getId())){
                            if(goodEntity.getDelFlag().equals("1")){
                                goodEntity.setInvalidFlag("1");
                            }else if(goodEntity.getStatus().equals("3") ){
                                goodEntity.setInvalidFlag("1");
                            }else{
                                goodEntity.setInvalidFlag("0");
                            }
                            CartGoodsItem cartGoodsItem = new CartGoodsItem();
                            BeanUtils.copyProperties(goodEntity,cartGoodsItem);
                            cartGoodsItem.setCartId(cartEntity.getId());
                            cartGoodsItem.setBuyNum(cartEntity.getBuyNum());
                            cartGoodsItem.setPidFlag(cartEntity.getPidFlag());
                            cartGoodList.add(cartGoodsItem);
                        }
                    }
                }
            }
            cartVO.setGoodsData(cartGoodList);
            carList.add(cartVO);
        }*/
        for (ShopEntity shopEntity:shopList) {
            CartVO cartVO = new CartVO();
            BeanUtils.copyProperties(shopEntity,cartVO);
            List<CartGoodsItem> cartGoodList = new ArrayList<>();
            for (GoodEntity goodEntity:goodList) {
                if(goodEntity.getShopShopId().equals(shopEntity.getId())){
                    for (CartEntity cartEntity:cartList) {
                        if(cartEntity.getGoodId().equals(goodEntity.getId()) && cartEntity.getShopId().equals(goodEntity.getShopShopId())){
                           /* if(goodEntity.getDelFlag().equals("1")){
                                goodEntity.setInvalidFlag("1");
                            }else if(goodEntity.getStatus().equals("3") ){
                                goodEntity.setInvalidFlag("1");
                            }else{
                                goodEntity.setInvalidFlag("0");
                            }*/
                            if(goodEntity.getGoodStatus().equals("3")){
                                goodEntity.setInvalidFlag("1");
                            }else if(goodEntity.getGoodStatus().equals("4") ){
                                goodEntity.setInvalidFlag("1");
                            }else{
                                goodEntity.setInvalidFlag("0");
                            }
                            CartGoodsItem cartGoodsItem = new CartGoodsItem();
                            BeanUtils.copyProperties(goodEntity,cartGoodsItem);
                            cartGoodsItem.setCartId(cartEntity.getId());
                            cartGoodsItem.setBuyNum(cartEntity.getBuyNum());
                            cartGoodsItem.setPidFlag(cartEntity.getPidFlag());
                            cartGoodList.add(cartGoodsItem);
                        }
                    }
                }
            }
            cartVO.setGoodsData(cartGoodList);
            carList.add(cartVO);
        }

    }

    //批量修改购物车
    @Transactional
    public void updCartByBatch(UpdateCartVO updateCartVO) {
        A.check(updateCartVO == null || updateCartVO.getCarts() == null, "入参异常");
        for (CartBatchVO cartBatchVO : updateCartVO.getCarts()) {
            CartEntity cartEntity = new CartEntity();
            BeanUtils.copyProperties(cartBatchVO, cartEntity);
            cartDAO.updateCart(cartEntity);
        }
    }
    //查询购物车内商品数量
    public int getCartGoodsCountByUserId(String userId) {
        return cartDAO.getCartGoodsCountByUserId(userId);

    }
}
