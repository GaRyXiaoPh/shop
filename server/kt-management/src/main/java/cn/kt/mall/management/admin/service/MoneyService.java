package cn.kt.mall.management.admin.service;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.management.admin.vo.MoneyVO;

/**
 * 提现业务接口类
 * Created by jerry on 2017/12/21.
 */
public interface MoneyService {
    /**
     * 根据条件查询提现列表
     * @param status
     * @param name
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<MoneyVO> getMoneyList(String status, String name, String beginTime, String endTime, String timeType,String hasShop, int pageNo, int pageSize);

    /**
     * 更新提现状态
     * @param ids
     * @param status
     */
    void updateMoney(String ids,String status);
}
