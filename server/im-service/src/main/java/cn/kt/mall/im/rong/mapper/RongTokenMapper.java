package cn.kt.mall.im.rong.mapper;

import cn.kt.mall.im.rong.entity.RongTokenEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 融云令牌Mapper
 * Created by Administrator on 2017/6/16.
 */
@Mapper
public interface RongTokenMapper {

    /**
     * 获取融云令牌
     * @param userId
     * @return
     */
    RongTokenEntity getTokenByUser(@Param("userId") String userId);

    /**
     * 新增融云令牌
     * @param rongTokenEntity
     * @return
     */
    int addToken(RongTokenEntity rongTokenEntity);

    /**
     * 修改融云令牌
     * @param userId 用户Id
     * @param token 新的融云令牌
     * @return
     */
    int updateToken(@Param("userId") String userId, @Param("token") String token);

    /**
     * 修改融云令牌状态
     * @param userId 用户Id
     * @param status 状态：0 正常, 1 失效
     * @return
     */
    int updateTokenStatus(@Param("userId") String userId, @Param("status") String status);
}
