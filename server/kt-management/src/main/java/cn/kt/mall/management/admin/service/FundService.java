package cn.kt.mall.management.admin.service;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.management.admin.vo.FundVO;
import cn.kt.mall.management.admin.vo.fund.DeliveFundsVO;
import cn.kt.mall.management.admin.vo.fund.FundReqVO;

/**
 * 资金管理业务接口类
 * Created by  on 2017/12/21.
 */
public interface FundService {
    /**
     * 查询资金列表
     * @param fundReqVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<FundVO> getFundList(FundReqVO fundReqVO, int pageNo, int pageSize);

    /**
     * 资金审核方法
     * @param id
     * @param status
     * @return
     */
    String passFund(String id,String status);

    /**
     * 批量修改审核状态
     *
     * @param deliveFundsVO
     */
    public void deliveFundsBatch(DeliveFundsVO deliveFundsVO);
}
