package cn.kt.mall.common.wallet.mapper;

import cn.kt.mall.common.wallet.entity.ContactEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContactDAO {

    //添加交易流水
    public int add(ContactEntity entity);

    public int del(@Param("userId")String userId, @Param("id")String id);

    public int update(@Param("userId")String userId, @Param("id")String id, @Param("nick")String nick, @Param("address")String address);

    public List<ContactEntity> getConstant(@Param("userId")String userId);
}
