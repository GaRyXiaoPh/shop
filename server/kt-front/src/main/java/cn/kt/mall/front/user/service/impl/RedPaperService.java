package cn.kt.mall.front.user.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.enums.AssetType;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.common.wallet.vo.UserAssetVO;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.util.RedUtil;
import cn.kt.mall.common.util.RedisUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.service.StatementService;
import cn.kt.mall.front.user.entity.RedpaperEntity;
import cn.kt.mall.front.user.entity.RedpaperReceiveEntity;
import cn.kt.mall.front.user.mapper.RedpaperEntityMapper;
import cn.kt.mall.front.user.mapper.RedpaperReceiveEntityMapper;
import cn.kt.mall.front.user.vo.GetRedRespVO;
import cn.kt.mall.front.user.vo.RedReceivedDetailsVO;
import cn.kt.mall.im.friend.service.FriendService;
import cn.kt.mall.im.friend.vo.FriendshipVO;
import cn.kt.mall.im.group.entity.GroupMemberEntity;
import cn.kt.mall.im.group.service.GroupService;
import cn.kt.mall.im.rong.service.RongCloudService;
import cn.kt.mall.mq.send.RedMQSend;
import cn.kt.mall.shop.config.SysConfig;
import io.rong.messages.RedInfoMessage;

public class RedPaperService {

	@Autowired
	private UserAssetService userAssetService;
	@Autowired
	private RedpaperEntityMapper redpaperEntityMapper;
	@Autowired
	private RedpaperReceiveEntityMapper redpaperReceiveEntityMapper;
	@Autowired
	private RongCloudService rongCloudService;
	@Autowired
	private StatementService statementService;
	@Autowired
	private SysConfig sysConfig;
	@Autowired
	private FriendService friendService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private RedMQSend redMQSend;

	public static final Short SIMPLE_RED = 1;
	public static final Short GROUP_RED = 2;
	public static final Short GROUP_RANDOM_RED = 3;

	/**
	 * 发红包
	 * 
	 * @param amount
	 * @param number
	 * @param words
	 * @param type
	 */
	@Transactional
	public RedpaperEntity sendRedPackage(String sendId, String targetId, double amount, int number, String words,
			Short type) {
		A.check(amount <= 0, "红包金额必须大于0");
		A.check(type != SIMPLE_RED && type != GROUP_RANDOM_RED && type != GROUP_RED, "暂不支持的红包类型");
		UserAssetEntity walletLemEntity = userAssetService.getUserAssetByCurrency(sendId, AssetType.CREDIT.getStrType());
		BigDecimal lem = new BigDecimal(Double.toString(amount));
		RedpaperEntity redpaperEntity = new RedpaperEntity();
		redpaperEntity.setId(IDUtil.getUUID());
		redpaperEntity.setTargetId(targetId);
		redpaperEntity.setAmount(lem);
		redpaperEntity.setSendId(sendId);
		redpaperEntity.setNumber(number);
		redpaperEntity.setType(type);
		redpaperEntity.setState((short) 1);
		redpaperEntity.setWords(words);
		redpaperEntity.setCreateTime(new Date());
		redpaperEntity.setLastTime(DateUtil.plusTime(new Date(), "1d"));
		redpaperEntityMapper.insert(redpaperEntity);
		List<RedpaperReceiveEntity> receiveList = new ArrayList<>();
		if (type == SIMPLE_RED) {
			A.check(number != 1, "红包数量必须为1");
			A.check(walletLemEntity == null || walletLemEntity.getAvailableBalance().compareTo(lem) < 0, "用户余额不足");
			receiveList.add(new RedpaperReceiveEntity(sendId, redpaperEntity.getId(), "0", lem, null, 0));
		} else if (type == GROUP_RED) {
			A.check(number <= 1, "红包数量必须大于等于1");
			for (int i = 0; i < number; i++) {
				receiveList.add(new RedpaperReceiveEntity(sendId, redpaperEntity.getId(), "0", lem, null, i));
			}
			lem = lem.multiply(new BigDecimal(Integer.toString(number)));
			A.check(walletLemEntity == null || walletLemEntity.getAvailableBalance().compareTo(lem) < 0, "用户余额不足");
		} else {
			A.check(number < 1, "红包数量必须大于等于1");
			A.check(walletLemEntity == null || walletLemEntity.getAvailableBalance().compareTo(lem) < 0, "用户余额不足");
			A.check(amount >= 20000, "单次支付总额不能大于20000");
			A.check(number > 100, "一次最多可发100个红包");
			A.check(amount / number >= 200, "单个红包金额不可超过200");
			A.check(amount / number < 0.01, "单个红包最低金额为0.01");
			int total = (int) (amount * 100);
			int[] amounts = RedUtil.generate(total, number);
			A.check(amounts.length != number, "服务器异常");
			int count = 0;
			for (int l : amounts) {
				BigDecimal itemAmount = new BigDecimal(Integer.toString(l)).divide(new BigDecimal("100"), 2,
						RoundingMode.FLOOR);
				receiveList
						.add(new RedpaperReceiveEntity(sendId, redpaperEntity.getId(), "0", itemAmount, null, count));
				count++;
			}
		}

		redpaperReceiveEntityMapper.insertBatch(receiveList);

		userAssetService.updateUserAsset(sendId, AssetType.CREDIT.getStrType(), lem.negate(), new BigDecimal("0"), TradeType.RED_PAPER,redpaperEntity.getId());


		redMQSend.sendDelayRedTimeOutMessage(redpaperEntity.getId(), System.currentTimeMillis());
		return redpaperEntity;
	}

	@Transactional
	public GetRedRespVO getRedPackage(String userId, String redId) {
		GetRedRespVO getRedRespVO = new GetRedRespVO();
		RedpaperEntity redpaperEntity = redpaperEntityMapper.selectByPrimaryKey(redId);
		A.check(redpaperEntity == null, "红包不存在");
		FriendshipVO friendshipVO = friendService.getFriendMessage(userId, redpaperEntity.getSendId());
		if (redpaperEntity.getType() != SIMPLE_RED) {
			GroupMemberEntity groupMemberEntity = groupService.getGroupMember(redpaperEntity.getTargetId(),
					redpaperEntity.getSendId());
			if (groupMemberEntity != null) {
				friendshipVO.setGroupNickname(groupMemberEntity.getNickname());
			}
		}
		getRedRespVO.setFriendshipVO(friendshipVO);
		if (redpaperEntity.getState() == 2 || redpaperEntity.getLastTime().before(new Date())) {
			// 失效
			getRedRespVO.setIsEffective((short) 1);
		} else {
			getRedRespVO.setIsEffective((short) 0);
		}
		List<RedpaperReceiveEntity> receiveList = redpaperReceiveEntityMapper.getByRedId(redId, userId);
		if (CollectionUtils.isEmpty(receiveList)) {
			List<RedpaperReceiveEntity> unReceiveList = redpaperReceiveEntityMapper.getByRedId(redId, "0");
			if (CollectionUtils.isEmpty(unReceiveList)) {
				getRedRespVO.setIsReceive((short) 2);
			} else {
				getRedRespVO.setIsReceive((short) 0);
			}
		} else {
			getRedRespVO.setIsReceive((short) 1);
			getRedRespVO.setReceiveAmount(receiveList.get(0).getAmount());
		}
		return getRedRespVO;
	}



	private void sendSysRedMessage(RedpaperEntity redpaperEntity, String userId, boolean isLast) {
		boolean isSelf = false;
		if (userId.equals(redpaperEntity.getSendId())) {
			isSelf = true;
		}
		if (redpaperEntity.getType() != SIMPLE_RED) {
			String str = "";
			if (isSelf) {
				str = "您领取了自己的红包";
			} else {
				str = "%s领取了您的红包";
				rongCloudService.pushRedMessage(userId, redpaperEntity.getTargetId(), "你领取了%s的红包",
						groupService.getGroupFriendNick(userId, redpaperEntity.getSendId()), "1");
			}
			if (isLast) {
				str += "，您的红包已经被抢完";
			}
			rongCloudService.pushRedMessage(redpaperEntity.getSendId(), redpaperEntity.getTargetId(), str,
					groupService.getGroupFriendNick(redpaperEntity.getSendId(), userId), "2");
		} else {
			rongCloudService.pushToFriendRed(userId, redpaperEntity.getSendId(),
					new RedInfoMessage("%s领取了您的红包", redpaperEntity.getSendId(),
							groupService.getGroupFriendNick(redpaperEntity.getSendId(), userId),
							System.currentTimeMillis(), "2"));
			rongCloudService.pushToFriendRed(redpaperEntity.getSendId(), userId,
					new RedInfoMessage("你领取了%s的红包", userId,
							groupService.getGroupFriendNick(userId, redpaperEntity.getSendId()),
							System.currentTimeMillis(), "1"));
		}
	}

	/**
	 * 获取红包领取详情
	 * 
	 * @param redId
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public CommonPageVO<RedReceivedDetailsVO> getRedPackageDetail(String redId, int pageNo, int pageSize) {
		List<RedReceivedDetailsVO> result = new ArrayList<>();
		RedpaperEntity redpaperEntity = redpaperEntityMapper.selectByPrimaryKey(redId);
		A.check(redpaperEntity == null, "红包不存在");
		List<RedpaperReceiveEntity> receivedList = redpaperReceiveEntityMapper.getByReceivedRedId(redId,
				new RowBounds(pageNo, pageSize));
		PageInfo<RedpaperReceiveEntity> pageInfo = new PageInfo<>(receivedList);
		if (redpaperEntity.getType() == SIMPLE_RED) {
			for (RedpaperReceiveEntity redpaperReceiveEntity : receivedList) {
				RedReceivedDetailsVO redReceivedDetailsVO = new RedReceivedDetailsVO();
				FriendshipVO friendshipVO = friendService.getFriendMessage(redpaperEntity.getSendId(),
						redpaperReceiveEntity.getReceiveId());
				BeanUtils.copyProperties(redpaperReceiveEntity, redReceivedDetailsVO);
				redReceivedDetailsVO.setFriendshipVO(friendshipVO);
				result.add(redReceivedDetailsVO);
			}
		} else {
			for (RedpaperReceiveEntity redpaperReceiveEntity : receivedList) {
				RedReceivedDetailsVO redReceivedDetailsVO = new RedReceivedDetailsVO();
				FriendshipVO friendshipVO = friendService.getFriendMessage(redpaperEntity.getSendId(),
						redpaperReceiveEntity.getReceiveId());
				GroupMemberEntity groupMemberEntity = groupService.getGroupMember(redpaperEntity.getTargetId(),
						redpaperReceiveEntity.getReceiveId());
				if (groupMemberEntity != null) {
					friendshipVO.setGroupNickname(groupMemberEntity.getNickname());
				}
				BeanUtils.copyProperties(redpaperReceiveEntity, redReceivedDetailsVO);
				redReceivedDetailsVO.setFriendshipVO(friendshipVO);
				result.add(redReceivedDetailsVO);
			}
		}
		return CommonUtil.copyFromPageInfo(pageInfo, result);
	}

	/*// 红包超时处理
	public void timeOut(String redId) {
		RedpaperEntity redpaperEntity = redpaperEntityMapper.selectByPrimaryKey(redId);
		if (redpaperEntity.getState() == 1) {
			RedpaperEntity redpaper = new RedpaperEntity();
			redpaper.setId(redpaperEntity.getId());
			redpaper.setState((short) 2);
			redpaperEntityMapper.updateByPrimaryKeySelective(redpaper);
			// 获取过期没有被领取的金额
			BigDecimal unReceived = redpaperReceiveEntityMapper.countUnReceived(redId);
			if (unReceived.compareTo(CommonConstants.BIGDECIMAL_DEFAULT) > 0) {
				StatementEntity entity = new StatementEntity("red_paper_no", sysConfig.getSysAccount(), null,
						redpaperEntity.getSendId(), null, unReceived, unReceived, TradeType.TRANSFER_RED_PAPER,
						(short) 1, "红包过期退还", null, new Date());
				statementService.addStatement(entity);
			}
		}
	}*/
}
