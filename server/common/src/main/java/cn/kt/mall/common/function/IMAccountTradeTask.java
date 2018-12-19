package cn.kt.mall.common.function;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**账户交易任务
 * Created by wqt on 2017/11/5.
 */
@Getter
@Setter
@NoArgsConstructor
public class IMAccountTradeTask implements Comparable<IMAccountTradeTask> {
    /** 账户Id */
    private String accountId;

    /** 账户Id */
    private IMFunction imFunction;

    public IMAccountTradeTask(String accountId, IMFunction imFunction) {
        this.accountId = accountId;
        this.imFunction = imFunction;
    }

    @Override
    public int compareTo(IMAccountTradeTask o) {
        return this.accountId.compareTo(o.getAccountId());
    }
}
