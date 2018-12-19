package cn.kt.mall.mq.util;

import java.util.concurrent.TimeUnit;

public class MQHepler {

    private MQHepler() {
    }

    public static Integer getExpireHours(DelayInfo info) {
        if (info == null) {
            return 1;
        }
        Long delayMs = info.getScheduledDelayMs();
        Long period = info.getScheduledPeriod();
        Integer repeatNum = info.getScheduledRepeat();
        // int cycleNum = info.getCycleCount()
        if (delayMs == null) {
            delayMs = 0L;
        }
        if (period == null) {
            period = 1L;
        }
        if (repeatNum == null) {
            repeatNum = 1;
        }
        long totalMs = delayMs + repeatNum * period;
        return (int) TimeUnit.MILLISECONDS.toHours(totalMs);
    }
}