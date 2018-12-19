package cn.kt.mall.common.wallet.mapper;

import cn.kt.mall.common.wallet.vo.UnfreezeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 解冻管理dao
 * @author gwj
 */
@Mapper
public interface UnfreezeDAO {

    /**
     * 解冻列表
     * @param startDate
     * @param endDate
     * @param offset
     * @param pageSize
     * @return
     */
    List<UnfreezeVO> unfreezeList(@Param("startDate") String startDate,
                                  @Param("endDate") String endDate,
                                  @Param("offset") Integer offset,
                                  @Param("pageSize") Integer pageSize);

    /**
     * 查询解冻列表长度
     *
     * @param startDate
     * @param endDate
     * @return
     */
    int getUnfreezeCount(@Param("startDate") String startDate,
                         @Param("endDate") String endDate);

    int addUnfreezeLog(UnfreezeVO voi);

}
