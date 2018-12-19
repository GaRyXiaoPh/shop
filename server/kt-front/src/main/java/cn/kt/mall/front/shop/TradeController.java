package cn.kt.mall.front.shop;

import java.util.Date;
import java.util.List;

import cn.kt.mall.front.user.service.MyAssetService;
import cn.kt.mall.shop.good.service.GoodService;
import cn.kt.mall.shop.shop.vo.ShopTransportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Result;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.front.password.service.TransactionPasswordService;
import cn.kt.mall.shop.logistics.service.LogisticsService;
import cn.kt.mall.shop.logistics.vo.LogisticsReqVO;
import cn.kt.mall.shop.logistics.vo.LogisticsRespVO;
import cn.kt.mall.shop.shop.service.TradeCommentService;
import cn.kt.mall.shop.trade.service.TradeService;
import cn.kt.mall.shop.trade.vo.SubmitTradeBatchVO;
import cn.kt.mall.shop.trade.vo.TradeRespVO;
import cn.kt.mall.shop.trade.vo.TradeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "shop-交易管理", tags = "shop-trade")
@RequestMapping("/shop/trade")
@RestController
public class TradeController {
	@Autowired
	TradeService tradeService;
	@Autowired
	TransactionPasswordService transactionPasswordService;
	@Autowired
	TradeCommentService tradeCommentService;
	@Autowired
	SysSettingsService sysSettingsService;
	@Autowired
	LogisticsService logisticsService;
	@Autowired
	MyAssetService myAssetService;
	@Autowired
	GoodService goodService;

	// 批量提交订单
	@ApiOperation(value = "批量提交订单-NEW", notes = "")
	@PostMapping("addBatch")
	@ResponseBody
	public TradeRespVO addTradeBatch(@RequestBody SubmitTradeBatchVO submitTradeBatchVO) {
		String userId = SubjectUtil.getCurrent().getId();
		TradeRespVO tradeRespVO = tradeService.tradeBatch(userId, submitTradeBatchVO);
		return tradeRespVO;
	}
	// 立即购买判断店铺信用金接口
	@ApiOperation(value = "立即购买", notes = "")
	@PostMapping("buyCheck")
	@ResponseBody
	public Success buyCheck(@RequestParam("shopId") String  shopId,@RequestParam("goodId") String  goodId,@RequestParam("goodName") String  goodName,@RequestParam("buyNum") int  buyNum) {
		//String sysGoodName =  sysSettingsService.getGoodName();
		/*if(goodName.equals("正宇汽车")){
			goodService.checkShopSalesAmountByShopIdAndGoodId(shopId,goodId,buyNum);
		}*/
		//myAssetService.checkShopAsset(shopId);
		return Response.SUCCESS;
	}
	// 提交并支付订单
	@ApiOperation(value = "提交并支付订单-NEW", notes = "")
	@PostMapping("addBatchAndPayTrade")
	@ResponseBody
	public Success addBatchAndPayTrade(@RequestBody SubmitTradeBatchVO submitTradeBatchVO,@RequestParam("password") String password) {
		String userId = SubjectUtil.getCurrent().getId();
		transactionPasswordService.check(userId, password);
		tradeService.tradeBatchAndPayTrade(userId, submitTradeBatchVO);
		return Response.SUCCESS;
	}

	// 我的订单
	@ApiOperation(value = "我的订单-NEW", notes = "")
	@GetMapping("list")
	@ResponseBody
	public PageVO<TradeVO> myTrade(@RequestParam(value = "status", required = false) String status,
			@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
		return tradeService.myTrade(SubjectUtil.getCurrent().getId(), status, pageNo, pageSize);
	}

	// 订单详情
	@ApiOperation(value = "订单详情-NEW", notes = "")
	@GetMapping("detailsTrade")
	@ResponseBody
	public TradeVO detailsTrade(@RequestParam(value = "tradeId") String tradeId) {
		return tradeService.detailsTrade(null, SubjectUtil.getCurrent().getId(), tradeId);
	}

	// 支付订单
	@ApiOperation(value = "支付订单,根据内部订单编号支付(此接口用于购物车批量提交之后跳转的页面进行支付)--NEW", notes = "密码密文：sha256hash(密码)")
	@PostMapping("payTradeByInteriorNo")
	@ResponseBody
	public Success payTradeByInteriorNo(@RequestParam("tradeNo") String interiorNo,
			@RequestParam("password") String password) {
		String userId = SubjectUtil.getCurrent().getId();
		transactionPasswordService.check(userId, password);
		tradeService.payTradeByInteriorNo(userId, interiorNo);
		return Response.SUCCESS;
	}

	// 支付订单
	@ApiOperation(value = "支付订单,根据订单编号支付（此接口用于在我的订单列表支付单个订单）--NEW", notes = "密码密文：sha256hash(密码)")
	@PostMapping("payTradeByTradeNo")
	@ResponseBody
	public Success payTradeByTradeNo(@RequestParam("tradeNo") String tradeNo,
			@RequestParam("password") String password) {
		String userId = SubjectUtil.getCurrent().getId();
		transactionPasswordService.check(userId, password);
		tradeService.payTradeByTradeNo(userId, tradeNo);
		return Response.SUCCESS;
	}

	// 取消订单
	@ApiOperation(value = "取消订单--NEW", notes = "")
	@PostMapping("cancelTrade")
	@ResponseBody
	public Success cancelTrade(@RequestParam("tradeId") String tradeId) {
		tradeService.cancelTrade(null, SubjectUtil.getCurrent().getId(), tradeId);
		return Response.SUCCESS;
	}

	// 删除订单
	@ApiOperation(value = "删除订单-NEW", notes = "")
	@PostMapping("delTrade")
	@ResponseBody
	public Success delTrade(@RequestParam("tradeId") String tradeId) {
		tradeService.delTrade(SubjectUtil.getCurrent().getId(), tradeId);
		return Response.SUCCESS;
	}

	@ApiOperation(value = "催单，提醒发货-NEW", notes = "")
	@PostMapping("remindTrade")
	@ResponseBody
	public Success remindTrade(@RequestParam("tradeId") String tradeId) {
		tradeService.remindTrade(SubjectUtil.getCurrent().getId(), tradeId);
		return Response.SUCCESS;
	}

	// 申请退款
	@ApiOperation(value = "申请退款--NEW", notes = "logValue：退款原因")
	@PostMapping("backTrade")
	@ResponseBody
	public Success cancelTrade(@RequestParam("tradeId") String tradeId, @RequestParam("logValue") String logValue,
			@RequestParam(value = "img1", required = false) String img1,
			@RequestParam(value = "img2", required = false) String img2,
			@RequestParam(value = "img3", required = false) String img3) {
		tradeService.backTrade(SubjectUtil.getCurrent().getId(), tradeId, logValue, img1, img2, img3);
		return Response.SUCCESS;
	}



	@ApiOperation(value = "确认收货-NEW", notes = "")
	@GetMapping("recvTrade")
	@ResponseBody
	public Success recvTrade(@RequestParam("tradeId") String tradeId,
			@ApiParam("密码密文：sha256hash(密码)") @RequestParam(value = "password" ,required = false) String password) {
		String userId = SubjectUtil.getCurrent().getId();
		//transactionPasswordService.check(userId, password);
		tradeService.recvTrade(userId, tradeId, "确认收货");
		return Response.SUCCESS;
	}

	@ApiOperation(value = "订单评论-NEW", notes = "anonymous:“1”表示匿名评论,其他表示非匿名")
	@PostMapping("commentShopTrade")
	@ResponseBody
	public Success commentShopTrade(@RequestParam("tradeId") String tradeId, @RequestParam("text") String text,
			@RequestParam("anonymous") String anonymous, @RequestParam("score") Double score,
			@RequestParam("images") List<String> images) {
		tradeCommentService.commentShopTrade(SubjectUtil.getCurrent().getId(), tradeId, text, score, images,
				"1".equals(anonymous) ? (short) 1 : (short) 0);
		return Response.SUCCESS;
	}

	@ApiOperation(value = "回复评论-NEW", notes = "")
	@PostMapping("replyComment")
	@ResponseBody
	public Success replyComment(@RequestParam("tradeId") String tradeId, @RequestParam("pid") Long pid,
			@RequestParam("text") String text) {
		tradeCommentService.replyComment(pid, tradeId, SubjectUtil.getCurrent().getId(), text);
		return Response.SUCCESS;
	}

	@ApiOperation(value = "查询订单物流信息-NEW", notes = "")
	@GetMapping("getLogistics")
	@ResponseBody
	public LogisticsRespVO getLogistics(
			@ApiParam(required = true, value = "订单id") @RequestParam("tradeId") String tradeId,
			@ApiParam(required = true, value = "快递公司编码") @RequestParam("expCode") String expCode,
			@ApiParam(required = true, value = "物流单号") @RequestParam("expNo") String expNo) {
		LogisticsRespVO result = null;
		LogisticsReqVO logisticsReqVO = new LogisticsReqVO();
		logisticsReqVO.setCom(expCode);
		logisticsReqVO.setNum(expNo);
		result = logisticsService.getLogisticsInfo(logisticsReqVO);
		result.setCondition(tradeService.getAddressByTradeId(tradeId));
		if(result != null) {
			SysSettings sysSettings = sysSettingsService.getLogisticsById("logistics", expCode);
			if (sysSettings != null) {
				result.setCom(sysSettings.getMark());
			}
		}
		return result;
	}
	@ApiOperation(value = "查询商品物流信息-NEW", notes = "")
	@GetMapping("getGoodLogistics")
	@ResponseBody
	public ShopTransportVO getGoodLogistics(
			@ApiParam(required = true, value = "订单Id") @RequestParam("tradeId") String tradeId,
			@ApiParam(required = true, value = "店铺Id") @RequestParam("shopId") String shopId,
			@ApiParam(required = true, value = "商品") @RequestParam("goodId") String goodId) {
		return tradeService.getShopTransportByShopIdAndGoodIdFront( SubjectUtil.getCurrent().getId(),shopId,goodId,tradeId);
	}
	@ApiOperation(value = "获取服务器时间-NEW", notes = "")
	@GetMapping("getTime")
	@ResponseBody
	public Result<Date> getCurrentDate() {
		return new Result<>(new Date());
	}
}
