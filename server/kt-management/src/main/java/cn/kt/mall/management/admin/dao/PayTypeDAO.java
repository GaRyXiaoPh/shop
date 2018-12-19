package cn.kt.mall.management.admin.dao;

import cn.kt.mall.management.admin.vo.PayTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PayTypeDAO {

    /**
     * 支付类型查询
     * @return
     */
    List<PayTypeVO> payTypeList(@Param("goodId") String goodId);

    /**
     * 支付比例修改
     * @param id 支付方式id
     * @param balanceRatio 余额比例值(%)
     * @param otherRatio 其它比例值(%)
     */
    void updatePayRatio(@Param("id") String id,
                        @Param("balanceRatio") String balanceRatio,
                        @Param("otherRatio") String otherRatio);

    /**
     * 查询信用金+优惠券支付所有比例
     * @return
     */
    List<PayTypeVO> getPayRatio();
}
