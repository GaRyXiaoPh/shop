package cn.kt.mall.shop.coupon.mapper;

import cn.kt.mall.shop.coupon.entity.UserAssetBaseEntity;
import cn.kt.mall.shop.coupon.vo.UserAssetBaseSearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReleaseUserAssetBaseDAO {

    List<UserAssetBaseEntity> getReleaseUserAssetBaseLsitByassetType(UserAssetBaseSearchVO searchVO);

    int getReleaseUserAssetBaseCountByAssetType(UserAssetBaseSearchVO searchVO);



}
