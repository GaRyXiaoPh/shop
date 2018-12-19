package cn.kt.mall.test;

import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.mapper.UserAssetDAO;
import cn.kt.mall.common.wallet.service.UserAssetService;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReleaseCoinTest {
    @Autowired
    private UserAssetService userAssetService;
    @Autowired
    private UserAssetDAO userAssetDAO;

    @Test
    public void release() throws InterruptedException {

        int pageNo = 1;
        int pageSize = 1000;
        String currency = "point";

        BigDecimal totalProfile = new BigDecimal("100000");
        BigDecimal totalReservedBalance = new BigDecimal("100000");
        BigDecimal releaseRatio = new BigDecimal("0.3");

        BigDecimal release = totalProfile.multiply(releaseRatio).divide(totalReservedBalance);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 1; i <= 10; ++i) {
            executorService.execute(new ReleaseTask(countDownLatch,i, userAssetService, userAssetDAO, release));
        }

        countDownLatch.await();

        executorService.shutdown();

    }

    public static class ReleaseTask implements Runnable {
        private UserAssetService userAssetService;
        private UserAssetDAO userAssetDAO;
        private int startPage = 1;
        BigDecimal releaseRatio;
        private String currency = "point";
        CountDownLatch countDownLatch;

        public ReleaseTask(CountDownLatch countDownLatch, int startPage, UserAssetService userAssetService,
                           UserAssetDAO userAssetDAO, BigDecimal releaseRatio) {
            this.startPage = startPage;
            this.userAssetDAO = userAssetDAO;
            this.userAssetService = userAssetService;
            this.releaseRatio = releaseRatio;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            int pageNo = startPage;
            List<UserAssetEntity> assetList = userAssetDAO.getAssetToRelease(currency,new RowBounds(pageNo, 1000));

            while (!assetList.isEmpty()) {
                for (UserAssetEntity u : assetList) {
                    //userAssetService.releaseCoin("2018-06-14", currency, new BigDecimal("10000000"), releaseRatio, u.getUserId(), u.getReservedBalance().multiply(releaseRatio));
                }

                pageNo += 10;
                assetList = userAssetDAO.getAssetToRelease(currency,new RowBounds(pageNo, 1000));
            }

            countDownLatch.countDown();
        }
    }
}
