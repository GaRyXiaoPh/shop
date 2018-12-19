package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.management.admin.dao.CouponTimeConfigDAO;
import cn.kt.mall.management.admin.entity.CouponTimeConfigEntity;
import cn.kt.mall.management.admin.service.CouponService;
import cn.kt.mall.management.admin.service.CouponTimeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CouponTimeConfigServiceImpl implements CouponTimeConfigService {

    @Autowired
    private CouponTimeConfigDAO couponTimeConfigDAO;

    @Override
    public List<CouponTimeConfigEntity> getCouponTimeConfigList() {
        return couponTimeConfigDAO.getCouponTimeConfigList();
    }

    @Override
    public void addCouponTimeConfig(List<String> dateList) {
        for(String dataStr:dateList){
            Date date = getDate(dataStr);
            CouponTimeConfigEntity couponTimeConfigEntity = new CouponTimeConfigEntity();
            couponTimeConfigEntity.setId(IDUtil.getUUID());
            couponTimeConfigEntity.setNotSendTime(date);
            int status = couponTimeConfigDAO.addCouponTimeConfig(couponTimeConfigEntity);
            A.check(status <= 0, "添加失败");
        }
    }

    /**
     * 字符串转为时间
     */
    public Date getDate(String dateStr){
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd" );
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    @Override
    public void delCouponTimeConfig(List<String> idList) {
        for(String id:idList){
            int status = couponTimeConfigDAO.delCouponTimeConfig(id);
            A.check(status <= 0, "删除失败");
        }
    }
}
