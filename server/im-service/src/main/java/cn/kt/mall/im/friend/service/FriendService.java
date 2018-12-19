package cn.kt.mall.im.friend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.user.vo.UserInfoVO;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.im.friend.constant.Constants;
import cn.kt.mall.im.friend.entity.FriendEntity;
import cn.kt.mall.im.friend.entity.FriendInvitationEntity;
import cn.kt.mall.im.friend.entity.UserRemarkEntity;
import cn.kt.mall.im.friend.mapper.FriendDAO;
import cn.kt.mall.im.friend.vo.FriendInvitationVO;
import cn.kt.mall.im.friend.vo.FriendOprVO;
import cn.kt.mall.im.friend.vo.FriendshipVO;
import cn.kt.mall.im.friend.vo.InviteFriendVO;
import cn.kt.mall.im.rong.service.RongCloudService;
import io.rong.messages.ContactNtfMessage;

/**
 * 朋友关系业务处理类 Created by Administrator on 2017/6/18.
 */

@Service
public class FriendService {

	@Autowired
	private RongCloudService rongCloudService;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private FriendDAO friendDAO;

	// 获取好友列表数据
	public PageVO<FriendshipVO> getFriendshipList(String userId, int pageNo, int pageSize) {
		int srcPageNo = pageNo;
		if (pageNo > 0)
			pageNo = pageNo - 1;
		int offset = pageNo * pageSize;

		// 获取黑名单
		String[] hates = this.rongCloudService.getHateList(userId);
		int count = this.friendDAO.getFriendshipListCount(userId, hates);
		List<FriendshipVO> list = this.friendDAO.getFriendshipList(userId, hates, offset, pageSize);
		return new PageVO<FriendshipVO>(srcPageNo, pageSize, count, list);
	}

	/**
	 * 查询好友列表(isHate 0:正常好友 1:黑名单好友)
	 *
	 * @param userId
	 * @param isHate
	 * @return
	 */
	public List<FriendshipVO> getFriendsList(String userId, int isHate) {
		return friendDAO.getFriendsList(userId, isHate);
	}

	public List<FriendshipVO> getFriendsFilter(String[] friend, String userId) {
		return this.friendDAO.getFriendsFilter(friend, userId);
	}

	// 添加好友业务处理
	@Transactional
	public void inviteFriend(InviteFriendVO inviteFriendVO) {
		String userId = inviteFriendVO.getUserId();
		String friendId = inviteFriendVO.getFriendId();
		String remarkName = inviteFriendVO.getRemarkName();
		UserEntity friendEntity = this.userDAO.getById(friendId);
		UserEntity sourceEntity = this.userDAO.getById(userId);

		A.check(friendEntity == null, "对方不存在");
		A.check(userId.equals(friendId), "不能添加自己为好友");

		// 检查是否已经是好友
		A.check(this.friendDAO.isFriend(userId, friendId) == 1, "对方已经是您的好友");

		// 是否有申请记录
		FriendInvitationEntity friendInvitationEntity = friendDAO.getFriendInvitation(userId, friendId);
		if (friendInvitationEntity == null) {
			rongCloudService.removeHate(userId, friendId);
			// 新增好友邀请数据
			FriendInvitationEntity friendInvitation = new FriendInvitationEntity(IDUtil.getUUID(), userId, friendId,
					Constants.InvitationStatus.NORMAL, inviteFriendVO.getMessage());
			// 添加申请好友记录
			this.friendDAO.addFriendInvitation(friendInvitation);
			// 添加备注
			UserRemarkEntity userRemarkEntity = new UserRemarkEntity();
			userRemarkEntity.setId(IDUtil.getUUID());
			userRemarkEntity.setUserId(userId);
			userRemarkEntity.setRemarkName(remarkName);
			userRemarkEntity.setFriendId(friendId);
			friendDAO.addUserRemark(userRemarkEntity);
		} else {
			// 已经申请则修改状态
			this.friendDAO.updateFriendInvitationStatus(userId, friendId, Constants.InvitationStatus.NORMAL, inviteFriendVO.getMessage());
		}

		Map<String, Object> map = new HashMap<>();
		map.put("sourceUserNickname", sourceEntity.getNick());
		map.put("version", new Date());
		// 给对方发送通知消息rong
		ContactNtfMessage rongMessage = new ContactNtfMessage("Request", JSONUtil.toJSONString(map), userId, friendId,
				inviteFriendVO.getMessage());
		rongCloudService.pushCommonMessage(userId, friendId, rongMessage, "");

	}

	// 获取待我确认的好友
	public PageVO<FriendInvitationVO> getMyFriendInvitation(String userId, int pageNo, int pageSize) {
		int srcPageNo = pageNo;
		if (pageNo > 0)
			pageNo = pageNo - 1;
		int offset = pageNo * pageSize;

		int count = friendDAO.getMyFriendInvitationCount(userId);
		List<FriendInvitationVO> list = new ArrayList<FriendInvitationVO>();
		List<FriendInvitationEntity> ll = friendDAO.getMyFriendInvitation(userId, offset, pageSize);
		for (int i = 0; i < ll.size(); i++) {
			FriendInvitationVO friendInvitationVO = new FriendInvitationVO();
			FriendInvitationEntity invit = ll.get(i);
			UserEntity user = userDAO.getById(invit.getUserId());
			friendInvitationVO.setInvitationId(invit.getId());
			friendInvitationVO.setId(invit.getUserId());
			friendInvitationVO.setMessage(invit.getMessage());
			friendInvitationVO.setStatus(invit.getStatus());
			friendInvitationVO.setAvatar(user.getAvatar());
			friendInvitationVO.setUsername(user.getNick());
			friendInvitationVO.setMobile(user.getMobile());
			list.add(friendInvitationVO);
		}
		return new PageVO<FriendInvitationVO>(srcPageNo, pageSize, count, list);
	}

	// 获取用户信息根据手机号
	public UserInfoVO searchUserByMobile(String mobile) {
		UserEntity userEntity = userDAO.getByMobile(mobile);
		A.check(userEntity == null, "该用户不存在 ");
		UserInfoVO userInfoVO = new UserInfoVO();
		if (userEntity != null) {
			userInfoVO.setUserId(userEntity.getId());
			userInfoVO.setUsername(userEntity.getUsername());
			userInfoVO.setMobile(userEntity.getMobile());
			userInfoVO.setNick(userEntity.getNick());
			userInfoVO.setAvatar(userEntity.getAvatar());
		}
		return userInfoVO;
	}

	// 确认好友邀请处理
	public FriendInvitationEntity confirmInvitation(String userId, String friendId, String result, String remarkName) {
		// 如果已经是好友则跳过处理
		if (this.friendDAO.isFriend(userId, friendId) == 1) {
			this.friendDAO.updateFriendInvitationStatus(friendId, userId, Constants.InvitationResult.AGREEMENT, null);
			return this.friendDAO.getFriendInvitation(friendId, userId);
		}

		String status = Constants.InvitationStatus.AGREEMENT;
		// 对方确认结果为“拒绝”
		if (Constants.InvitationResult.REJECT.equals(result)) {
			status = Constants.InvitationStatus.REJECT;
		}else {
			rongCloudService.removeHate(userId, friendId);
			rongCloudService.removeHate(friendId, userId);
		}

		// 更新好友邀请状态
		this.friendDAO.updateFriendInvitationStatus(friendId, userId, status, null);
		// 更新好友邀请状态
		this.friendDAO.updateFriendInvitationStatus(userId, friendId, status, null);

		// 新增双方好友关系
		if (Constants.InvitationStatus.AGREEMENT.equals(status)) {
			FriendEntity[] array = new FriendEntity[2];
			array[0] = new FriendEntity(IDUtil.getUUID(), userId, friendId);
			array[1] = new FriendEntity(IDUtil.getUUID(), friendId, userId);
			// 添加好友关系
			this.friendDAO.addFriendship(array);
			// 添加名称备注
			UserRemarkEntity userRemarkEntity = new UserRemarkEntity();
			userRemarkEntity.setId(IDUtil.getUUID());
			userRemarkEntity.setUserId(userId);
			userRemarkEntity.setRemarkName(remarkName);
			userRemarkEntity.setFriendId(friendId);
			friendDAO.addUserRemark(userRemarkEntity);
		}

		// 給双方发送通知消息rong
		rongCloudService.pushToFriend(userId, friendId, "你们已经成为好友啦", null);
		rongCloudService.pushToFriend(friendId, userId, "你们已经成为好友啦", null);
		return this.friendDAO.getFriendInvitation(friendId, userId);
	}

	// 删除好友(双方)
	public void deleteFriend(String userId, String friendId) {
		// 删除好友关系数据
		this.friendDAO.deleteFriend(userId, friendId);
		// 删除好友关系数据
		this.friendDAO.deleteFriend(friendId, userId);

		// 删除备注
		friendDAO.deleteRemark(userId, friendId);
		// 删除备注
		friendDAO.deleteRemark(friendId, userId);

		rongCloudService.addHate(userId, friendId);
		rongCloudService.addHate(friendId, userId);
		// 发送融云指令消息给对方，清空对方的聊得会话
//		this.rongCloudService.pushCmdToUser(userId, friendId,
//				new RongMessage<String>(MessageType.FRIEND_DELETE, userId));
	}

	// 修改好关系信息
	public void updateFriend(FriendOprVO friendOprVO) {
		A.check(friendOprVO.getUserId() == null || friendOprVO.getFriendId() == null, "没有提交完整的好友关系");

		A.check(friendOprVO.getStar() == null && friendOprVO.getInvisibleFriend() == null
				&& friendOprVO.getInvisibleMe() == null, "没有提交需要修改的信息");

		this.friendDAO.updateFriend(friendOprVO);
	}

	// 获取好友关系信息
	public FriendEntity getFriendInfo(String userId, String friendId) {
		return this.friendDAO.getEntityByUserAndFriend(userId, friendId);
	}

	// 修改备注
	public void updateRemark(UserRemarkEntity userRemarkEntity) {
		// 当用户已备注好友,则更新
		if (friendDAO.getRemark(userRemarkEntity) > 0) {
			this.friendDAO.updateRemark(userRemarkEntity);
		} else {
			// 没有备注则添加
			userRemarkEntity.setId(IDUtil.getUUID());
			this.friendDAO.addUserRemark(userRemarkEntity);
		}

	}

	public FriendshipVO getFriendMessage(String userId, String friendId) {
		FriendshipVO friendshipVO = friendDAO.getFriendMessage(userId, friendId);
		if (friendshipVO == null) {
			FriendshipVO userInfo = friendDAO.getUserMessage(friendId);
			userInfo.setFlag(false);
			return userInfo;
		}
		friendshipVO.setFlag(true);
		return friendshipVO;
	}
}
