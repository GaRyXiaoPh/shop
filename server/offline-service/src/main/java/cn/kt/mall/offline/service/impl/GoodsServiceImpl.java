package cn.kt.mall.offline.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.offline.dao.GoodsDAO;
import cn.kt.mall.offline.dao.OfflineShopDAO;
import cn.kt.mall.offline.dao.OfflineUserDAO;
import cn.kt.mall.offline.entity.GoodEntity;
import cn.kt.mall.offline.entity.PicEntity;
import cn.kt.mall.offline.service.GoodsService;
import cn.kt.mall.offline.vo.GoodUpVO;
import cn.kt.mall.offline.vo.GoodVO;
import cn.kt.mall.offline.vo.OffGoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 产品管理实现类
 * Created by chenhong on 2018/4/26.
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDAO goodsDAO;

    @Autowired
    private OfflineShopDAO offlineShopDAO;

    @Autowired
    private OfflineUserDAO offlineUserDAO;

    /**
     * 添加商品信息
     *
     * @param goodVO
     */
    @Transactional
    @Override
    @ShopAuth
    public void addGoods(GoodVO goodVO,String userId) {
        GoodEntity goodEntity = new GoodEntity();
        //商品id
        goodEntity.setId(IDUtil.getUUID());
        //用户id
        goodEntity.setUserId(userId);
        //店铺id
        goodEntity.setShopId(offlineUserDAO.getShopId(userId));
        //商品名称
        goodEntity.setName(goodVO.getName());
        //商品价格
        goodEntity.setPrice(goodVO.getPrice());

        /**添加商品信息*/
        int count = goodsDAO.addGoods(goodEntity);
        A.check(count<1,"添加商品信息失败");

        /**添加商品图片信息*/
        String[] goodsPic = goodVO.getGoodsPic();
        for(int i=0;i<goodsPic.length;i++){
            //图片类型
            goodEntity.setType(0);
            //图片地址
            goodEntity.setUrl(goodsPic[i]);
            if(i==0){
                goodEntity.setIsMain(0);
            }else{
                goodEntity.setIsMain(1);
            }
            count = goodsDAO.addGoodPic(goodEntity);
            A.check(count<1,"添加商品图片信息失败");
        }
        /**添加商品详细图片信息*/
        String[] goodDetailPic = goodVO.getGoodDetailPic();
        for(int j=0;j<goodDetailPic.length;j++){
            //图片类型
            goodEntity.setType(1);
            //图片路径
            goodEntity.setUrl(goodDetailPic[j]);
            if(j==0){
                goodEntity.setIsMain(0);
            }else{
                goodEntity.setIsMain(1);
            }
            count= goodsDAO.addGoodPic(goodEntity);
            A.check(count<1,"添加商品详细图片信息失败");
        }
    }

    @Transactional
    public void updateGood(GoodUpVO goodUpVO) {
        //更新商品信息
        GoodEntity goodEntity = new GoodEntity();
        goodEntity.setName(goodUpVO.getName());
        goodEntity.setPrice(goodUpVO.getPrice());
        goodEntity.setId(goodUpVO.getId());
        //修改商品信息
        int  count = goodsDAO.updateGoodInfo(goodEntity);
        A.check(count<1,"修改商品信息失败");
        //删除商品图片信息
        count = goodsDAO.deleteGoodPic(goodUpVO.getId());
        A.check(count<1,"删除商品图片信息失败");
        //添加商品图片信息
        String[] goodsPic = goodUpVO.getGoodsPic();
        for(int i=0;i<goodsPic.length;i++){
            goodEntity.setType(0);
            goodEntity.setUrl(goodsPic[i]);
            if(i==0){
                goodEntity.setIsMain(0);
            }else{
                goodEntity.setIsMain(1);
            }
            count = goodsDAO.addGoodPic(goodEntity);
            A.check(count<1,"添加商品图片失败");
        }

        String[] goodDetailPic = goodUpVO.getGoodDetailPic();
        for(int j=0;j<goodDetailPic.length;j++){
            goodEntity.setType(1);
            goodEntity.setUrl(goodDetailPic[j]);
            if(j==0){
                goodEntity.setIsMain(0);
            }else {
                goodEntity.setIsMain(1);
            }
            count = goodsDAO.addGoodPic(goodEntity);
            A.check(count<1,"添加商品详细图片失败");

        }

    }

    @Override
    public int updateGoodInfo(GoodEntity goodEntity) {
        return  goodsDAO.updateGoodInfo(goodEntity);
    }

    @Override
    public PageVO<GoodEntity> getGoodsList(String userId, Integer pageNo, Integer pageSize, Integer status, String name) {
        int srcPageNo = pageNo;
        if (pageNo>=1) pageNo = pageNo-1;
        int offset = pageNo*pageSize;
        int count = goodsDAO.getGoodsListCount(userId,status,name);
        List<GoodEntity> list = goodsDAO.getGoodsList(userId,status,name,offset,pageSize);
        return new PageVO<>(srcPageNo, pageSize, count, list);
    }

    @Override
    public GoodEntity getGoodDetail(String id) {
        GoodEntity goodEntity = goodsDAO.getGoodDetail(id);
        List<PicEntity> picList = goodsDAO.getPicList(id,0);
        List<PicEntity> picDetailList = goodsDAO.getPicList(id,1);
        goodEntity.setPicList(picList);
        goodEntity.setPicDetailList(picDetailList);
        return goodEntity;
    }

    @Override
    @Transactional
    public int delGood(String id) {
        //删除商品信息
        int count = goodsDAO.delGood(id);
        A.check(count<1,"删除商品信息失败");
        //删除商品图片信息
        count = goodsDAO.deleteGoodPic(id);
        A.check(count<1,"删除商品图片信息失败");
        return count;
    }

    @Override
   public PageVO<GoodEntity> getOffGoods(OffGoodVO offGoodVO) {
        int pageNo = offGoodVO.getPageNo();
        int pageSize = offGoodVO.getPageSize();
        int srcPageNo = pageNo;
        if (pageNo>=1) pageNo = pageNo-1;
        int offset = pageNo*pageSize;
        int count = goodsDAO.getOffGoodsCount();
        offGoodVO.setPageNo(offset);
        offGoodVO.setPageSize(pageSize);
        List<GoodEntity> list = goodsDAO.getOffGoods(offGoodVO);
        return new PageVO<>(srcPageNo, pageSize, count, list);
    }

}
