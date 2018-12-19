package cn.kt.mall.management.user.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.user.common.UserConstants;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.RegexUtil;
import cn.kt.mall.management.user.service.MgrUserService;
import cn.kt.mall.management.user.vo.RelationshipVO;
import cn.kt.mall.management.user.vo.UserAddressVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户业务实现类
 * Created by wqt on 2018/2/2.
 */
@Service
public class MgrUserServiceImpl implements MgrUserService {

    private UserDAO userDAO;

    @Autowired
    public MgrUserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserAddressVO getAddress(String target, String type) {
        A.checkParam(target == null, "手机号或用户名不能为空");
        UserEntity userEntity = null;
        if(RegexUtil.checkUsername(target)) {
            userEntity = this.userDAO.getByUsername(target);
        } else {
            userEntity = this.userDAO.getByMobile(target);
        }
        A.check(userEntity == null, "目标用户不存在");
        A.check(type == null || !"lem,aec,btc,ltc".contains(type), "不支持的账户类型");


        return null;
    }

    /**
     * 获取直接下级信息
     * @param userId 用户Id
     * @return List<RelationshipVO>
     */
    @Override
    public List<RelationshipVO> getDirectSlave(String userId) {
        List<RelationshipVO> result = new ArrayList<>();
        if (userId == null) {
            UserEntity entity = this.userDAO.getById(UserConstants.GENESIS);
            result.add(new RelationshipVO((entity)));
            return result;
        }

        int slaveGeneration = this.getUserGeneration(userId) + 1;

        List<UserEntity> entities = this.userDAO.getSlaveList(userId);
        if(entities == null || entities.size() == 0) {
            return result;
        }

        for (UserEntity entity : entities) {
            result.add(new RelationshipVO(entity, slaveGeneration));
        }

        return result;
    }

    private int getUserGeneration(String userId) {
        int generation = 0;

        UserEntity entity = this.userDAO.getById(userId);
        if(entity == null) {
            return generation;
        }

        do {
            if(entity.getReferrer() == null || UserConstants.GENESIS.equals(entity.getId())) {
                break;
            }

            generation++;
            entity = this.userDAO.getById(entity.getReferrer());
        } while (entity != null);

        return generation;
    }
}
