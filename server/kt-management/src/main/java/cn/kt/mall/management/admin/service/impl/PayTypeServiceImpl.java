package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.management.admin.dao.PayTypeDAO;
import cn.kt.mall.management.admin.service.PayTypeService;
import cn.kt.mall.management.admin.vo.PayTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 支付类型
 */
@Service
public class PayTypeServiceImpl implements PayTypeService {

    @Autowired
    private PayTypeDAO payTypeDAO;

    /**
     * 支付类型查询
     * @return
     */
    public List<PayTypeVO> payTypeList(String goodId) {
        List<PayTypeVO> list = payTypeDAO.payTypeList(goodId);
        if(list.size() <= 1){
            PayTypeVO vo = new PayTypeVO();
            vo.setPayName("信用金+优惠券");
            list.add(vo);
        }
        return list;
    }

    /**
     * 支付比例修改
     * @param payTypeId 支付方式id
     * @param balanceRatio 余额比例值(%)
     * @param otherRatio 其它比例值(%)
     */
    public void updatePayRatio(String payTypeId, String balanceRatio, String otherRatio) {
        // 判断是否是余额+优惠券的方式
        A.check((null == payTypeId || !payTypeId.equals("2")), "只能修改余额+优惠券支付方式的比例");
        // 判断比例balanceRatio+otherRatio是否为100
        try{
            Double balanceRatioDouble = Double.parseDouble(balanceRatio);
            Double otherRatioDouble = Double.parseDouble(otherRatio);
            if((balanceRatioDouble + otherRatioDouble) != 100) {
                A.check(true, "请输入正确的百分比");
            }
            payTypeDAO.updatePayRatio(payTypeId, balanceRatio, otherRatio);
        }catch (Exception e) {
            A.check(true, "请输入正确的百分比");
        }
    }

    @Override
    public List<PayTypeVO> getPayRatio() {
        List<PayTypeVO> list = payTypeDAO.getPayRatio();
        for (PayTypeVO payTypeVO : list) {
            payTypeVO.setPayName("信用金:" + payTypeVO.getBalanceRatio().substring(0,5) + "% " + "优惠券支付:" + payTypeVO.getOtherRatio().substring(0,5) + "%");
        }
        return list;
    }
}
