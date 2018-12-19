package cn.kt.mall.shop.address.service;


import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.shop.address.constant.Constants;
import cn.kt.mall.shop.address.entity.AddressEntity;
import cn.kt.mall.shop.address.mapper.AddressDAO;
import cn.kt.mall.shop.address.vo.AddressVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AddressService {

    @Autowired
    AddressDAO addressDAO;

    //添加用户地址
    public void addUserAddress(String userId, AddressVO addressVO){
        String id = addressVO.getId();
        String def = addressVO.getDef();

        //如果要添加地址是默认的则先取消原有默认
        if (Constants.AddressDef.ADDRESS_DEFAULT.equals(def)){
            addressDAO.cancelUserDef(userId);
        }

        //不存在则添加
        if (StringUtils.isBlank(id) || addressDAO.getUserAddressById(id,userId)==null){
            AddressEntity addressEntity = new AddressEntity();
            BeanUtils.copyProperties(addressVO, addressEntity);
            addressEntity.setId(IDUtil.getUUID());
            addressEntity.setUserId(userId);
            addressDAO.addUserAddress(addressEntity);
        } else {
            //修改
            AddressEntity addressEntity = new AddressEntity();
            BeanUtils.copyProperties(addressVO, addressEntity);
            addressEntity.setUserId(userId);
            addressDAO.updateUserAddress(addressEntity);
        }
    }

    //删除用户地址
    public void delUserAddress(String id, String userId){
        addressDAO.delUserAddress(id, userId);
    }

    //设置默认地址列表
    public void defUserAddress(String id, String userId){
        A.check(addressDAO.getUserAddressById(id,userId)==null,"ID不存在");
        addressDAO.cancelUserDef(userId);
        addressDAO.updateUserDef(id, userId);
    }

    //获取用户地址
    public AddressEntity getUserAddressById(String id, String userId){
        return addressDAO.getUserAddressById(id,userId);
    }
    //获取用户地址列表
    public List<AddressVO> getUserAddress(String userId){
        return addressDAO.getUserAddress(userId);
    }
}
