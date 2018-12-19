package cn.kt.mall.management.admin.service;


import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.management.admin.entity.CouponEntity;
import cn.kt.mall.management.admin.vo.ExtractVO;
import cn.kt.mall.management.admin.vo.UserStatementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


public interface CouponService {

    int deleteCouponByIds( List<String> idsList);

    int  addCoupon(CouponEntity couponsEntity);

    int updateCouponById( String couponName,
                      BigDecimal ratio,
                        Integer couponType,String id,
                         Integer sendDays,
                          Integer isSend,Integer isDocking);

    int updateCoupon4sendDays(String id, Integer sendDays);

    List<CouponEntity> getCouponsList();

    CouponEntity getCouponEntityById(String id);

    List<CouponEntity> getCouponEntityBySendDay();

    /**
     * 查询消费返优惠券记录
     * @param beginTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<UserStatementVO> getReturnCoupon(String beginTime, String endTime,int pageNo, int pageSize);

    /**
     * 查询彩票、游戏积分提取记录
     * @param beginTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonPageVO<ExtractVO> getExtractList(String beginTime, String endTime, String mobile, String type, String status,String operateType,int pageNo, int pageSize);

    /**
     * 根据是否与第三方对接查询优惠券list
     * @param isDocking
     * @return
     */
    List<CouponEntity> getCouponListByIsDocking(String isDocking);

    /**
     * 审核彩票、游戏积分提取记录
     * @param ids
     * @param status
     */
    void passExtract(String ids, String status);

    /**
     * 导出查询彩票、游戏积分提取记录
     * @param extractVO
     * @return
     */
    List<ExtractVO> getExportExtractList(ExtractVO extractVO);
}
