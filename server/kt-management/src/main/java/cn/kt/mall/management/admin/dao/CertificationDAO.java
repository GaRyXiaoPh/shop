package cn.kt.mall.management.admin.dao;

import cn.kt.mall.common.user.entity.CertificationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 实名认证
 */
@Mapper
public interface CertificationDAO {

    /**
     * 获取实名认证列表长度
     * @param status 审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @param trueName 用户姓名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    int getCertificationListCount(@Param("status") String status,
                                  @Param("trueName") String trueName,
                                  @Param("startTime") String startTime,
                                  @Param("endTime") String endTime);

    /**
     * 获取实名认证列表
     * @param status 审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @param trueName 用户姓名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param offset
     * @param pageSize
     * @return
     */
    List<CertificationEntity> getCertificationList(@Param("status") String status,
                                                   @Param("trueName") String trueName,
                                                   @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime,
                                                   @Param("offset") Integer offset,
                                                   @Param("pageSize") Integer pageSize);

    /**
     * 编辑实名认证的审核状态
     * @param status 审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @param idsList 用户id集合：使用,分隔
     * @return
     */
    int editCertificationStatus(@Param("status") String status,
                                @Param("idsList") List<String> idsList);

}
