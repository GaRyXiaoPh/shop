package cn.kt.mall.shop.address.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.kt.mall.shop.address.entity.AddressEntity;
import cn.kt.mall.shop.address.vo.AddressVO;

@Mapper
public interface AddressDAO {

    //添加用户收藏地址
    int addUserAddress(AddressEntity addressEntity);

    List<AddressVO> listAddressByIds(@Param("ids")List<String> ids, @Param("userId")String userId);

    int delUserAddress(@Param("id") String id, @Param("userId")String userId);

    //修改用户收藏地址
    int updateUserAddress(AddressEntity addressEntity);

    //取消默认地址
    int cancelUserDef(@Param("userId") String userId);
    int updateUserDef(@Param("id") String id, @Param("userId") String userId);

    //获取用户地址根据ID
    AddressEntity getUserAddressById(@Param("id")String id, @Param("userId") String userId);
    List<AddressVO> getUserAddress(@Param("userId")String userId);
}
