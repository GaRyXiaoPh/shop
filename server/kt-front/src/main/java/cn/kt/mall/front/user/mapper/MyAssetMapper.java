package cn.kt.mall.front.user.mapper;

import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.front.user.vo.MyAssetInfoVO;
import cn.kt.mall.front.user.vo.MyAssetVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 我的资产dao
 * @author gwj
 */
@Mapper
public interface MyAssetMapper {

    /**
     * 查询我的资产（现金+popc）
     * @param userId
     * @return
     */
    List<MyAssetVO> queryMyAsset(String userId);
    /**
     * 查询我的资余额
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
     * 查询我的资余额明细
     * @param userId
     * @return
     */
    List<StatementEntity> getMyAssetStatementList(String userId);
    /**
     * 查询交易列表长度
     *
     * @param userId
     * @return
     */
    int queryDealCount(String userId);

    /**
     * 查询提币列表
     * @param userId
     * @return
     */
    List<MyAssetInfoVO> queryDealByUserID(@Param("userId") String userId,
                                          @Param("offset") Integer offset,
                                          @Param("pageSize") Integer pageSize);

    /**
     * 查询提币列表长度
     *
     * @param userId
     * @return
     */
    int queryCashCount(String userId);

    /**
     * 查询提币列表
     * @param userId
     * @return
     */
    List<MyAssetInfoVO> queryCashByUserID(@Param("userId") String userId,
                                          @Param("offset") Integer offset,
                                          @Param("pageSize") Integer pageSize);
    /**
     * 查询充值&扣除列表长度
     *
     * @param userId
     * @return
     */
    int queryRechargeCount(String userId);

    /**
     * 查询充值&扣除列表
     * @param userId
     * @param offset
     * @param pageSize
     * @return
     */
    List<MyAssetInfoVO> queryRechargeByUserID(@Param("userId") String userId,
                                              @Param("offset") Integer offset,
                                              @Param("pageSize") Integer pageSize);

    /**
     * 查询解冻列表长度
     *
     * @param userId
     * @return
     */
    int queryUnfreezeCount(String userId);

    /**
     * 查询解冻列表
     * @param userId
     * @param offset
     * @param pageSize
     * @return
     */
    List<MyAssetInfoVO> queryUnfreezeByUserID(@Param("userId") String userId,
                                              @Param("offset") Integer offset,
                                              @Param("pageSize") Integer pageSize);

}
