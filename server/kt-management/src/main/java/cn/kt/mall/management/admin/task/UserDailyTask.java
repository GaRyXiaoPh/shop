package cn.kt.mall.management.admin.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务类
 */
@Component
public class UserDailyTask {


    private static long times = 24 * 7 * 60 * 60 * 1000;

    /**
     * 每日优惠券的统计与发放每日凌晨发放
     */
    //  @Scheduled(cron="0 0 0 * * ?")
    /*@Scheduled(cron = "0/10 * *  * * ?")
    public void couponsDailyTask() {
        System.out.print("3444444444444444444444444444444");
    }*/
}