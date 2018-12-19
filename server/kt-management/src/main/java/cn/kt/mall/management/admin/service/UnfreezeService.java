package cn.kt.mall.management.admin.service;

import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.wallet.vo.UnfreezeVO;
import cn.kt.mall.management.admin.vo.UnfreezeInfoVO;

import java.util.Date;


/**
 * 解冻管理service接口
 * @author gwj
 */
public interface UnfreezeService {

    /**
     * 解冻列表
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageVO<UnfreezeVO> unfreezeList(Date startTime, Date endTime, Integer pageNo, Integer pageSize);

    /**
     * 查询当日营业额
     * @return
     */
    UnfreezeInfoVO queryUnfreezeInfo();

}
