package cn.kt.mall.shop.coupon.service;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.shop.coupon.entity.UserCouponEntity;
import cn.kt.mall.shop.coupon.mapper.CouponsDAO;
import cn.kt.mall.shop.coupon.vo.CouponTransferVO;
import cn.kt.mall.shop.coupon.vo.ExtractVO;
import cn.kt.mall.shop.coupon.vo.TBUserCouponVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCouponEntityService {

    @Autowired
    private CouponsDAO couponsDAO;



    public UserCouponEntity getUserCouponEntityByUserIdAndCouponId(String userId,String counponId){
        return couponsDAO.getUserCouponEntity(userId,counponId);
    }

    public int addUserCouponEntity(UserCouponEntity userCouponEntity){
        return couponsDAO.addUserCouponEntity(userCouponEntity);
    }

    public int updateUserCouponEntity(UserCouponEntity userCouponEntity){
        return couponsDAO.updateUserCouponEntity(userCouponEntity);
    }
    public int updateUserCouponBySend(UserCouponEntity userCouponEntity){
        return couponsDAO.updateUserCouponBySend(userCouponEntity);
    }

    public int inserCouponTransfer(CouponTransferVO couponTransferVO){
        return couponsDAO.inserCouponTransfer(couponTransferVO);
    }


    public int inserCouponExtract(ExtractVO extractVO){
        return couponsDAO.inserCouponExtract(extractVO);
    }

    public CommonPageVO<ExtractVO> getCouponExtractLogList(String userId, String couponId,int pageNo ,int pageSize){
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<ExtractVO> list = couponsDAO.getCouponAllExtractLogList(userId,couponId,rowBounds);
        PageInfo<ExtractVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    public int updatePatchUserCouPonList(List<TBUserCouponVO> list){
        return couponsDAO.updatePatchUserCouPonList(list);
    }

    public int addPatchUserCouponList(List<UserCouponEntity> list){
        return couponsDAO.addPatchUserCouPonList(list);
    }

    public List<UserCouponEntity> getUserCouponEntityList(){
        return couponsDAO.getUserCouponEntityList();
    }

    public List<UserCouponEntity> getUserCouponEntityUserIdAndTypeList(){
        return couponsDAO.getUserCouponEntityUserIdAndTypeList();
    }
}
