package cn.kt.mall.common.bitcoin.mapper;

import cn.kt.mall.common.bitcoin.entity.BtcAddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BtcAddressDAO {

    //添加地址
    int add(BtcAddressEntity entity);

    //查询地址
    BtcAddressEntity getAddress(@Param("address") String address);

    //获取所有地址
    List<BtcAddressEntity> getAddressList();

    //获取币的地址
    List<BtcAddressEntity> getAddressByName(@Param("name")String name);
}
