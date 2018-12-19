package cn.kt.mall.offline.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.wallet.service.LemRateService;
import cn.kt.mall.offline.dao.CommentDAO;
import cn.kt.mall.offline.dao.ShopInfoDAO;
import cn.kt.mall.offline.entity.GoodEntity;
import cn.kt.mall.offline.entity.ShopInfoEntity;
import cn.kt.mall.offline.service.ShopInfoService;
import cn.kt.mall.offline.vo.CommentInfo;
import cn.kt.mall.shop.collect.mapper.CollectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/5/11.
 */
@Service("shopInfoService")
public class ShopInfoServiceImpl implements ShopInfoService {

    @Autowired
    private ShopInfoDAO shopInfoDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private LemRateService lemRateService;

    @Autowired
    private CollectDAO collectDAO;


    @Override
    public ShopInfoEntity selectShopInfo(String shopId) {
        //获取店铺基础信息
        ShopInfoEntity shopInfoEntity = shopInfoDAO.selectShopInfo(shopId);
        A.check(shopInfoEntity == null,"该店铺不存在");
        //获取店铺的商品列表
        List<GoodEntity> goodList = shopInfoDAO.selectGoodInfo(shopInfoEntity.getUserId());
        shopInfoEntity.setGoodsList(goodList);

        //获取评论列表
        List<CommentInfo> commentList = commentDAO.selectComment(shopId);
        shopInfoEntity.setCommentList(commentList);
        //莱姆比例
        shopInfoEntity.setRate(lemRateService.getLemRate());
        //判断该店铺是否被当前用户收藏
        if(collectDAO.getShopCollectEntity(SubjectUtil.getCurrent().getId(),shopInfoEntity.getShopId())==null){
           shopInfoEntity.setFlag(false);
        }else{
            shopInfoEntity.setFlag(true);
        }
        return shopInfoEntity;
    }
}
