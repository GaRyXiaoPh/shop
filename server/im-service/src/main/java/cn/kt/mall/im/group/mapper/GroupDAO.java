package cn.kt.mall.im.group.mapper;

import cn.kt.mall.im.friend.vo.FriendshipVO;
import cn.kt.mall.im.group.entity.GroupApplyEntity;
import cn.kt.mall.im.group.entity.GroupChatEntity;
import cn.kt.mall.im.group.entity.GroupMemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupDAO {

    //获取群信息
    GroupChatEntity getGroupChat(@Param("groupId") String groupId,@Param("userId")String userId);

    //修改群信息
    int fixGroupChat(@Param("groupId")String groupId,
                     @Param("name") String name,
                     @Param("board")String board,
                     @Param("master") String master);

    //获取我的群
    int getMyGroupChatCount(@Param("userId") String userId);
    List<GroupChatEntity> getMyGroupChat(@Param("userId")String userId, @Param("offset")int offset,@Param("pageSize") int pageSize);

    //是否已经加入群
    boolean isGroupMember(@Param("userId")String userId, @Param("groupId") String groupId);

    //创建群
    int addGroupChat(GroupChatEntity entity);

    //加入群
    int addGroupMember(@Param("list") List<GroupMemberEntity> list);

    //群是否存在
    boolean isGroupChat(@Param("groupId") String groupId);

    //申请加群
    int addGroupApply(GroupApplyEntity entity);

    //群主获取申请记录
    int getMyGroupApplyEntityCount(@Param("groupId")String groupId);
    List<GroupApplyEntity> getMyGroupApplyEntity(@Param("groupId")String groupId);

    //群主审批
    GroupApplyEntity getGroupApplyById(@Param("id")String id);
    int confirmGroupApplyById(@Param("id")String id, @Param("status")String status);

    //获取设置群主
    List<String> getGroupChatMembers(@Param("groupId")String groupId);
    String getGroupChatManager(@Param("groupId") String groupId);
    int setGroupChatManager(@Param("groupId")String groupId);

    //退出群聊
    int delGroupMember(@Param("userId") String userId, @Param("groupId")String groupId);
    int delGroup(@Param("groupId") String groupId);
    int checkGroupManage(@Param("userId") String userId,@Param("groupId")String groupId);

    //解散群
    int disGroupChat(@Param("groupId")String groupId);
    int removeGroupChat(@Param("groupId")String groupId);
    //删除群成员
    int delGroupMembers(@Param("groupId") String groupId,@Param("userIds") String[] userIds);

    //更新群成员昵称
    int updateNick(@Param("nickname") String nickname,@Param("groupId") String groupId,@Param("userId") String userId);
    //群主权限过度
    int overAuthority(@Param("isManager") String isManager,@Param("groupId") String groupId,@Param("userId") String userId);

    GroupMemberEntity getGroupChatMember(@Param("groupId") String groupId, @Param("userId") String userId);

    //根据群id查询群信息
    GroupChatEntity getGroupInfo(@Param("id") String id);
}
