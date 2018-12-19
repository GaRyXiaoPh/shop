package cn.kt.mall.management.admin.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.management.admin.dao.LevelConfigDAO;
import cn.kt.mall.management.admin.service.LevelConfigService;
import cn.kt.mall.management.admin.vo.LevelConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LevelConfigServiceImpl implements LevelConfigService {

	@Autowired
	private LevelConfigDAO levelConfigDAO;

	@Override
	public List<LevelConfigVO> getLevelConfigList() {
		List<LevelConfigVO> list = levelConfigDAO.getLevelConfigList();
		return list;
	}

	@Override
	@Transactional
	public void updateLevelConfig(String id, BigDecimal amount, String configLevel) {
		int updateCount = levelConfigDAO.updateLevelConfig(id, amount, configLevel);
		A.check(updateCount <= 0, "更新失败");
	}

}
