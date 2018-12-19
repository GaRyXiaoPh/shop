package cn.kt.mall.management.admin.dao;

import cn.kt.mall.management.admin.vo.FundVO;
import cn.kt.mall.management.admin.vo.fund.FundReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface FundDAO {

    /**
     * 查询资金列表
     * @param fundReqVO
     * @param rowBounds
     * @return
     */
    List<FundVO> getFundList(FundReqVO fundReqVO, RowBounds rowBounds);
}
