package cn.kt.mall.offline.service.impl;

import cn.kt.mall.offline.dao.RecommendDao;
import cn.kt.mall.offline.entity.ConsumeEntity;
import cn.kt.mall.offline.entity.RecommendEntity;
import cn.kt.mall.offline.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/21.
 */
@Service("recommendService")
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private RecommendDao recommendDao;

    @Override
    public Map myRecommend(String userId, Integer flag) {
        //获取推荐列表
        List<RecommendEntity> list = recommendDao.getRecommendList(flag,userId);
        Map map = new HashMap();
        map.put("recommendList",list);
        map.put("count",list.size());
        map.put("reward",recommendDao.getRecommendReward(userId));
        return map;
    }
}
