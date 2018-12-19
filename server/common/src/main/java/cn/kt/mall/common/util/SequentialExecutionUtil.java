package cn.kt.mall.common.util;

import cn.kt.mall.common.function.IMComparable;
import cn.kt.mall.common.function.IMAccountTradeTask;
import cn.kt.mall.common.function.IMFunction;

import java.util.Arrays;

/**
 * 顺序执行工具类
 * Created by wqt on 2017/10/26.
 */
public class SequentialExecutionUtil {

    // 按照顺序更新两个注册现金余额，避免产生死锁
    public static void sequentialExecution(int compare, IMFunction firstFun, IMFunction secondFun) {
        if (compare < 0) {
            firstFun.execution();
            secondFun.execution();
            return;
        }
        secondFun.execution();
        firstFun.execution();
    }

    // 按照顺序更新两个注册现金余额，避免产生死锁
    public static void sequentialExecution(IMAccountTradeTask... tasks) {
        if(tasks == null || tasks.length == 0) {
            return;
        }

        Arrays.sort(tasks);

        for (IMAccountTradeTask task : tasks) {
            task.getImFunction().execution();
        }
    }

    // 按照顺序更新两个注册现金余额，避免产生死锁
    public static void sequentialExecution(IMComparable comparable, IMFunction expenseFun, IMFunction incomeFun) {
        sequentialExecution(comparable.compare(), expenseFun, incomeFun);
    }
}
