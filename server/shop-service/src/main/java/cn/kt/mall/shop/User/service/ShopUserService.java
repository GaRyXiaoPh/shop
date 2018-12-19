package cn.kt.mall.shop.User.service;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.shop.User.mapper.ShopUserDao;
import cn.kt.mall.shop.User.vo.UserRechargeVO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopUserService {
    @Autowired
    private ShopUserDao shopUserDao;
    @Autowired
    private ShopDAO shopDAO;
    @Autowired
    private UserDAO userDAO;

    public CommonPageVO<UserRechargeVO> getUserListByPid(String pid, String userName, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        ShopEntity shopEntity = shopDAO.getShopEntityByShopId(pid);
        List<UserRechargeVO> list = shopUserDao.getUserListByPid(shopEntity.getUserId(), userName, rowBounds);
        for (UserRechargeVO userRechargeVO : list) {
            if (userRechargeVO.getReferrer() != null && !userRechargeVO.getReferrer().equals("")) {
                UserEntity user = userDAO.getById(userRechargeVO.getReferrer());
                if (user != null) {
                    userRechargeVO.setReferrerName(user.getTrueName());
                    userRechargeVO.setReferrerMobile(user.getMobile());
                }
            }
        }
        PageInfo<UserRechargeVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    public UserEntity getById(String id) {

        return userDAO.getById(id);
    }
}
