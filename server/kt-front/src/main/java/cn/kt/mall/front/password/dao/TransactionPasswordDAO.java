package cn.kt.mall.front.password.dao;

import cn.kt.mall.front.password.entity.TransactionPasswordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 交易密码数据操作类
 * Created by wqt on 2018/1/29.
 */
@Mapper
public interface TransactionPasswordDAO {

    void add(TransactionPasswordEntity entity);

    void update(@Param("id") String id, @Param("password") String password, @Param("salt") String salt);

    TransactionPasswordEntity getByUserId(@Param("userId") String userId);
}
