package cn.kt.mall.front.user.controller;

import cn.kt.mall.front.user.service.impl.RedPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.front.password.service.TransactionPasswordService;
import cn.kt.mall.front.user.entity.RedpaperEntity;
import cn.kt.mall.front.user.vo.ClickRedRespVO;
import cn.kt.mall.front.user.vo.GetRedRespVO;
import cn.kt.mall.front.user.vo.RedReceivedDetailsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "红包", tags = "user-red-paper")
@RequestMapping("/red")
public class RedPaperController {

	@Autowired
	private RedPaperService redPaperService;
	@Autowired
	private TransactionPasswordService transactionPasswordService;

	@ApiOperation(value = "指定目标对象发红包 ", notes = "")
	@PostMapping("/sendRedPackage")
	@ResponseBody
	public RedpaperEntity sendRedPackage(
			@ApiParam(value = "targetId目标用户", required = true) @RequestParam("targetId") String targetId,
			@ApiParam(value = "amount红包金额（最多支持两位小数）", required = true) @RequestParam("amount") double amount,
			@ApiParam(value = "count红包数量", required = true) @RequestParam("count") int count,
			@ApiParam(value = "words留言", required = true) @RequestParam("words") String words,
			@ApiParam(value = "type红包类型：1，通用-单聊红包，2通用-群聊红包，3群聊拼手气红包", required = true) @RequestParam("type") Short type,
			@ApiParam(value = "password交易密码", required = true) @RequestParam("password") String password) {
		transactionPasswordService.check(SubjectUtil.getCurrent().getId(), password);
		return redPaperService.sendRedPackage(SubjectUtil.getCurrent().getId(), targetId, amount, count, words, type);
	}

	@ApiOperation(value = "获取红包状态 ", notes = "")
	@GetMapping("/getRedPackageState")
	@ResponseBody
	public GetRedRespVO getRedPackageState(
			@ApiParam(value = "redId红包id", required = true) @RequestParam("targetId") String redId) {
		return redPaperService.getRedPackage(SubjectUtil.getCurrent().getId(), redId);
	}

	@ApiOperation(value = "抢红包", notes = "")
	@GetMapping("/clickRedPackage")
	@ResponseBody
	public ClickRedRespVO clickRedPackage(
			@ApiParam(value = "redId红包id", required = true) @RequestParam("targetId") String redId) {
		//return redPaperService.clickRedPackage(SubjectUtil.getCurrent().getId(), redId);
		return null;
	}

	@ApiOperation(value = "获取红包详情", notes = "")
	@GetMapping("/getRedPackageDetail")
	@ResponseBody
	public CommonPageVO<RedReceivedDetailsVO> getRedPackageDetail(
			@ApiParam(value = "redId红包id", required = true) @RequestParam("targetId") String redId,
			@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
		return redPaperService.getRedPackageDetail(redId, pageNo, pageSize);
	}
}
