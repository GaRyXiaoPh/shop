package cn.kt.mall.im.group.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.im.friend.constant.Constants;
import cn.kt.mall.im.friend.mapper.FriendDAO;
import cn.kt.mall.im.friend.vo.FriendshipVO;
import cn.kt.mall.im.group.entity.GroupApplyEntity;
import cn.kt.mall.im.group.entity.GroupChatEntity;
import cn.kt.mall.im.group.entity.GroupMemberEntity;
import cn.kt.mall.im.group.mapper.GroupDAO;
import cn.kt.mall.im.group.vo.GroupApplyVO;
import cn.kt.mall.im.group.vo.MembersUpdateVo;
import cn.kt.mall.im.rong.service.RongCloudService;

@Service
public class GroupService {

	@Autowired
	RongCloudService rongCloudService;

	@Autowired
	UserDAO userDAO;

	@Autowired
	GroupDAO groupDAO;

	@Autowired
	FriendDAO friendDAO;

	// 获取群信息
	public GroupChatEntity getGroupChat(String groupId, String userId) {
		GroupChatEntity groupChatEntity = groupDAO.getGroupChat(groupId, userId);
		return groupChatEntity;
	}

	public List<FriendshipVO> getGroupMemberList(String groupId, String userId) {
		List<FriendshipVO> list = friendDAO.getGroupMemberList(groupId, userId);
		for (FriendshipVO l : list) {
			int count = friendDAO.isFriend(userId, l.getUserId());
			if (count == 1)
				l.setFlag(true);
			if (count == 0)
				l.setFlag(false);
		}
		return list;
	}

	// 修改群信息
	public void fixGroupChat(String userId, String groupId, String groupName, String board) {
		GroupChatEntity groupChatEntity = groupDAO.getGroupChat(groupId, userId);
		A.check(groupChatEntity == null, "群不存在");
		A.check(userId.equals(groupChatEntity.getMaster()) == false, "不是群主不能修改群信息");
		if (groupName != null && groupName != "") {
			rongCloudService.refreshRongGroupName(groupId, groupName, userId);
		}
		this.groupDAO.fixGroupChat(groupId, groupName, board, null);
	}

	// 获取我的群list
	public PageVO<GroupChatEntity> getMyGroupChat(String userId, int pageNo, int pageSize) {
		int srcPageNo = pageNo;
		if (pageNo > 0)
			pageNo = pageNo - 1;
		int offset = pageNo * pageSize;

		int count = this.groupDAO.getMyGroupChatCount(userId);
		List<GroupChatEntity> list = this.groupDAO.getMyGroupChat(userId, offset, pageSize);
		return new PageVO<GroupChatEntity>(srcPageNo, pageSize, count, list);
	}

	// 创建群
	public boolean createGroupChat(String userId, String[] memberIds, String groupId, String groupName) {

		// 如果userId不在memberIds中则错误
		boolean bFind = false;
		for (int i = 0; i < memberIds.length; i++) {
			if (userId.equals(memberIds[i])) {
				bFind = true;
				break;
			}
		}
		A.check(bFind == false, "memberIds错误");

		// 融云创建群
		try {
			this.rongCloudService.createGroupChat(groupId, groupName, memberIds, userId);
		} catch (Exception e) {
			return false;
		}

		// 数据库维护
		GroupChatEntity groupChatEntity = new GroupChatEntity();
		groupChatEntity.setId(groupId);
		groupChatEntity.setName(groupName);
		groupChatEntity.setMaster(userId);
		groupChatEntity.setCreator(userId);
		this.groupDAO.addGroupChat(groupChatEntity);
		List<GroupMemberEntity> list = new ArrayList<GroupMemberEntity>();
		for (int i = 0; i < memberIds.length; i++) {
			GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
			groupMemberEntity.setId(IDUtil.getUUID());
			groupMemberEntity.setGroupId(groupId);
			groupMemberEntity.setUserId(memberIds[i]);
			if (userId.equals(memberIds[i])) {
				groupMemberEntity.setIsManager("1");
			}
			groupMemberEntity.setCreator(userId);
			groupMemberEntity.setCreateTime(new Date());
			list.add(groupMemberEntity);
		}
		this.groupDAO.addGroupMember(list);
		return true;
	}

	// 申请加群
	public void joinGroupChat(String userId, String groupId, String message) {
		A.check(this.groupDAO.isGroupMember(userId, groupId), "您已经加入了群");

		GroupApplyEntity groupApplyEntity = new GroupApplyEntity();
		groupApplyEntity.setId(IDUtil.getUUID());
		groupApplyEntity.setGroupId(groupId);
		groupApplyEntity.setCreator(userId);
		groupApplyEntity.setMessage(message);
		groupApplyEntity.setCreateTime(new Date());
		this.groupDAO.addGroupApply(groupApplyEntity);
	}

	// 群主获取申请列表
	public PageVO<GroupApplyVO> getMyGroupApply(String userId, String groupId, int pageNo, int pageSize) {
		int srcPageNo = pageNo;
		if (pageNo > 0)
			pageNo = pageNo - 1;
		int offset = pageNo * pageSize;

		// 检查userId是否是群主
		String manager = this.groupDAO.getGroupChatManager(groupId);
		A.check(!userId.equals(manager), "您不是此群主");

		// 获取群信息
		GroupChatEntity groupChat = this.groupDAO.getGroupChat(groupId, userId);

		int count = this.groupDAO.getMyGroupApplyEntityCount(groupId);
		List<GroupApplyVO> list = new ArrayList<>();
		List<GroupApplyEntity> ll = this.groupDAO.getMyGroupApplyEntity(groupId);
		for (int i = 0; i < ll.size(); i++) {
			GroupApplyEntity groupApplyEntity = ll.get(i);
			UserEntity user = this.userDAO.getById(groupApplyEntity.getCreator());
			GroupApplyVO groupApplyVO = new GroupApplyVO();
			groupApplyVO.setId(groupApplyEntity.getId());
			groupApplyVO.setGroupId(groupApplyEntity.getGroupId());
			groupApplyVO.setGroupName(groupChat.getName());
			groupApplyVO.setUserId(groupApplyEntity.getCreator());
			groupApplyVO.setMobile(user.getMobile());
			groupApplyVO.setNick(user.getNick());
			groupApplyVO.setUsername(user.getUsername());
			groupApplyVO.setMessage(groupApplyEntity.getMessage());
			groupApplyVO.setStatus(groupApplyEntity.getStatus());
			groupApplyVO.setCreateTime(groupApplyEntity.getCreateTime());
			list.add(groupApplyVO);
		}
		return new PageVO<GroupApplyVO>(srcPageNo, pageSize, count, list);
	}

	// 同意加群
	public boolean confirmMyGroupApply(String userId, String applyId, String status) {
		GroupApplyEntity applyEntity = groupDAO.getGroupApplyById(applyId);
		A.check(applyEntity == null, "申请记录无效");
		GroupChatEntity chatEntity = groupDAO.getGroupChat(applyEntity.getGroupId(), userId);
		A.check(chatEntity == null, "申请的群不存在");
		UserEntity userEntity = userDAO.getById(applyEntity.getCreator());
		A.check(userEntity == null, "申请人无效");

		String result = Constants.InvitationResult.AGREEMENT;
		if (Constants.InvitationResult.AGREEMENT.equals(status)) {
			result = Constants.InvitationResult.AGREEMENT;
			// 融云处理
			this.rongCloudService.joinGroupChat(applyEntity.getGroupId(), chatEntity.getName(),
					new String[] { applyEntity.getCreator() }, new String[] { userEntity.getNick() }, userId);
			// 数据库群成员
			List<GroupMemberEntity> list = new ArrayList<GroupMemberEntity>();
			GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
			groupMemberEntity.setId(IDUtil.getUUID());
			groupMemberEntity.setGroupId(applyEntity.getGroupId());
			groupMemberEntity.setUserId(applyEntity.getCreator());
			groupMemberEntity.setCreator(userId);
			groupMemberEntity.setCreateTime(new Date());
			groupMemberEntity.setIsManager("");
			groupMemberEntity.setNickname(userEntity.getNick());
			list.add(groupMemberEntity);
			this.groupDAO.addGroupMember(list);
		} else {
			result = Constants.InvitationResult.REJECT;
		}
		this.groupDAO.confirmGroupApplyById(applyId, result);
		return true;
	}

	// 退群
	public boolean exitGroupChat(String userId, String groupId) {
		this.rongCloudService.quitGroupChat(groupId, new String[] { userId });
		List<String> list = this.groupDAO.getGroupChatMembers(groupId);
		// 最后一个成员退出解散群
		if (list.size() <= 1) {
			// 删除群
			this.rongCloudService.dismissGroupChat(groupId, userId);
			this.groupDAO.delGroup(groupId);
			this.groupDAO.disGroupChat(groupId);
		} else {
			this.groupDAO.delGroupMember(userId, groupId);
			// 当退出群的用户为群主时,则将群主身份传给另一个群用户
			if (groupDAO.checkGroupManage(userId, groupId) == 1) {
				this.groupDAO.setGroupChatManager(groupId);
			}
		}
		return true;
	}

	// 解散群
	public boolean disGroupChat(String userId, String groupId) {
		// 判断是否是群主
		String manager = this.groupDAO.getGroupChatManager(groupId);
		if (userId.equals(manager)) {
			this.rongCloudService.dismissGroupChat(groupId, userId);
			this.groupDAO.delGroup(groupId);
			this.groupDAO.disGroupChat(groupId);
		}
		return true;
	}

	@Transactional
	public int updateMember(String userId, MembersUpdateVo membersUpdateVo) {
		//获取成员信息
		String[] userIds = membersUpdateVo.getUserIds();

		//获取成员的昵称
		List<String> nick = userDAO.getNicks(userIds);
		String[] nickname = new String[nick.size()];
		nick.toArray(nickname);

		//获取群信息
		GroupChatEntity groupChatEntity = groupDAO.getGroupInfo(membersUpdateVo.getGroupId());
		// 添加成员
		if (membersUpdateVo.getFlag() == 1) {
			List<GroupMemberEntity> list = new ArrayList<>();
			for (int i = 0; i < userIds.length; i++) {
				GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
				groupMemberEntity.setId(IDUtil.getUUID());
				groupMemberEntity.setGroupId(membersUpdateVo.getGroupId());
				groupMemberEntity.setUserId(userIds[i]);
				groupMemberEntity.setIsManager(null);
				groupMemberEntity.setCreator(userId);
				list.add(groupMemberEntity);
			}
			int count = groupDAO.addGroupMember(list);
			A.check(count < 1, "成员添加失败");

			//加入融云群聊
			this.rongCloudService.joinGroupChat(membersUpdateVo.getGroupId(),groupChatEntity.getName() , userIds,nickname, userId);
			return count;
		}
		// 删除成员
		if (membersUpdateVo.getFlag() == 2) {
			// 判断是否是群主
			String manager = this.groupDAO.getGroupChatManager(membersUpdateVo.getGroupId());
			A.check(userId.equals(manager) == false, "只有群主才能删除");
			// 删除融云上的群成员
			this.rongCloudService.quitGroupChat(membersUpdateVo.getGroupId(), membersUpdateVo.getUserIds());
			// 删除数据库的群成员信息
			int count = groupDAO.delGroupMembers(membersUpdateVo.getGroupId(), membersUpdateVo.getUserIds());
			A.check(count < 1, "删除成员失败");
			return count;
		}
		return 0;
	}

	/**
	 * 更新昵称
	 *
	 * @param nickname
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public int updateNick(String nickname, String groupId, String userId) {
		GroupChatEntity groupChatEntity = groupDAO.getGroupChat(groupId, userId);
		A.check(groupChatEntity == null, "群不存在");
		int count = groupDAO.updateNick(nickname, groupId, userId);
		A.check(count < 1, "修改昵称失败");
		return count;
	}

	@Transactional
	public int overAuthority(String groupId, String userId, String newUseId) {
		GroupChatEntity groupChatEntity = groupDAO.getGroupChat(groupId, userId);
		A.check(groupChatEntity == null, "群不存在");
		A.check(groupChatEntity.getMaster().equals(userId) == false, "只有群主才能操作权限过度");
		// 将群主变为普通群成员
		int count = groupDAO.overAuthority(null, groupId, userId);
		A.check(count < 1, "群主变为普通成员失败");

		// 将普通成员变为群主
		count = groupDAO.overAuthority("1", groupId, newUseId);
		A.check(count < 1, "普通成员变为群主失败");

		// 更新群信息中的群主信息
		count = groupDAO.fixGroupChat(groupId, null, null, newUseId);
		A.check(count < 1, "更新群信息中的群主信息");
		return count;
	}

	// 获取群组里面好友信息
	public GroupMemberEntity getGroupMember(String groupId, String userId) {
		return groupDAO.getGroupChatMember(groupId, userId);
	}

	/**
	 * 获取群友昵称
	 * @param currentUserId
	 * @param friendId
	 * @return
	 */
	public String getGroupFriendNick(String currentUserId, String friendId) {
		FriendshipVO friendshipVO = friendDAO.getFriendMessage(currentUserId, friendId);
		if (friendshipVO == null) {
			friendshipVO = friendDAO.getUserMessage(friendId);
		}
		String nickName = friendshipVO.getUsername();
		GroupMemberEntity groupMemberEntity = this.getGroupMember(currentUserId, friendId);
		if (groupMemberEntity != null) {
			friendshipVO.setGroupNickname(groupMemberEntity.getNickname());
		}
		if(!StringUtils.isEmpty(friendshipVO.getNick())) {
			nickName = friendshipVO.getNick();
		}
		if(!StringUtils.isEmpty(friendshipVO.getRemarkName())) {
			nickName = friendshipVO.getRemarkName();
		}
		if (!StringUtils.isEmpty(friendshipVO.getGroupNickname())) {
			nickName = friendshipVO.getGroupNickname();
		}
		return nickName;
	}
}
