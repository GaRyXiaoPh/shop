package cn.kt.mall.management.admin.dao;

import cn.kt.mall.management.admin.vo.MoneyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface MoneyDAO {
    /**
     * 查询提现列表
     * @return
     */
    List<MoneyVO> getMoneyList(@Param("statusList") List<String> statusList,
                                @Param("name") String name,
                                @Param("beginTime") String beginTime,
                                @Param("endTime") String endTime,
                                @Param("timeType") String timeType,
                                @Param("hasShop") String hasShop,
                                RowBounds rowBounds);

    /**
     * 更新提现状态
     * @return
     */
   int updateMoney(@Param("idsList") List<String> idsList,@Param("status") String status);

}
