package cn.kt.mall.mq.util;

public class Rollback extends RuntimeException {

    private static final long serialVersionUID = 797841856590915316L;

    public Rollback(String message) {
        super(message);
    }
}
