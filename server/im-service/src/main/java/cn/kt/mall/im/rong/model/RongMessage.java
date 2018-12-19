package cn.kt.mall.im.rong.model;

/**
 * 融云推送消息基础模型类
 * Created by Administrator on 2017/6/29.
 */
public class RongMessage<T extends Object> {

    /** 消息类型 */
    private MessageType type;

    /** 消息内容 */
    private T content;

    public RongMessage(MessageType type, T content) {
        super();
        this.type = type;
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
