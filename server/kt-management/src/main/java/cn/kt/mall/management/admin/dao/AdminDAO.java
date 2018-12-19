package cn.kt.mall.management.admin.dao;

import cn.kt.mall.common.admin.model.AdminInfo;
import cn.kt.mall.management.admin.entity.AdminEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据操作类
 * Created by jerry on 2017/12/21.
 */
@Mapper
public interface AdminDAO {

    void add(AdminEntity entity);

    void updatePassword(@Param("id") String id, @Param("password") String password, @Param("salt") String salt);

    AdminEntity getById(@Param("id") String id);

    AdminEntity getByAccount(@Param("account") String account);

    List<AdminInfo> getUserList();

    int delete(@Param("userId") String userId);

    /**
     * 获取资金审核管理员信息
     * @return
     */
    List<AdminEntity> getAdminList();
}
