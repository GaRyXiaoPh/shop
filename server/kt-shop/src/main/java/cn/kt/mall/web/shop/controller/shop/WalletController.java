package cn.kt.mall.web.shop.controller.shop;

import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.common.wallet.vo.UserAssetVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.WalletLemEntity;
import cn.kt.mall.common.wallet.service.StatementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(value = "用户-钱包管理", tags = "shop-wallet")
@RequestMapping("/wallet")
@RestController
public class WalletController {

	@Autowired
	UserAssetService userAssetService; // 钱包服务

	@Autowired
	StatementService statementService; // 账户流水服务

	@ApiOperation(value = "获取用户钱包")
	@GetMapping("wallet")
	@ShopAuth
	public List<UserAssetEntity> getWallet() {
		return userAssetService.getUserAsset(SubjectUtil.getCurrent().getId());
	}

	@ApiOperation(value = "获取账户流水")
	@GetMapping("getStatement")
	@ShopAuth
	public PageVO<StatementEntity> getStatement(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize) {
		return statementService.getStatement(SubjectUtil.getCurrent().getId(), pageNo, pageSize);
	}
	
	@ApiOperation(value = "统计月度收益")
	@GetMapping("countStatement")
	@ShopAuth
	public CommonPageVO<?> countStatement(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize) {
		return statementService.countStatement(SubjectUtil.getCurrentShop().getId(), pageNo, pageSize);
	}
}
