package cn.kt.mall.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * uuid 工具类
 * Created by jerry on 2017/12/28.
 */
public class IDUtil {

    /**
     * 获取36位UUID字符串
     * @return UUID字符串
     */
    private static ReentrantLock lock =new ReentrantLock();

    public static String getUUID() {
        lock.lock();
       try {
            return UUID.randomUUID().toString();
        }finally {
           lock.unlock();
       }
    }


    public static String getOrderId() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        String head = "lmcxx";
        return head+newDate+result;
    }


    public static void main(String[] args) {

        //System.out.println("111111==="+getOrderId());
    }

}
