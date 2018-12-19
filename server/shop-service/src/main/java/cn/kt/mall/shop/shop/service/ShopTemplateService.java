package cn.kt.mall.shop.shop.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.shop.shop.entity.ShopTemplateEntity;
import cn.kt.mall.shop.shop.mapper.ShopTemplateDAO;

@Service
public class ShopTemplateService {

	@Autowired
	private ShopTemplateDAO shopTemplateDAO;

	public CommonPageVO<ShopTemplateEntity> listTemplate(String shopId, int pageNo, int pageSize) {
		List<ShopTemplateEntity> list = shopTemplateDAO.listByPage(shopId, new RowBounds(pageNo, pageSize));
		PageInfo<ShopTemplateEntity> pageInfo = new PageInfo<>(list);
		return CommonUtil.copyFromPageInfo(pageInfo, list);
	}

	// 编辑物流模版
	public void addTemplate(ShopTemplateEntity shopTemplateEntity) {
		// 新增
		if (StringUtils.isEmpty(shopTemplateEntity.getId())) {
			shopTemplateEntity.setId(IDUtil.getUUID());
			shopTemplateDAO.insert(shopTemplateEntity);
		} else {
			shopTemplateDAO.updateByIdAndShopId(shopTemplateEntity);
		}
	}

	// 删除模版
	public void delTemplate(String shopId, String templateId) {
		shopTemplateDAO.deleteById(templateId, shopId);
	}
	
	public List<ShopTemplateEntity> getAllTemplate(String shopId){
		return shopTemplateDAO.getByShopId(shopId);
	}
}
