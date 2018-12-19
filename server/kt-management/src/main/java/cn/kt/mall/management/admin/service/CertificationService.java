package cn.kt.mall.management.admin.service;

import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.user.entity.CertificationEntity;

/**
 * 实名认证
 */
public interface CertificationService {

    /**
     * 获取实名认证列表
     * @param status 审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @param trueName 用户姓名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageNo 页数
     * @param pageSize 页码
     * @return
     */
    PageVO<CertificationEntity> getCertificationList(String status, String trueName,
                                                     String startTime, String endTime,
                                                     Integer pageNo, Integer pageSize);


    /**
     * 编辑实名认证的审核状态
     * @param status 审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @param userIds 用户ID集合：使用,分隔
     */
    void editCertificationStatus(String status, String userIds);

}
