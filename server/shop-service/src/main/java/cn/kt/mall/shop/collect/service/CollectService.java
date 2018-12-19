package cn.kt.mall.shop.collect.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.shop.collect.entity.GoodCollectEntity;
import cn.kt.mall.shop.collect.entity.ShopCollectEntity;
import cn.kt.mall.shop.collect.mapper.CollectDAO;
import cn.kt.mall.shop.collect.vo.ShopCollectVO;
import cn.kt.mall.shop.good.entity.GoodEntity;
import cn.kt.mall.shop.good.mapper.GoodDAO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;

@Service
public class CollectService {

	@Autowired
	CollectDAO collectDAO;
	@Autowired
	GoodDAO goodDAO;
	@Autowired
	ShopDAO shopDAO;

	// 店铺收藏
	public void addShopCollect(String userId, String shopId) {
		// 判断是否已经收藏
		ShopCollectEntity collectEntity = collectDAO.getShopCollectEntity(userId, shopId);
		A.check(collectEntity != null, "该店铺已经收藏");
		collectDAO.addShopCollect(IDUtil.getUUID(), userId, shopId);
	}

	public void delShopCollect(String userId, String[] shopIds) {
		if (shopIds != null) {
			collectDAO.delShopCollect(userId, shopIds);
		}
	}

	public PageVO<ShopCollectVO> listCollectShop(String userId, int pageNo, int pageSize) {
		int srcPageNo = pageNo;
		if (pageNo >= 1)
			pageNo = pageNo - 1;
		int offset = pageNo * pageSize;
		int count = collectDAO.getShopCollectEntityListCount(userId);

		List<ShopCollectEntity> collectList = collectDAO.getShopCollectEntityList(userId, offset, pageSize);
		List<ShopCollectVO> shopList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(collectList)) {
			List<String> shopIds = new ArrayList<>(collectList.size());
			for (ShopCollectEntity shopCollectEntity : collectList) {
				shopIds.add(shopCollectEntity.getShopId());
			}
			shopList = shopDAO.listCollectShopByIds(shopIds);
		}
		return new PageVO<>(srcPageNo, pageSize, count, shopList);
	}

	// 商品收藏
	public void addGoodCollect(String userId, String goodId) {
		// 判断是否已经收藏
		GoodCollectEntity collectEntity = collectDAO.getGoodCollectEntity(userId, goodId);
		A.check(collectEntity != null, "该商品已经收藏");
		collectDAO.addGoodCollect(IDUtil.getUUID(), userId, goodId);
	}

	public void delGoodCollect(String userId, String[] goodIds) {
		collectDAO.delGoodCollectBatch(userId, goodIds);
	}

	public PageVO<GoodEntity> getGoodCollectEntityList(String userId, int pageNo, int pageSize) {
		int srcPageNo = pageNo;
		if (pageNo >= 1)
			pageNo = pageNo - 1;
		int offset = pageNo * pageSize;
		int count = collectDAO.getGoodCollectEntityListCount(userId);

		List<GoodEntity> list = new ArrayList<>();
		List<GoodCollectEntity> collectList = collectDAO.getGoodCollectEntityList(userId, offset, pageSize);

		if (!CollectionUtils.isEmpty(collectList)) {
			List<String> goodIds = new ArrayList<>(collectList.size());
			for (GoodCollectEntity goodCollectEntity : collectList) {
				goodIds.add(goodCollectEntity.getGoodId());
			}
			list = goodDAO.listGoodByIds(goodIds, null, null);
		}
		return new PageVO<>(srcPageNo, pageSize, count, list);
	}
}
