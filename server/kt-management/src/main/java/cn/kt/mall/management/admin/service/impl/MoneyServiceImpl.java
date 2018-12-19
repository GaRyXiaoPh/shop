package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.management.admin.dao.MoneyDAO;
import cn.kt.mall.management.admin.service.MoneyService;
import cn.kt.mall.management.admin.vo.MoneyVO;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class MoneyServiceImpl implements MoneyService {

	@Autowired
	private MoneyDAO moneyDAO;

	@Override
	public CommonPageVO<MoneyVO> getMoneyList(String status, String name, String beginTime, String endTime, String timeType,String hasShop, int pageNo, int pageSize) {
		List<String> statusList = null;
		if (!StringUtils.isEmpty(status)) {
			String[] statusArry = status.split(",");
			if (statusArry.length > 0) {
				statusList = Arrays.asList(statusArry);
			}
		}
		List<MoneyVO> list = moneyDAO.getMoneyList(statusList,name,beginTime,endTime,timeType,hasShop,new RowBounds(pageNo, pageSize));

		PageInfo<MoneyVO> pageInfo = new PageInfo<>(list);
		return CommonUtil.copyFromPageInfo(pageInfo, list);
	}

	@Transactional
	public void updateMoney(String ids,String status) {
		A.check(StringUtils.isBlank(ids), "id不能为空");
		A.check(StringUtils.isBlank(status), "状态不能为空");
		String[] idarry = ids.split(",");
		List<String> idsList = null;
		if (idarry.length > 0) {
			idsList = Arrays.asList(idarry);
		}
		int updateCount = moneyDAO.updateMoney(idsList, status);
		A.check(updateCount <= 0, "更新失败");
	}

}
