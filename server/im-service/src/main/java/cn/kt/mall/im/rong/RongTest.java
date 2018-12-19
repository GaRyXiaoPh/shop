package cn.kt.mall.im.rong;

import cn.kt.mall.common.exception.ServerException;
import cn.kt.mall.common.util.JSONUtil;
import io.rong.RongCloud;
import io.rong.messages.CmdNtfMessage;
import io.rong.messages.InfoNtfMessage;
import io.rong.models.CodeSuccessResult;

/**
 * Created by Administrator on 2017/6/29.
 */
public class RongTest {

    RongCloud rongCloud = RongCloud.getInstance("lmxuhwaglzcud", "J4H0guU4iK");

    public static void main(String[] args) throws Exception {
        RongCloud rongCloud = RongCloud.getInstance("lmxuhwaglzcud", "J4H0guU4iK");

        String userId = "ab8b7e45227648d780c7c435fcfa9dda";
        String targetUserId = "e140da3445204a06bfd0854105fa33a1";

        /*Map<String, Object> data = new HashMap<>();
        data.put("friendId", userId);
        new RongTest().pushInfoToUser(userId, targetUserId, "FRIEND_DELETE", JSONUtil.getJSON(new RongMessage<String>(MessageType.FRIEND_DELETE, targetUserId)));*/

        System.out.println(JSONUtil.toJSONString(rongCloud.user.queryBlacklist("42d2fc2eae69426c9e0322e4a8e50492").getUsers()));
    }

    private CodeSuccessResult pushToGroup(String userId, String groupId, String info, Object data) throws Exception {
        return rongCloud.message.publishGroup(userId,
                new String[]{ groupId },
                new InfoNtfMessage(info, JSONUtil.toJSONString(data)),
                null,
                null,
                0,
                0,
                1);
    }


    public void pushInfoToUser(String fromUserId, String targetUserId, String cmdType, Object data) {
        try {
            rongCloud.message.publishPrivate(
                    fromUserId,
                    new String[]{targetUserId},
                    new CmdNtfMessage(cmdType, JSONUtil.toJSONString(data)),
                    null,
                    null,
                    "0",
                    0,
                    0,
                    0,
                    0
            );
        } catch (Exception e) {
            throw new ServerException("调用融云群聊发送用户CmdMsgMessage错误", e);
        }
    }
}
