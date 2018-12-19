package cn.kt.mall.offline.service;

import cn.kt.mall.offline.entity.ConsumeEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/21.
 */
public interface RecommendService {

    /**
     * 获取我的推荐列表
     *
     * @param userId
     * @return
     */
    Map myRecommend(String userId, Integer flag);
}
