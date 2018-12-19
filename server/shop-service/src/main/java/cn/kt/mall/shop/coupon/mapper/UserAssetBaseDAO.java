package cn.kt.mall.shop.coupon.mapper;

import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.shop.coupon.entity.UserAssetBaseEntity;
import cn.kt.mall.shop.coupon.vo.UserAssetBaseSearchVO;
import cn.kt.mall.shop.coupon.vo.UserAssetEntityVO;
import cn.kt.mall.shop.trade.entity.TradeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface UserAssetBaseDAO {
    ///查询没有释放完所有玩家信息
    List<UserAssetBaseEntity>  getUserAssetBaseLsitByassetType(UserAssetBaseSearchVO serachVo);


    /**
     * 修改导入的基本表信息
     * @param
     * @return
     */
    int updateUserAssetBaseByUserId(UserAssetBaseEntity userAssetBaseEntity);

    int updatePatchUserAssetBase(@Param("list") List<UserAssetBaseEntity> list);

    /**
     * 批量增加资产流水
     */
    int addPatchStatementEntity(@Param("list") List<StatementEntity> list);

    /**
     * 批量修改玩家资产总表
     */
    int updatePatchAssetByUserIdAndMoneny(@Param("list") List<UserAssetEntityVO> list);

    int getUserAssetBaseCountByAssetType(UserAssetBaseSearchVO serachVo);

}
