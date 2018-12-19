package cn.kt.mall.management.user.service;

import cn.kt.mall.management.user.vo.RelationshipVO;
import cn.kt.mall.management.user.vo.UserAddressVO;

import java.util.List;

/**
 * 用户服务接口
 * Created by wqt on 2018/2/2.
 */
public interface MgrUserService {

    /**
     * 获取用户区块链钱包地址
     * @param mobile 手机号
     * @param type 钱包类型
     * @return 钱包地址
     */
    UserAddressVO getAddress(String mobile, String type);

    /**
     * 获取直接下级信息
     * @param userId 用户Id
     * @return List<RelationshipVO>
     */
    List<RelationshipVO> getDirectSlave(String userId);
}
