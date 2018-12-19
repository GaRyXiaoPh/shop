package cn.kt.mall.shop.User.mapper;

import cn.kt.mall.shop.User.vo.UserRechargeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface ShopUserDao {
    public List<UserRechargeVO> getUserListByPid(@Param("pid") String pid, @Param("userName") String userName, RowBounds rowBounds);

    /**
     * 根据推荐人查询用户信息
     */
    public UserRechargeVO getUserRechargeVOByReferrer(@Param("mobile") String referrer);

    public List<UserRechargeVO> getUserListByReferrer(@Param("pid") String pid, @Param("userName") String userName, RowBounds rowBounds);
}
