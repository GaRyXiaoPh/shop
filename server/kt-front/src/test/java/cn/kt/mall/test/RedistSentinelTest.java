package cn.kt.mall.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedistSentinelTest {
    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void readAnsWrite() throws InterruptedException {

        while (true) {

            try {
                redisTemplate.opsForHash().put("unit-test","test-key","value:" + System.currentTimeMillis());
                String value = (String)redisTemplate.opsForHash().get("unit-test","test-key");
                System.out.println("value: " + value);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            Thread.sleep(2000);
        }


    }
}
