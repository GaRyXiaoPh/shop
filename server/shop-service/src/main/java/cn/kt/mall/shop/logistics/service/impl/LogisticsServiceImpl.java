
package cn.kt.mall.shop.logistics.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.kt.mall.shop.logistics.entity.Logistics;
import cn.kt.mall.shop.logistics.entity.LogisticsDetail;
import cn.kt.mall.shop.logistics.mapper.LogisticsDetailMapper;
import cn.kt.mall.shop.logistics.mapper.LogisticsMapper;
import cn.kt.mall.shop.logistics.service.LogisticsBaseService;
import cn.kt.mall.shop.logistics.service.LogisticsService;
import cn.kt.mall.shop.logistics.vo.LogisticsReqVO;
import cn.kt.mall.shop.logistics.vo.LogisticsRespVO;
import cn.kt.mall.shop.logistics.vo.LogisticsSubReqVO;
import cn.kt.mall.shop.logistics.vo.ResultItem;

@Service
public class LogisticsServiceImpl implements LogisticsService {

	@Autowired
	private LogisticsMapper logisticsMapper;

	@Autowired
	private LogisticsDetailMapper logisticsDetailMapper;

	@Autowired
	private LogisticsBaseService logisticsBaseService;

	public static final String SUCCESS = "200";

	public static final String OK = "ok";

	public static final String FORM_DATA = "form-data";

	@Override
	public boolean updateLogisticsStatus(Logistics logistics) {
		if (logistics != null) {
			logisticsMapper.updateLogisticsStatus(logistics);
			return true;
		}
		return false;
	}

	@Override
	public List<LogisticsDetail> selectLogisticsDetail(String logisticsNo) {
		if (logisticsNo != null) {
			List<LogisticsDetail> selectLogisticsDetail = logisticsDetailMapper.selectLogisticsDetail(logisticsNo);
			return selectLogisticsDetail;
		}
		return null;
	}

	@Override
	public boolean addLogisticsDetail(LogisticsDetail logisticsDetail) {
		if (logisticsDetail != null) {
			LogisticsDetail exist = logisticsDetailMapper.selectOne(logisticsDetail);
			if (exist != null) {
				return true;
			}
			logisticsDetailMapper.addLogisticsDetail(logisticsDetail);
			return true;
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public LogisticsRespVO getLogisticsInfo(LogisticsReqVO logisticsReqVO) {
		LogisticsRespVO logisticsRespVO = new LogisticsRespVO();
		if (logisticsReqVO != null && logisticsReqVO.getNum() != null) {
			Logistics logistics = logisticsMapper.selectLogistics(logisticsReqVO.getNum());
			if (logistics != null && logistics.getCom() != null) {
				getLogisticsFromDB(logisticsReqVO, logisticsRespVO, logistics);
			} else {
				logisticsRespVO = getLogisticsFromRemote(logisticsReqVO);
			}
		}
		return logisticsRespVO;
	}

	private LogisticsRespVO getLogisticsFromRemote(LogisticsReqVO logisticsReqVO) {
		LogisticsRespVO logisticsRespVO;
		logisticsRespVO = logisticsBaseService.query(logisticsReqVO);
		if (logisticsRespVO != null && logisticsRespVO.getStatus().equals(SUCCESS)) {
			Logistics logisticsDomain = new Logistics();
			logisticsDomain.setLogisticsNo(logisticsRespVO.getNu());
			logisticsDomain.setLogisticsStatus(Integer.valueOf(logisticsRespVO.getState()));
			logisticsDomain.setCom(logisticsRespVO.getCom());
			logisticsMapper.addLogistics(logisticsDomain);

			ArrayList<ResultItem> data = logisticsRespVO.getData();
			Collections.reverse(data);
			for (ResultItem resultItem : data) {
				LogisticsDetail logisticsDetail = new LogisticsDetail();
				logisticsDetail.setLogisticsNo(logisticsRespVO.getNu());
				logisticsDetail.setLogisticsContext(resultItem.getContext());
				logisticsDetail.setFtime(resultItem.getFtime());
				logisticsDetailMapper.addLogisticsDetail(logisticsDetail);
			}
			Collections.reverse(data);
		}
		return logisticsRespVO;
	}

	private void getLogisticsFromDB(LogisticsReqVO logisticsReqVO, LogisticsRespVO logisticsRespVO,
			Logistics logistics) {
		logisticsRespVO.setCom(logistics.getCom());
		logisticsRespVO.setMessage(OK);
		logisticsRespVO.setNu(logistics.getLogisticsNo());
		logisticsRespVO.setState(String.valueOf(logistics.getLogisticsStatus()));
		logisticsRespVO.setStatus(SUCCESS);

		int count = logisticsMapper.getLogisticsCount(logisticsReqVO.getNum(), logisticsReqVO.getCom());
		if (count == 0) {
			return;
		}

		List<LogisticsDetail> logisticsDetails = logisticsDetailMapper.selectLogisticsDetail(logisticsReqVO.getNum());
		if (logisticsDetails != null && logisticsDetails.size() > 0) {
			ArrayList<ResultItem> list = new ArrayList<>();
			for (LogisticsDetail logisticsDetail : logisticsDetails) {
				ResultItem resultItem = new ResultItem();
				resultItem.setContext(logisticsDetail.getLogisticsContext());
				resultItem.setFtime(logisticsDetail.getFtime());
				resultItem.setTime(logisticsDetail.getFtime());
				list.add(resultItem);
			}

			logisticsRespVO.setData(list);
		}
	}

	@Override
	public boolean subscribeLogisticsInfo(LogisticsSubReqVO logisticsSubReqVO) {
		return logisticsBaseService.subscribe(logisticsSubReqVO);
	}

	@Override
	public boolean delLogisticsDetail(String logisticsNo) {
		if (!"".equals(logisticsNo)) {
			logisticsDetailMapper.delLogisticsDetail(logisticsNo);
			return true;
		}
		return false;
	}

	@Override
	public void addLogistice(String logisticsNo, String label) {
		if (logisticsMapper.getLogisticsCount(logisticsNo, label) == 0) {
			Logistics logistics = new Logistics();
			logistics.setCom(label);
			logistics.setLogisticsNo(logisticsNo);
			logistics.setLogisticsStatus(0);
			logisticsMapper.addLogistics(logistics);

			LogisticsSubReqVO logisticsSubReqVO = new LogisticsSubReqVO();
			logisticsSubReqVO.setNumber(logisticsNo);
			logisticsSubReqVO.setCompany(label);
			subscribeLogisticsInfo(logisticsSubReqVO);
		}
	}

}
