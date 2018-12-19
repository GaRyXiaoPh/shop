package cn.kt.mall.management.admin.service;

import cn.kt.mall.management.admin.entity.UserOperatorLogEntity;

import java.util.Date;

public interface UserOperatorLogService {

    int addUserOperatorLog(String userId, int operatorType, String OperatorName);
}
