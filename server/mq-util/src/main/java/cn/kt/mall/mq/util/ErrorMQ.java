package cn.kt.mall.mq.util;

public class ErrorMQ extends Exception {

    public ErrorMQ(Exception e) {
        super(e);
    }

    private static final long serialVersionUID = 4645953244281998373L;
}
