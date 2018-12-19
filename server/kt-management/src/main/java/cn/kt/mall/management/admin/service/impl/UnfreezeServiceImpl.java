package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.mapper.SysSettingsMapper;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.common.util.RegexUtil;
import cn.kt.mall.common.wallet.mapper.UnfreezeDAO;
import cn.kt.mall.common.wallet.vo.UnfreezeVO;
import cn.kt.mall.management.admin.service.UnfreezeService;
import cn.kt.mall.management.admin.vo.UnfreezeInfoVO;
import cn.kt.mall.shop.trade.mapper.ShopTradeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 解冻管理service实现类
 * @author gwj
 */
@Service("unfreezeService")
public class UnfreezeServiceImpl implements UnfreezeService {

    @Autowired
    private UnfreezeDAO unfreezeDAO;
    @Autowired
    private SysSettingsMapper settingsMapper;
    @Autowired
    private ShopTradeDAO shopTradeDAO;

    /**
     * 获取解冻列表
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageVO<UnfreezeVO> unfreezeList(Date startTime, Date endTime, Integer pageNo, Integer pageSize) {
        String startDate = null;
        String endDate = null;
        if ( startTime != null && endTime != null ) {
            startDate = DateUtil.getTime(startTime);
            endDate = DateUtil.getTime(endTime);
        }
        int srcPageNo = pageNo;
        if ( pageNo > 0 ) {
            pageNo = pageNo-1;
        }
        int offset = pageNo * pageSize;
        int count = unfreezeDAO.getUnfreezeCount(startDate,endDate);
        List<UnfreezeVO> list = unfreezeDAO.unfreezeList(startDate,endDate,offset,pageSize);
        return new PageVO<>(srcPageNo, pageSize, count, list);
    }

    /**
     * 查询当日营业额
     * @return
     */
    public UnfreezeInfoVO queryUnfreezeInfo() {
        UnfreezeInfoVO unfreezeInfoVO = new UnfreezeInfoVO();
        // 查询当日营业额
        BigDecimal cash = shopTradeDAO.selectTotalMoney(DateUtil.getDateString(new Date()));
        if(null == cash) {
            unfreezeInfoVO.setProfitCashToday(new BigDecimal(0));
        } else {
            unfreezeInfoVO.setProfitCashToday(cash);
        }
        return unfreezeInfoVO;
    }

}
