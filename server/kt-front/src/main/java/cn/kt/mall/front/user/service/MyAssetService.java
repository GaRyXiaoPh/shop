package cn.kt.mall.front.user.service;

import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.front.user.vo.MyAssetInfoVO;
import cn.kt.mall.front.user.vo.MyAssetVO;
import io.swagger.annotations.ApiParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 我的资产service接口
 * @author gwj
 */
public interface MyAssetService {

    /**
     * 查询我的资产（现金+popc）
     * @param userId
     * @return
     */
    MyAssetVO queryMyAsset(String userId);
    /**
     * 查询我的余额
     * @param userId
     * @return
     */
     UserAssetEntity getMyAsset(String userId);
    /**
     * 查询我的优惠券资产
     * @param userId
     * @return
     */
    UserAssetEntity getMyAssetPopc(String userId);

    /**
     * 申请提币
     * @param userId
     * @param count 提币数量
     */
    void applyPopcTurnOut(String userId,double count);

    /**
     * 查询我的资产明细记录
     * @param userId
     * @param type 0消费,1提币,2充值,3解冻
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageVO<MyAssetInfoVO> queryMyAssetList(String userId, int type, Integer pageNo, Integer pageSize);
    @Transactional
    void transferMyAsset( BigDecimal transferNumber,String mobile, String couponId, String password,String useType);

    void checkShopAsset(String shopId);

}
