package cn.kt.mall.management.admin.dao;

import cn.kt.mall.management.admin.entity.UserOperatorLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserOperatorLogDAO {

    /**
     * 添加日志记录
     * @param userOperatorLog
     */
    int add(UserOperatorLogEntity userOperatorLog);


}
