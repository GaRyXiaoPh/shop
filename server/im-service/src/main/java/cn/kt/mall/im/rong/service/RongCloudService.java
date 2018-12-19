package cn.kt.mall.im.rong.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import cn.kt.mall.common.exception.ServerException;
import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.im.rong.model.MessageType;
import cn.kt.mall.im.rong.model.RongMessage;
import cn.kt.mall.im.rong.model.RongUser;
import io.rong.RongCloud;
import io.rong.messages.CmdNtfMessage;
import io.rong.messages.ContactNtfMessage;
import io.rong.messages.InfoNtfMessage;
import io.rong.messages.MomentsMessage;
import io.rong.messages.RedInfoMessage;
import io.rong.models.CodeSuccessResult;
import io.rong.models.GroupUser;
import io.rong.models.GroupUserQueryResult;
import io.rong.models.QueryBlacklistUserResult;
import io.rong.models.TokenResult;


/**
 * 融云(即时通讯)服务类
 * Created by Administrator on 2017/6/18.
 */
@Service
public class RongCloudService {
    private static final Logger logger = LoggerFactory.getLogger(RongCloudService.class);

    /** 融云成功响应code */
    private static final int SUCCESS = 200;

    /** 缓存名称：黑名单列表 */
    private static final String CACHE_NAME_HATE_LIST = "HATE_LIST";

    @Autowired
    private RongCloud rongCloud;

    @Async
    public void createGroupChat(String groupId, String name, String[] members, String operatorId) {
        // 创建群组方法（创建群组，并将用户加入该群组，用户将可以收到该群的消息，同一用户最多可加入 500 个群，每个群最大至 3000 人，App 内的群组数量没有限制.注：其实本方法是加入群组方法 /group/join 的别名。）
        try {
            rongCloud.group.create(members, groupId, name);
            this.pushInfoToGroup(groupId, operatorId,
                    "\"" + name + "\"创建成功",
                    new RongMessage<String>(MessageType.GROUP_CREATE, groupId));
        } catch (Exception e) {
            throw new ServerException("调用融云创建群聊错误", e);
        }
    }

    /**
     * 刷新融云群聊名称
     * @param groupId 群聊Id
     * @param name 群聊名称
     */
    @Async
    public void refreshRongGroupName(String groupId, String name, String operatorId) {
        try {
            rongCloud.group.refresh(groupId, name);
            this.pushInfoToGroup(groupId, operatorId,
                    "群聊名称修改为：\"" + name + "\"",
                    new RongMessage<String>(MessageType.GROUP_RENAME, groupId));
        } catch (Exception e) {
            throw new ServerException("调用融云刷新群组接口失败", e);
        }
    }

    /**
     * 退出融云群聊
     * @param groupId 群聊Id
     * @param removes 移出的成员Ids
     */
    @Async
    public void quitGroupChat(String groupId, String[] removes) {
        try {
            rongCloud.group.quit(removes, groupId);
        } catch (Exception e) {
            throw new ServerException("调用融云移出群聊成员接口错误", e);
        }
    }

    /**
     * 加入融云群聊
     * @param groupId 群聊Id
     * @param groupChatName 群聊名称
     * @param adds 加入的成员Ids
     */
    @Async
    public void joinGroupChat(String groupId, String groupChatName, String[] adds, String[] names, String operatorId) {
        try {
            String tmpName = new String();
            for (int i=0; i < names.length; i++){
                tmpName=tmpName.concat(names[i]);
            }

            int max = 42;
            StringBuffer sb = new StringBuffer("\"");
            if(tmpName != null && tmpName.length() > max) {
                sb = new StringBuffer(tmpName.substring(0, max)).append("...\" 等").append(adds.length).append("人");
            } else {
                sb.append("\" ");
            }
            tmpName += "加入了群聊";
            rongCloud.group.join(adds, groupId, groupChatName);
            this.pushInfoToGroup(groupId, operatorId, tmpName,
                    new RongMessage<String>(MessageType.GROUP_ADD_MEMBER, groupId));
        } catch (Exception e) {
            throw new ServerException("调用融云添加群聊成员接口错误", e);
        }
    }

    //获取融云群成员
    @Async
    public List<GroupUser> getGroupMember(String groupId){
        try{
            GroupUserQueryResult groupUserQueryResult = rongCloud.group.queryUser(groupId);
            if (groupUserQueryResult.getCode() == SUCCESS){
                return groupUserQueryResult.getUsers();
            }
            logger.error("获取融云失败，失败原因：{ code:" + groupUserQueryResult.getCode() + ", message: " + groupUserQueryResult.toString() + "}");
        }catch(Exception e){
            throw new ServerException("调用融云群成员接口错误", e);
        }
        return null;
    }

    /**
     * 解散融云群聊
     * @param groupId 群聊Id
     * @param operatorId 操作人Id
     */
    @Async
    public void dismissGroupChat(String groupId, String operatorId) {
        try {
            this.rongCloud.group.dismiss(operatorId, groupId);
        } catch (Exception e) {
            throw new ServerException("调用融云群聊解散接口错误", e);
        }
    }

    /**
     * 向群组发送提示消息
     * 用于：创建群、公告修改、
     * @param groupId 群聊Id
     * @param userId 发送用户Id
     * @param info 消息内容
     * @param data 自定义数据
     */
    @Async
    public void pushInfoToGroup(String groupId, String userId, String info, RongMessage data) {
        try {
            rongCloud.message.publishGroup(userId,
                    new String[]{ groupId },
                    new InfoNtfMessage(info, JSONUtil.toJSONString(data)),
                    null,
                    null,
                    0,
                    0,
                    1);
        } catch (Exception e) {
            throw new ServerException("调用融云群聊发送群聊InfoNtfMessage错误", e);
        }
    }
    
    @Async
	public void pushToGroup(String userId, String groupId, String info, Object data) {
		try {
			rongCloud.message.publishGroup(userId, new String[] { groupId },
					new InfoNtfMessage(info, JSONUtil.toJSONString(data)), null, null, 0, 0, 1);
		} catch (Exception e) {
			throw new ServerException("调用融云群聊发送提示信息错误", e);
		}
	}
    

    @Async
	public void pushRedMessage(String userId, String groupId, String info, String name, String redType) {
		try {
			rongCloud.message.publishGroup(userId, new String[] { groupId },
					new RedInfoMessage(info, userId, name, System.currentTimeMillis(), redType), null, null, 0, 0, 1);
		} catch (Exception e) {
			throw new ServerException("调用融云群聊发送提示信息错误", e);
		}
	}
    
    @Async
	public void pushToFriend(String userId, String friendId, String info, Object data) {
		try {
			rongCloud.message.publishPrivate(
					userId,
                    new String[]{friendId},
                    new InfoNtfMessage(info, JSONUtil.toJSONString(data)),
                    null,
                    null,
                    "0",
                    0,
                    0,
                    0,
                    0
            );
		} catch (Exception e) {
			throw new ServerException("调用融云群聊发送提示信息错误", e);
		}
	}  
    
    @Async
	public void pushToFriendRed(String userId, String friendId, RedInfoMessage message) {
		try {
			rongCloud.message.publishPrivate(
					userId,
                    new String[]{friendId},
                    message,
                    null,
                    null,
                    "0",
                    0,
                    0,
                    0,
                    0
            );
		} catch (Exception e) {
			throw new ServerException("调用融云红包推送信息提示信息错误", e);
		}
	} 
    
    @Async
	public void pushToFriendMoments(String userId, String friendId, MomentsMessage message) {
		try {
			rongCloud.message.publishPrivate(
					userId,
                    new String[]{friendId},
                    message,
                    null,
                    null,
                    "0",
                    0,
                    0,
                    0,
                    0
            );
		} catch (Exception e) {
			throw new ServerException("调用融云朋友圈推送信息提示信息错误", e);
		}
	} 

    /**
     * 向用户发送通用指令消息
     * @param fromUserId 来源用户Id
     * @param targetUserId 目标用户Id
     * @param data 消息数据
     */
    @Async
    public void pushCmdToUser(String fromUserId, String targetUserId, RongMessage data) {
        try {
            rongCloud.message.publishPrivate(
                    fromUserId,
                    new String[]{targetUserId},
                    new CmdNtfMessage(data.getType().getCode(), JSONUtil.toJSONString(data)),
                    JSONUtil.toJSONString(data),
                    null,
                    "0",
                    0,
                    0,
                    0,
                    0
            );
        } catch (Exception e) {
            throw new ServerException("调用融云群聊发送用户CmdNtfMessage错误", e);
        }
    }

    //向用户添加联系人消息
    @Async
    public void pushContactNtfToUser(String fromUserId, String targetUserId, RongMessage<?> data){
        try {
            rongCloud.message.PublishSystem(
                    fromUserId,
                    new String[]{targetUserId},
                    new ContactNtfMessage(data.getType().getCode(),"", fromUserId, targetUserId, JSONUtil.toJSONString(data)),
                    JSONUtil.toJSONString(data),
                    null,
                    0,
                    0
            );
        }catch (Exception e){
            throw new ServerException("调用融云添加联系人ContactNtfMessage错误", e);
        }
    }
    
    //发送通用消息
    @Async
    public void pushCommonMessage(String fromUserId, String targetUserId, ContactNtfMessage contactNtfMessage, String pushContent){
        try {
            rongCloud.message.publishPrivate(
                    fromUserId,
                    new String[]{targetUserId},
                    contactNtfMessage,
                    pushContent,
                    null,
                    "0",
                    0,
                    0,
                    0,
                    0
            );
        }catch (Exception e){
            throw new ServerException("调用融云添加联系人ContactNtfMessage错误", e);
        }
    }

    /**
     * 获取融云令牌
     * @param user RongUser{userId(用户Id), name(用户名称), portraitUri(用户头像地址)}
     * @return
     */
    public String getToken(RongUser user) {
        // 获取 Token 方法
        try {
            TokenResult userGetTokenResult = rongCloud.user.getToken(user.getUserId(), user.getName(), user.getPortraitUri());
            if(userGetTokenResult.getCode() == SUCCESS) {
                return userGetTokenResult.getToken();
            }
            logger.error("获取融云token失败，失败原因：{ code:" + userGetTokenResult.getCode() + ", message: " + userGetTokenResult.getErrorMessage() + "}");
        } catch (Exception e) {
            throw new ServerException("获取融云token错误错误", e);
        }
        return null;
    }

    /**
     * 刷新融云令牌
     * @param user RongUser{userId(用户Id), name(用户名称), portraitUri(用户头像地址)}
     */
    public void refreshToken(RongUser user) {
        try {
            CodeSuccessResult userRefreshResult = rongCloud.user.refresh(user.getUserId(), user.getName(), user.getPortraitUri());
            if(userRefreshResult.getCode() == SUCCESS) {
                return;
            }
            logger.error("刷新融云token失败，失败原因：{ code:" + userRefreshResult.getCode() + ", message: " + userRefreshResult.getErrorMessage() + "}");
        } catch (Exception e) {
            throw new ServerException("获取融云token错误错误", e);
        }
    }

    /**
     * 获取用户黑名单
     * @param userId 用户Id
     * @return String[] 黑名单用户Id
     */
    public String[] getHateList(String userId) {
        try {
            QueryBlacklistUserResult result = rongCloud.user.queryBlacklist(userId);
            if(SUCCESS == result.getCode()) {
                return result.getUsers();
            }
        } catch (Exception e) {
            throw new ServerException("获取融云用户黑名单列表错误", e);
        }
        return null;
    }

    /**
     * 添加融云黑名单
     * @param userId 用户Id
     * @param hateId 黑名单用户Id
     * @return 调用结果：true 成功, false 失败
     */
    public boolean addHate(String userId, String hateId) {
        try {
            CodeSuccessResult result = rongCloud.user.addBlacklist(userId, hateId);
            return SUCCESS == result.getCode();
        } catch (Exception e) {
            throw new ServerException("添加融云用户黑名单错误", e);
        }
    }

    /**
     * 移除融云黑名单
     * @param userId 用户Id
     * @param hateId 黑名单用户Id
     * @return 调用结果：true 成功, false 失败
     */
    public boolean removeHate(String userId, String hateId) {
        try {
            CodeSuccessResult result = rongCloud.user.removeBlacklist(userId, hateId);
            return SUCCESS == result.getCode();
        } catch (Exception e) {
            throw new ServerException("移出融云用户黑名单错误", e);
        }
    }
}
