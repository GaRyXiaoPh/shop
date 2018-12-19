package cn.kt.mall.im.moments.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.im.friend.service.FriendService;
import cn.kt.mall.im.friend.vo.FriendshipVO;
import cn.kt.mall.im.group.service.GroupService;
import cn.kt.mall.im.moments.entity.MomentsCommentEntity;
import cn.kt.mall.im.moments.entity.MomentsEntity;
import cn.kt.mall.im.moments.entity.MomentsImgEntity;
import cn.kt.mall.im.moments.entity.MomentsLikeEntity;
import cn.kt.mall.im.moments.mapper.MomentsCommentEntityMapper;
import cn.kt.mall.im.moments.mapper.MomentsEntityMapper;
import cn.kt.mall.im.moments.mapper.MomentsImgEntityMapper;
import cn.kt.mall.im.moments.mapper.MomentsLikeEntityMapper;
import cn.kt.mall.im.moments.vo.CommentRespVO;
import cn.kt.mall.im.moments.vo.LikeRespVO;
import cn.kt.mall.im.moments.vo.MomentsPushVO;
import cn.kt.mall.im.moments.vo.MomentsReqVO;
import cn.kt.mall.im.moments.vo.MomentsRespVO;
import cn.kt.mall.im.rong.service.RongCloudService;
import io.rong.messages.MomentsMessage;

@Service
public class MomentService {

	@Autowired
	private MomentsEntityMapper momentsEntityMapper;
	@Autowired
	private MomentsImgEntityMapper momentsImgEntityMapper;
	@Autowired
	private MomentsCommentEntityMapper momentsCommentEntityMapper;
	@Autowired
	private MomentsLikeEntityMapper momentsLikeEntityMapper;
	@Autowired
	private RongCloudService rongCloudService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private GroupService groupService;
	@Autowired
	private FriendService friendService;

	/**
	 * 发布莱粉圈
	 * 
	 * @param id
	 * @param momentsReqVO
	 */
	@Transactional
	public void addMoments(String userId, MomentsReqVO momentsReqVO) {
		MomentsEntity record = new MomentsEntity(momentsReqVO.getContent(), momentsReqVO.getAddressLon(),
				momentsReqVO.getAddressLat(), userId);
		record.setLocation(momentsReqVO.getLocation());
		momentsEntityMapper.insert(record);
		if (!CollectionUtils.isEmpty(momentsReqVO.getImgUrl())) {
			List<MomentsImgEntity> list = new ArrayList<>();
			for (String image : momentsReqVO.getImgUrl()) {
				MomentsImgEntity momentsImgEntity = new MomentsImgEntity();
				momentsImgEntity.setMomentsId(record.getId());
				momentsImgEntity.setUrl(image);
				list.add(momentsImgEntity);
			}
			momentsImgEntityMapper.insertBatch(list);
		}
	}

	/**
	 * 分页获取莱粉圈信息
	 * 
	 * @param userId
	 * @param friendId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public CommonPageVO<MomentsRespVO> listMoments(String userId, String friendId, int pageNo, int pageSize) {
		List<String> ids = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		if (userId.equals(friendId)) {
			ids.add(userId);
			map.put(userId, "");
		} else {
			List<FriendshipVO> friendList = friendService.getFriendsList(userId, 0);
			if (CollectionUtils.isEmpty(friendList)) {
				ids.add(userId);
				map.put(userId, "");
			} else {
				for (FriendshipVO friendshipVO : friendList) {
					ids.add(friendshipVO.getUserId());
					map.put(friendshipVO.getUserId(), friendshipVO.getRemarkName());
				}
			}
		}
		List<MomentsRespVO> list = momentsEntityMapper.listMoments(ids, new RowBounds(pageNo, pageSize));
		List<Long> momentsIds = new ArrayList<>();
		for (MomentsRespVO momentsRespVO : list) {
			momentsIds.add(momentsRespVO.getId());
		}
		if (!CollectionUtils.isEmpty(momentsIds) && momentsIds.size() > 0) {
			// 获取图片信息
			List<MomentsImgEntity> imgList = momentsImgEntityMapper.selectImgList(momentsIds);
			// 获取评论信息
			List<CommentRespVO> commentList = momentsCommentEntityMapper.selectCommentList(momentsIds, userId);
			// 获取点赞信息
			List<LikeRespVO> likeList = momentsLikeEntityMapper.selectLikeUserList(momentsIds, userId);

			buildCommentAndLikeList(list, commentList, likeList, imgList, map);
		}

		PageInfo<MomentsRespVO> pageInfo = new PageInfo<>(list);
		return CommonUtil.copyFromPageInfo(pageInfo, list);
	}

	/**
	 * 构建评论和点赞信息
	 * 
	 * @param list
	 * @param commentList
	 * @param likeList
	 * @param imgList
	 */
	private void buildCommentAndLikeList(List<MomentsRespVO> momentsList, List<CommentRespVO> commentList,
			List<LikeRespVO> likeList, List<MomentsImgEntity> imgList, Map<String, String> remarkMap) {
		Map<Long, List<CommentRespVO>> commentMap = new HashMap<>();
		Map<Long, List<LikeRespVO>> likeMap = new HashMap<>();
		Map<Long, List<String>> imgMap = new HashMap<>();
		for (CommentRespVO commentRespVO : commentList) {
			List<CommentRespVO> list = commentMap.get(commentRespVO.getMomentsId());
			if (CollectionUtils.isEmpty(list)) {
				list = new ArrayList<>();
			}
			list.add(commentRespVO);
			commentMap.put(commentRespVO.getMomentsId(), list);
		}
		for (LikeRespVO likeRespVO : likeList) {
			List<LikeRespVO> list = likeMap.get(likeRespVO.getMomentsId());
			if (CollectionUtils.isEmpty(list)) {
				list = new ArrayList<>();
			}
			list.add(likeRespVO);
			likeMap.put(likeRespVO.getMomentsId(), list);
		}
		for (MomentsImgEntity momentsImgEntity : imgList) {
			List<String> list = imgMap.get(momentsImgEntity.getMomentsId());
			if (CollectionUtils.isEmpty(list)) {
				list = new ArrayList<>();
			}
			list.add(momentsImgEntity.getUrl());
			imgMap.put(momentsImgEntity.getMomentsId(), list);
		}

		for (MomentsRespVO momentsRespVO : momentsList) {
			momentsRespVO.setRemarkName(remarkMap.get(momentsRespVO.getUserId()));
			momentsRespVO.setLikeList(likeMap.get(momentsRespVO.getId()) == null ? new ArrayList<>(0)
					: likeMap.get(momentsRespVO.getId()));
			momentsRespVO.setCommentList(commentMap.get(momentsRespVO.getId()) == null ? new ArrayList<>(0)
					: commentMap.get(momentsRespVO.getId()));
			momentsRespVO.setImages(
					imgMap.get(momentsRespVO.getId()) == null ? new ArrayList<>(0) : imgMap.get(momentsRespVO.getId()));
		}

	}

	/**
	 * 评论
	 * 
	 * @param userId
	 * @param momentsCommentEntity
	 */
	@Transactional
	public Long addMomentComment(String userId, MomentsCommentEntity momentsCommentEntity) {
		MomentsEntity momentsEntity = momentsEntityMapper.selectByPrimaryKey(momentsCommentEntity.getMomentsId());
		A.check(momentsEntity == null, "评论信息不存在或已经被删除");
		momentsCommentEntity.setUserId(userId);
		momentsCommentEntityMapper.insertSelective(momentsCommentEntity);

		List<Long> requestList = new ArrayList<>();
		requestList.add(momentsCommentEntity.getMomentsId());
		List<MomentsImgEntity> imgList = momentsImgEntityMapper.selectImgList(requestList);

		if (!userId.equals(momentsEntity.getUserId())) {
			String nick = null;
			String avater = null;
			String content = momentsCommentEntity.getContent();
			Date createTime = new Date();
			String targetUserId = null;

			if (momentsCommentEntity.getReplyId() != 0) {
				MomentsCommentEntity target = momentsCommentEntityMapper
						.selectByPrimaryKey(momentsCommentEntity.getReplyId());
				A.check(target == null, "评论对象不存在或已经被删除");
				UserEntity userEntity = userDAO.getById(userId);
				A.check(userEntity == null, "用户不存在");
				avater = userEntity.getAvatar();
				nick = groupService.getGroupFriendNick(target.getUserId(), userId);
				targetUserId = target.getUserId();
			} else {
				UserEntity userEntity = userDAO.getById(userId);
				A.check(userEntity == null, "用户不存在");
				avater = userEntity.getAvatar();
				nick = groupService.getGroupFriendNick(momentsEntity.getUserId(), userId);
				targetUserId = momentsEntity.getUserId();
			}

			MomentsPushVO momentsPushVO = new MomentsPushVO(momentsEntity.getId(), avater, nick, createTime, content,
					false, momentsEntity.getContent(), CollectionUtils.isEmpty(imgList) ? "" : imgList.get(0).getUrl());
			rongCloudService.pushToFriendMoments(userId, targetUserId,
					new MomentsMessage(JSONUtil.toJSONString(momentsPushVO)));
		}

		return momentsCommentEntity.getId();
	}

	public void likeMoments(String userId, Long momentsId) {
		MomentsEntity momentsEntity = momentsEntityMapper.selectByPrimaryKey(momentsId);
		A.check(momentsEntity == null, "评论信息不存在或已经被删除");
		MomentsLikeEntity momentsLikeEntity = momentsLikeEntityMapper.selectByUserIdAndMomentsId(userId, momentsId);
		if (momentsLikeEntity == null) {

			List<Long> requestList = new ArrayList<>();
			requestList.add(momentsId);
			List<MomentsImgEntity> imgList = momentsImgEntityMapper.selectImgList(requestList);

			momentsLikeEntityMapper.insert(new MomentsLikeEntity(momentsId, userId));
			if (!userId.equals(momentsEntity.getUserId())) {
				Date createTime = new Date();
				UserEntity userEntity = userDAO.getById(momentsEntity.getUserId());
				A.check(userEntity == null, "用户不存在");
				MomentsPushVO momentsPushVO = new MomentsPushVO(momentsEntity.getId(), userEntity.getAvatar(),
						groupService.getGroupFriendNick(momentsEntity.getUserId(), userId), createTime, "", true,
						momentsEntity.getContent(), CollectionUtils.isEmpty(imgList) ? "" : imgList.get(0).getUrl());
				rongCloudService.pushToFriendMoments(userId, momentsEntity.getUserId(),
						new MomentsMessage(JSONUtil.toJSONString(momentsPushVO)));
			}
		} else {
			momentsLikeEntityMapper.deleteByPrimaryKey(momentsLikeEntity.getId());
		}
	}

	public void delMoments(String userId, Long momentsId) {
		MomentsEntity momentsEntity = momentsEntityMapper.selectByPrimaryKey(momentsId);
		A.check(momentsEntity == null || !momentsEntity.getUserId().equals(userId), "记录不存在或已经删除");
		momentsEntityMapper.deleteByPrimaryKey(momentsId);
		momentsLikeEntityMapper.deleteByMomentsId(momentsId);
		momentsCommentEntityMapper.deleteByMomentsId(momentsId);
		momentsImgEntityMapper.deleteByMomentsId(momentsId);
	}
}
