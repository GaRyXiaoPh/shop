package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.user.entity.CertificationEntity;
import cn.kt.mall.management.admin.dao.CertificationDAO;
import cn.kt.mall.management.admin.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 实名认证
 */
@Service("certificationService")
public class CertificationServiceImpl implements CertificationService {

    @Autowired
    private CertificationDAO certificationDAO;
    @Autowired
    private UserService userService;

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
    public PageVO<CertificationEntity> getCertificationList(String status, String trueName,
                                                            String startTime, String endTime,
                                                            Integer pageNo, Integer pageSize) {
        int srcPageNo = pageNo;
        if ( pageNo > 0 ) {
            pageNo = pageNo-1;
        }
        int offset = pageNo * pageSize;
        int count = certificationDAO.getCertificationListCount(status,trueName,startTime,endTime);
        List<CertificationEntity> list = certificationDAO.getCertificationList(status,trueName,startTime,endTime,offset,pageSize);
        return new PageVO<>(srcPageNo, pageSize, count, list);
    }

    /**
     * 编辑实名认证的审核状态
     * @param status 审核状态，0未审核，1已通过，2已拒绝，3未实名
     * @param userIds 用户ID集合：使用,分隔
     */
    public void editCertificationStatus(String status, String userIds) {
        A.check((null == userIds || userIds.equals("")), "请选择审核信息");
        String[] idArr = userIds.split(",");
        List<String> idsList = null;
        if (idArr.length > 0) {
            idsList = Arrays.asList(idArr);
        }
        int updateCount = certificationDAO.editCertificationStatus(status, idsList);
        A.check(updateCount <= 0, "审核失败");
        // 查询用户ID
        userService.updateCertificationStatus(userIds, status);
    }
}
