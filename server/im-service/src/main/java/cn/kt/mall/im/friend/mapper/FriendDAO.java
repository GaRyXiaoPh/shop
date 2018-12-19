package cn.kt.mall.im.friend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.kt.mall.im.friend.entity.FriendEntity;
import cn.kt.mall.im.friend.entity.FriendInvitationEntity;
import cn.kt.mall.im.friend.entity.UserRemarkEntity;
import cn.kt.mall.im.friend.vo.FriendOprVO;
import cn.kt.mall.im.friend.vo.FriendshipVO;

/**
 * 好友关系数据处理Mapper
 * Created by Administrator on 2017/6/18.
 */
@Mapper
public interface FriendDAO {

    //获取用户好友列表,注：剔除自己的黑名单
    int getFriendshipListCount(@Param("userId") String userId, @Param("hates")String[] hates);

    /**
     * 获取好友列表(0:正常用户  1:黑名单用户)
     *
     * @param userId
     * @param isHate
     * @return
     */
    List<FriendshipVO> getFriendsList(@Param("userId") String userId,@Param("isHate") int isHate);
    //获取用户的好友列表
    List<FriendshipVO> getFriendshipList(@Param("userId") String userId, @Param("hates") String[] hates, @Param("offset")int offset, @Param("pageSize")int pageSize);

    //获取我剔除的好友
    List<FriendshipVO> getFriendsFilter(@Param("friend") String[] friend,@Param("userId") String userId);

    //新增好友邀请
    void addFriendInvitation(FriendInvitationEntity friendInvitation);

    //更新好友状态,status 0 未确认，1 接受, 2 拒绝
    void updateFriendInvitationStatus(@Param("userId") String userId, @Param("friendId") String friendId, @Param("status") String status, @Param("message") String message);

    //获取好友申请记录
    FriendInvitationEntity getFriendInvitation(@Param("userId") String userId, @Param("friendId") String friendId);

    //获取待我审批的好友申请
    int getMyFriendInvitationCount(@Param("userId")String userId);
    List<FriendInvitationEntity> getMyFriendInvitation(@Param("userId")String userId, @Param("offset")int offset, @Param("pageSize")int pageSize);

    //批量添加好友关系
    void addFriendship(@Param("list") FriendEntity[] list);

    //添加备注
    void addUserRemark(UserRemarkEntity list);

    //修改备注
    void updateRemark(UserRemarkEntity userRemarkEntity);

    //删除备注
    void deleteRemark(@Param("userId") String userId,@Param("friendId") String friendId);

    int getRemark(UserRemarkEntity userRemarkEntity);

    //删除好友(双方)
    int deleteFriend(@Param("userId") String userId, @Param("friendId") String friendId);

    //修改好关系信息
    int updateFriend(FriendOprVO friendOprVO);

    //是否是好友关系
    int isFriend(@Param("userId") String userId, @Param("friendId") String friendId);

    //获取用户的所有好友的用户Id
    String[] getFriendUserIds(@Param("userId") String userId);

    /**
     * 获取朋友圈可见的好友用户Ids
     * 条件：自己的“不看他朋友圈”为0 and 对方的“不让他看朋友圈”为0
     * @param userId 用户Id
     * @return 可见好友用户Ids
     */
    String[] getVisibleFriendUserIds(@Param("userId") String userId);

    /**
     * 是否可见好友的朋友圈
     * 条件：自己的“不看他朋友圈”为0 and 对方的“不让他看朋友圈”为0
     * @param currentId 当前用户Id
     * @param friendId 目标用户Id
     * @return boolean; true 可见, false 不可见
     */
    boolean canVisibleFriendMoment(@Param("currentId") String currentId, @Param("friendId") String friendId);

    //获取好友关系信息
    FriendEntity getEntityByUserAndFriend(@Param("userId") String userId, @Param("friendId") String friendId);

    /**
     * 查询群成员信息列表
     *
     * @param userId
     * @param groupId
     * @return
     */
    List<FriendshipVO> getGroupMemberList(@Param("groupId") String groupId,@Param("userId") String userId);

    /**
     * 获取好友信息
     *
     * @param friendId
     * @param userId
     * @return
     */
    FriendshipVO getFriendMessage(@Param("userId") String userId,@Param("friendId") String friendId);

    /**
     * 查询用户信息
     *
     * @param id
     * @return
     */
    FriendshipVO getUserMessage(@Param("id") String id);
}
