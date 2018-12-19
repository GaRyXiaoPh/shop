package cn.kt.mall.common.bitcoin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.kt.mall.common.bitcoin.entity.BtcTransactionEntity;

@Mapper
public interface BtcTransactionDAO {

    //添加交易
    int add(BtcTransactionEntity entity);

    //获取交易记录,用于判断是否存在,防止重复插入
    BtcTransactionEntity getTransaction(@Param("hash")String hash);

}
