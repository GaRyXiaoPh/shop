package cn.kt.mall.offline.dao;

import cn.kt.mall.offline.entity.OfflineUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018/4/28.
 */
@Mapper
public interface OfflineUserDAO {

    /**
     * 根据手机号查询商圈商户信息
     *
     * @param mobile
     * @return
     */
    OfflineUserEntity queryByMobile(@Param("mobile") String mobile);

    /**
     * 获取线下的店铺id
     *
     * @param userId
     * @return
     */
    String getShopId(@Param("userId") String userId);

}
