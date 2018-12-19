package cn.kt.mall.common.wallet.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.mapper.UserAssetDAO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;

import cn.kt.mall.common.constant.CommonConstants;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.mapper.StatementDAO;
import cn.kt.mall.common.wallet.vo.CountStatementVO;

@Service
public class StatementService {

	private final static Logger logger = LoggerFactory.getLogger(StatementService.class);
	@Autowired
	StatementDAO statementDAO;
	@Autowired
	UserAssetService userAssetService;

	@Autowired
	UserAssetDAO userAssetDAO;

	// 生成账户流水
	public int addStatement(StatementEntity entity) {
		return statementDAO.add(entity);
	}
	// 购物赠送优惠券流水表加入商品Id
	public int addStatementGive(StatementEntity entity) {
		return statementDAO.addStatementGive(entity);
	}

	// 查询交易流水
	public PageVO<StatementEntity> getStatement(String userId, int pageNo, int pageSize) {
		int srcPageNo = pageNo;
		if (pageNo >= 1)
			pageNo = pageNo - 1;
		int offset = pageNo * pageSize;

		int count = statementDAO.getStatementCount(userId);
		List<StatementEntity> list = statementDAO.getStatement(userId, offset, pageSize);
		return new PageVO<>(srcPageNo, pageSize, count, list);
	}
	// 获取优惠券流水
	public List<StatementEntity> getUserStatementDetailList(String userId) {
		int count = statementDAO.getStatementCount(userId);
		List<StatementEntity> list = statementDAO.getUserStatementDetailList(userId);
		return list;
	}
	// 获取优惠券赠送（消费，推荐人）流水
	public CommonPageVO<StatementEntity>  getuserCouponConsumeDetailList(String userId,String couponId,int pageNo,int pageSize) {
		//int count = statementDAO.getStatementCount(userId);
		//UserAssetEntity userAssetEntity = userAssetDAO.getAssetBaseByUserIdAndCurrency(userId,"popc");
		/*StatementEntity StatementEntity = new StatementEntity();
		if(userAssetEntity == null){
			StatementEntity.setReservedAfter(new BigDecimal(0));
			StatementEntity.setCreateTime(new Date());
		}else{
			StatementEntity.setReservedAfter(userAssetEntity.getReservedBalance());
			StatementEntity.setCreateTime(userAssetEntity.getCreateTime());
		}
		StatementEntity.setTradeType((short) 14);*/
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<StatementEntity> list = new ArrayList<>();
		//list.add(StatementEntity);
		//List<StatementEntity> listStatementEntity = statementDAO.getuserCouponConsumeDetailList(userId, rowBounds);
		//list.addAll(listStatementEntity);
		list = statementDAO.getuserCouponConsumeDetailList(userId, rowBounds);
		if(list.size() > 0){
			for(StatementEntity statementEntity : list){
				statementEntity.setCouponId(couponId);
			}
		}
		PageInfo<StatementEntity> pageInfo = new PageInfo<>(list);
		return CommonUtil.copyFromPageInfo(pageInfo, list);
	}
	// 获取优惠消费转让流水
	public  CommonPageVO<StatementEntity>  getuserStatementTransferDetailList(String userId,int pageNo,int pageSize) {
		//int count = statementDAO.getStatementCount(userId);
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<StatementEntity> list = statementDAO.getUserStatementTransferDetailList(userId, rowBounds);
		PageInfo<StatementEntity> pageInfo = new PageInfo<>(list);
		return CommonUtil.copyFromPageInfo(pageInfo, list);
	}
	public int updateStatement(String id, String hash, Short status) {
		return statementDAO.updateStatement(id, hash, status);
	}


	public void updateByHash(String hash, Short status) {
		statementDAO.updateByHash(hash, status);
	}

	public CommonPageVO<CountStatementVO> countStatement(String shopId, int pageNo, int pageSize) {
		Date endDate;
		if (pageNo > 1) {
			endDate = DateUtil.minusTime(new Date(), (pageNo - 1) * pageSize + "M");
		} else {
			endDate = new Date();
		}
		Date startDate = DateUtil.minusTime(endDate, (pageSize - 1) + "M");
		List<CountStatementVO> list = statementDAO.countStatement(shopId, DateUtil.getMonth(startDate),
				DateUtil.getMonth(endDate), new RowBounds(pageNo, pageSize));
		PageInfo<CountStatementVO> pageInfo = new PageInfo<>(list);
		list = buildTradeCountData(list, startDate, endDate);
		return CommonUtil.copyFromPageInfo(pageInfo, list);
	}

	public List<CountStatementVO> countStatementByDay(String shopId) {
		Date endDate = new Date();
		Date startDate = DateUtil.minusTime(endDate, "6d");
		List<CountStatementVO> list = statementDAO.countStatementByDay(shopId, DateUtil.getDateString(startDate),
				DateUtil.getDateString(endDate));
		list = buildTradeCountDataByDay(list, startDate, endDate);
		return list;
	}
	
	private static List<CountStatementVO> buildTradeCountDataByDay(List<CountStatementVO> todayCountList, Date startTime,
			Date endTime) {
		Map<String, CountStatementVO> map = new HashMap<>();
		if (!CollectionUtils.isEmpty(todayCountList)) {
			for (CountStatementVO statementCountVO : todayCountList) {
				map.put(statementCountVO.getMonth(), statementCountVO);
			}
		} else {
			todayCountList = new ArrayList<>();
		}
		for (Date currentDate = startTime; !currentDate.after(endTime); currentDate = DateUtil.plusTime(currentDate,
				"1d")) {
			String key = DateUtil.getDateString(currentDate);
			CountStatementVO vo = map.get(key);
			if (vo == null) {
				vo = new CountStatementVO(key, CommonConstants.BIGDECIMAL_DEFAULT);
				todayCountList.add(vo);
			}
		}
		return todayCountList.stream().sorted(Comparator.comparing(CountStatementVO::getMonth))
				.collect(Collectors.toList());
	}

	private static List<CountStatementVO> buildTradeCountData(List<CountStatementVO> todayCountList, Date startTime,
			Date endTime) {
		Map<String, CountStatementVO> map = new HashMap<>();
		if (!CollectionUtils.isEmpty(todayCountList)) {
			for (CountStatementVO statementCountVO : todayCountList) {
				map.put(statementCountVO.getMonth(), statementCountVO);
			}
		} else {
			todayCountList = new ArrayList<>();
		}
		for (Date currentDate = startTime; !currentDate.after(endTime); currentDate = DateUtil.plusTime(currentDate,
				"1M")) {
			String key = DateUtil.getMonth(currentDate);
			CountStatementVO vo = map.get(key);
			if (vo == null) {
				vo = new CountStatementVO(key, CommonConstants.BIGDECIMAL_DEFAULT);
				todayCountList.add(vo);
			}
		}
		return todayCountList.stream().sorted(Comparator.comparing(CountStatementVO::getMonth))
				.collect(Collectors.toList());
	}

	/**
	 * 统计店铺累计收益
	 * 
	 * @param shopId
	 * @return
	 */
	public BigDecimal countByStatementLem(String shopId) {
		return statementDAO.countByStatementLem(shopId);
	}

	/**
	 * 用户基础资产释放记录
	 *
	 * @param
	 * @return
	 */
	public List<StatementEntity>  getUserAssetBaseReleaseStatementDetailList(String userId) {
		List<StatementEntity> listStatementEntityList = statementDAO.getUserAssetBaseReleaseStatementDetailList(userId);
		return listStatementEntityList;
	}



}
