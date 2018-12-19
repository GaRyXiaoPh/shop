package cn.kt.mall.im.friend.mapper;

import cn.kt.mall.im.friend.entity.HateUserEntity;
import cn.kt.mall.im.friend.vo.FriendshipVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 黑名单Mapper
 * Created by Administrator on 2017/7/5.
 */
@Mapper
public interface HateUserDAO {

    /**
     * 获取黑名单用户信息列表
     * @param userId 当前用户Id
     * @param hates 黑名单Ids
     * @return List<FriendshipVO>
     */
    List<FriendshipVO> getHateUserList(@Param("userId") String userId, @Param("hates") String[] hates);

    /**
     * 获取黑名单列表
     * @param userId 所属人Id
     * @return List<String> 黑名单用Id集合
     */
    String[] getHateList(@Param("userId") String userId);

    /**
     * 新增黑名单用户
     * @param hateUserEntity 黑名单用户
     */
    void addHateUser(HateUserEntity hateUserEntity);

    /**
     * 移出黑名单
     * @param userId 所属人Id
     * @param hateId 黑名单用户Id
     * @return 更新结果
     */
    int deleteHateUser(@Param("userId") String userId, @Param("hateId") String hateId);

    /**
     * 判断是否在我的黑名单中
     * 判断条件：对方在我的黑名单中
     * @param userId 所属人Id
     * @param hateId 黑名单用户Id
     * @return boolean, true 是, false 否
     */
    boolean isMyHate(@Param("userId") String userId, @Param("hateId") String hateId);

    /**
     * 判断两个用户是否存在黑名单广西
     * 判断条件：只要有一方把对方加入黑名单
     * @param userId 所属人Id
     * @param hateId 黑名单用户Id
     * @return boolean, true 是, false 否
     */
    boolean isHate(@Param("userId") String userId, @Param("hateId") String hateId);

    /**
     * 批量增加黑名单用户
     * @param hates 黑名单用户集合
     */
    void addBathHate(@Param("hates") HateUserEntity[] hates);

    /**
     * 批量删除黑名单
     * @param userId 用户Id
     * @param hates 黑名单用户Id数组
     * @return 删除结果
     */
    int deleteBathHate(@Param("userId") String userId, @Param("hates") String[] hates);

    /**
     * 获取把自己加入黑名单的用户Id集
     * @param userId 当前用户Id
     * @return String[] 用户Ids
     */
    String[] getHateMeUserIds(@Param("userId") String userId);
}
