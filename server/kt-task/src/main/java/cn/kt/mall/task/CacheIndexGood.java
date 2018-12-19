package cn.kt.mall.task;

import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.shop.good.mapper.GoodDAO;
import cn.kt.mall.shop.good.service.GoodService;
import cn.kt.mall.shop.good.vo.GoodRespVO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class CacheIndexGood {
    private static Logger logger = LoggerFactory.getLogger(CacheIndexGood.class);
    @Autowired
    GoodDAO goodDAO;

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserDAO userDAO;
    @Autowired
    ShopDAO shopDAO;
    @Autowired
    GoodService goodService;

    private RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();

    private static Thread releaseThread = null;

    @Scheduled(cron = "0 0/3 * * * ?")
    public void init() {

        if (releaseThread != null && releaseThread.isAlive()) {
            logger.info("-------------run--------------------------");
            logger.info("-------------run--------------------------");
            logger.info("-------------run--------------------------");
            logger.info("-------------run--------------------------");
            return;
        }

        releaseThread = new Thread() {
            public void run() {
                //查询站长
                List<String> shopId = userDAO.getShopIdByUser();
                //处理关闭得店铺
                List<String> shopList = shopDAO.getCloseShop();
                if (shopList != null) {
                    for (String shop : shopList) {
                        Boolean bool = redisTemplate.opsForHash().hasKey("home", shop);
                        if (bool) {
                            redisTemplate.opsForHash().delete("home", shop);
                        }
                    }
                }
                //处理 正常得店铺
                if (shopId != null) {
                    int pageSize = 1000;
                    int TotalPage = (shopId.size() % pageSize == 0 ? shopId.size() / pageSize : shopId.size() / pageSize + 1);

                    for (int i = 0; i < TotalPage; i++) {
                        int fromIndex = i * pageSize;
                        //如果总数少于PAGE_SIZE,为了防止数组越界,toIndex直接使用totalCount即可
                        int toIndex = Math.min(shopId.size(), (i + 1) * pageSize);
                        GoodThread goodThread = new GoodThread(userDAO, redisTemplate, goodDAO, shopDAO, serializer, goodService, shopId.subList(fromIndex, toIndex));
                        new Thread(goodThread).start();
                    }
                }
            }
        };
        releaseThread.start();

    }

    public static class GoodThread implements Runnable {
        private UserDAO userDAO;
        private RedisTemplate redisTemplate;
        private GoodDAO goodDAO;
        private ShopDAO shopDAO;
        private RedisSerializer<Object> serializer;
        private GoodService goodService;
        List<String> shopId;


        public GoodThread(UserDAO userDAO, RedisTemplate redisTemplate, GoodDAO goodDAO, ShopDAO shopDAO, RedisSerializer<Object> serializer, GoodService goodService, List<String> shopId) {
            this.userDAO = userDAO;
            this.redisTemplate = redisTemplate;
            this.goodDAO = goodDAO;
            this.shopDAO = shopDAO;
            this.serializer = serializer;
            this.goodService = goodService;
            this.shopId = shopId;
        }

        @Override
        public void run() {

            try {
                works(this.shopId);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }


        /**
         * 2018-07-25 zkp
         */
        public void works(List<String> shopIds) {
            Date now = new Date();
            if (!shopIds.isEmpty() && shopIds.size() > 0) {
                //处理首页
                this.initByshopPage(shopIds);
            }
            Date endTime = new Date();
            long minute = (endTime.getTime() - now.getTime()) / 1000;
            logger.info("------------------------------CacheIndexGood-begin---------------------------");
            logger.info("---------------完成时长：" + minute + "秒--------------------");
            logger.info("------------------------------CacheIndexGood-end-----------------------------------");
        }

        private void initByshopPage(List<String> shops) {
            Map<String, Object> param = new HashMap<String, Object>();
            //查询全部商品
            List<GoodRespVO> goodRespVOList = goodDAO.getGoodListByIds(null);
            for (String shopId : shops) {
                List<GoodRespVO> goodRespList = new ArrayList<>();
                //每个站长 返回3个标识
                List<GoodRespVO> searchGoodsByType = goodDAO.initSearchGoodsByType(shopId);
                //去掉查询的前三个商品 去重复
                for (GoodRespVO vo : goodRespVOList) {
                    boolean isExcet = false;
                    for (GoodRespVO searchGood : searchGoodsByType) {

                        if (vo.getId().equals(searchGood.getId())) {
                            isExcet = true;
                        }
                    }
                    if (!isExcet) {
                        goodRespList.add(vo);
                    }
                }
                //查询剩余商品 随机分配店铺
                if (searchGoodsByType.size() > 0 && searchGoodsByType != null) {
                    //查询剩余商品
                    if (goodRespList != null && goodRespList.size() > 0) {
                        List<ShopEntity> shopIds = shopDAO.randShop(goodRespList.size());
                        int i = 0;
                        for (GoodRespVO good : goodRespList) {
                            //随机一个商铺
                            ShopEntity shop = shopIds.get(i);
                            good.setShopRank(shop.getShopRank());
                            good.setShopLevel(shop.getShopLevel());
                            good.setShopShopId(shop.getId());
                            good.setShopName(shop.getShopName());
                            i++;
                        }
                        searchGoodsByType.addAll(goodRespList);
                    }
                    if (!searchGoodsByType.isEmpty() && searchGoodsByType.size() > 0) {
                        param.put(shopId, serializer.serialize(searchGoodsByType));
                    }
                }
            }


            //没有站长的直接随机分配
            List<GoodRespVO> noShopGood = goodDAO.getGoodListByIds(null);
            List<ShopEntity> shopIds = shopDAO.randShop(noShopGood.size());
            for (GoodRespVO good : noShopGood) {
                //随机一个商铺
                for (ShopEntity shop : shopIds) {
                    good.setShopRank(shop.getShopRank());
                    good.setShopLevel(shop.getShopLevel());
                    good.setShopShopId(shop.getId());
                    good.setShopName(shop.getShopName());
                }

            }
            if (!goodRespVOList.isEmpty() && goodRespVOList.size() > 0) {
                param.put("noShop", serializer.serialize(goodRespVOList));
            }
            if (param != null) {
                redisTemplate.opsForHash().putAll("home", param);
            }
        }
    }


}
