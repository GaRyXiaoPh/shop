package cn.kt.mall.common.wallet.mapper;


import cn.kt.mall.common.wallet.entity.TransferEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransferDAO {

    //添加转账提现记录
    int add(TransferEntity entity);

    //获取记录
    List<TransferEntity> getTransfer(@Param("userId")String userId);

    //获取未确认的交易记录
    List<TransferEntity> getUnConfirmTransfer();

    int confirm(@Param("hash") String hash);
}
