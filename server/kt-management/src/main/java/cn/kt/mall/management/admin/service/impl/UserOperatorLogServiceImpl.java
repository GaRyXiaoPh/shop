package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.management.admin.dao.AdminDAO;
import cn.kt.mall.management.admin.dao.UserOperatorLogDAO;
import cn.kt.mall.management.admin.entity.AdminEntity;
import cn.kt.mall.management.admin.entity.UserOperatorLogEntity;
import cn.kt.mall.management.admin.service.UserOperatorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserOperatorLogServiceImpl implements UserOperatorLogService {

   @Autowired
   private UserOperatorLogDAO userOperatorLogDAO;
   @Autowired
   private AdminDAO adminDAO;

    @Override
    public int addUserOperatorLog(String userId, int operatorType, String operatorName) {

        AdminEntity adminEntity =adminDAO.getById(userId);
        if(adminEntity == null){
            return 0;
        }
        UserOperatorLogEntity userOperatorLogEntity = UserOperatorLogEntity.userOperatorLogContract(userId,adminEntity.getAccount(),new Date(),adminEntity.getPosition(),
                operatorType,operatorName);
        userOperatorLogEntity.setId(IDUtil.getUUID());
        int count = userOperatorLogDAO.add(userOperatorLogEntity);
        A.check(count != 1,"添加日志失败");
        return count;
    }
}
