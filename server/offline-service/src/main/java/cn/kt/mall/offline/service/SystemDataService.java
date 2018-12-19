package cn.kt.mall.offline.service;

import java.util.Map;

/**
 * Created by Administrator on 2018/5/25.
 */
public interface SystemDataService {

    /**
     * 获取首页数据
     *
     * @param userId
     * @return
     */
    Map homePage(String userId);

    /**
     * 商家数据统计
     *
     * @param userId
     * @return
     */
    Map dataStatistics(String userId);
}
