package cn.kt.mall.common.wallet.service;


import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.entity.ContactEntity;
import cn.kt.mall.common.wallet.mapper.ContactDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    ContactDAO contactDAO;

    //添加联系人
    public void add(String userId, String nick, String address){
        ContactEntity entity = new ContactEntity(IDUtil.getUUID(), userId, nick, address);
        contactDAO.add(entity);
    }

    //删除联系人
    public void del(String userId, String id){
        contactDAO.del(userId, id);
    }

    //修改联系人
    public void update(String userId, String id, String nick, String address){
        contactDAO.update(userId, id, nick, address);
    }

    //获取联系人
    public List<ContactEntity> getConstant(String userId){
        return contactDAO.getConstant(userId);
    }
}
