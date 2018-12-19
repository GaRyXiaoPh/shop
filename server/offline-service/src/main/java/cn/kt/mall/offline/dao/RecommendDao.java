package cn.kt.mall.offline.dao;

import cn.kt.mall.offline.entity.ConsumeEntity;
import cn.kt.mall.offline.entity.RecommendEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/5/21.
 */
@Mapper
public interface RecommendDao {

    /**
     * 获取用户的推荐列表
     *
     * @param flag
     * @return
     */
    List<RecommendEntity> getRecommendList(@Param("flag") Integer flag,@Param("userId") String userId);

    /**
     * 查询用户的推荐奖励
     *
     * @param userId
     * @return
     */
    double getRecommendReward(@Param("userId") String userId);

}
