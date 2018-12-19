package cn.kt.mall.mq.util;

public class DelayInfo {
    /**
     *
     * 延迟投递的时间
     * AMQ_SCHEDULED_DELAY   long
     */
    private Long scheduledDelayMs;
    /**
     * 重复投递的时间间隔
     * AMQ_SCHEDULED_PERIOD  long
     */
    private Long scheduledPeriod;
    /**
     * 重复投递次数
     * AMQ_SCHEDULED_REPEAT int
     */
    private int scheduledRepeat = 1;
    /**
     * Cron表达式
     * AMQ_SCHEDULED_CRON
     */
    private String scheduledCron;
    /**
     * 重试轮次
     * 0： 永远不停的发送，直道消费者消费成功
     * 大于0 ：循环指定的次数,默认只循环一次
     */
    private Integer cycleCount = 1;

    public long getScheduledDelayMs() {
        return scheduledDelayMs;
    }

    public DelayInfo setScheduledDelayMs(long scheduledDelayMs) {
        this.scheduledDelayMs = scheduledDelayMs;
        return this;
    }

    public long getScheduledPeriod() {
        return scheduledPeriod;
    }

    public DelayInfo setScheduledPeriod(long scheduledPeriod) {
        this.scheduledPeriod = scheduledPeriod;
        return this;
    }

    public int getScheduledRepeat() {
        return scheduledRepeat;
    }

    public DelayInfo setScheduledRepeat(int scheduledRepeat) {
        this.scheduledRepeat = scheduledRepeat;
        return this;
    }

    public String getScheduledCron() {
        return scheduledCron;
    }

    public DelayInfo setScheduledCron(String scheduledCron) {
        this.scheduledCron = scheduledCron;
        return this;
    }

    public Integer getCycleCount() {
        return cycleCount;
    }

    public DelayInfo setCycleCount(Integer cycleCount) {
        this.cycleCount = cycleCount;
        return this;
    }
}