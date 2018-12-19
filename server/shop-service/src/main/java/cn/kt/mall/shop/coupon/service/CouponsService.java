package cn.kt.mall.shop.coupon.service;

import cn.kt.mall.common.asserts.A;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.IDUtil;

import cn.kt.mall.shop.coupon.entity.CouponEntity;
import cn.kt.mall.shop.coupon.entity.CouponsEntity;

import cn.kt.mall.shop.coupon.entity.UserCouponLogEntity;
import cn.kt.mall.shop.coupon.mapper.CouponsDAO;
import cn.kt.mall.shop.coupon.vo.CouponsVO;
import cn.kt.mall.shop.coupon.vo.UserCouponSearchVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class CouponsService {

	@Autowired
	private CouponsDAO couponsDAO;

	//查询优惠券列表/详细信息
	public CommonPageVO<CouponsVO> getCouponsList(String id, int pageNo, int pageSize) {
		List<CouponsVO> list = couponsDAO.getCouponsList(id,new RowBounds(pageNo, pageSize));
		PageInfo<CouponsVO> pageInfo = new PageInfo<>(list);
		return CommonUtil.copyFromPageInfo(pageInfo, list);
	}

	//添加/修改优惠券
	@Transactional
	public String addOrUpdateCoupons(CouponsVO couponsVO) {
		A.check(couponsVO == null, "未接收到添加参数");
		CouponsEntity couponsEntity = new CouponsEntity();
		BeanUtils.copyProperties(couponsVO, couponsEntity);
		int rows = 0;
		if (org.apache.commons.lang3.StringUtils.isEmpty(couponsVO.getId())) {
			couponsEntity.setId(IDUtil.getUUID().toString());
			couponsEntity.setCreateTime(new Date());
			rows = couponsDAO.addCoupon(couponsEntity);
			A.check(rows != 1, "添加优惠券失败");
		} else {
			rows = couponsDAO.updateCoupon(couponsEntity);
			A.check(rows != 1, "修改优惠券失败");
		}

		return couponsEntity.getId();

	}

	//删除优惠券
	public void deleteCoupons(String ids){
		A.check(ids == null || ids.equals(""), "入参异常");
		String[] idArray = ids.split(",");
		for (int i = 0; i < idArray.length; i++) {
			int count = couponsDAO.getIsOrNotByGoods(idArray[i]);
			A.check(count != 0, "删除失败,优惠券已被使用不能删除");
		}

		List<String> idsList = null;
		if (idArray.length > 0) {
			idsList = Arrays.asList(idArray);
		}
		int delCount = couponsDAO.deleteCoupons(idsList);
		A.check(delCount <= 0 || delCount != idArray.length , "删除失败,数据库显示数据与页面显示数据不符");
	}


	/**
	 * 获取优惠券列表，用于定时发送优惠券,查询出未发送完的信息
	 */
	public List<UserCouponLogEntity> getUserCouponsLogListByTime(UserCouponSearchVO searchVO){

		return couponsDAO.getUserCouponsLogListByTime(searchVO);
	}

	public int getUserCouponLogCountByTime(UserCouponSearchVO searchVO){
		return couponsDAO.getUserCoupobLogByTimeCount(searchVO);
	}

	/*public int updateUserCouponLogEntity(UserCouponLogEntity userCouponLogEntity){
		int i = couponsDAO.updateUserCouponLog(userCouponLogEntity);
		return i;
	}

	*//**
	 * 查询所有的优惠券列表
	 *//*
	public List<CouponEntity>  getCouponList(){
		return couponsDAO.getSysCouponsList();
	}*/

}
