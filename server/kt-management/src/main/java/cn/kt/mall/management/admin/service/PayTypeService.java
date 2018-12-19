package cn.kt.mall.management.admin.service;

import cn.kt.mall.management.admin.vo.PayTypeVO;

import java.util.List;

/**
 * 支付类型
 */
public interface PayTypeService {

    /**
     * 支付类型查询
     * @return
     */
    List<PayTypeVO> payTypeList(String goodId);

    /**
     * 支付比例修改
     * @param payTypeId 支付方式id
     * @param balanceRatio 余额比例值(%)
     * @param otherRatio 其它比例值(%)
     */
    void updatePayRatio(String payTypeId,
                        String balanceRatio,
                        String otherRatio);

    /**
     * 查询信用金+优惠券支付比例
     * @return
     */
    List<PayTypeVO> getPayRatio();
}
