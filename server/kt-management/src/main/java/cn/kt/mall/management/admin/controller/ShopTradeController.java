package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.excel.ExcelGenerator;
import cn.kt.mall.common.excel.ReportColumn;
import cn.kt.mall.common.excel.ReportData;
import cn.kt.mall.common.excel.ReportDefinition;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.shop.cash.vo.CashReqVO;
import cn.kt.mall.shop.cash.vo.CashRespVO;
import cn.kt.mall.shop.logistics.vo.LogisticsVO;
import cn.kt.mall.shop.trade.service.ShopTradeService;
import cn.kt.mall.shop.trade.vo.DeliveGoodsVO;
import cn.kt.mall.shop.trade.vo.ShopTradeReqVO;
import cn.kt.mall.shop.trade.vo.ShopTradeRespVO;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.shop.logistics.service.LogisticsService;
import cn.kt.mall.shop.logistics.vo.LogisticsReqVO;
import cn.kt.mall.shop.logistics.vo.LogisticsRespVO;
import cn.kt.mall.shop.trade.entity.TradeLogEntity;
import cn.kt.mall.shop.trade.service.TradeService;
import cn.kt.mall.shop.trade.vo.TradeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "物流管理", tags = "ShopTradeController")
@RequestMapping("/shopTrade/manage")
@RestController
public class ShopTradeController {

	@Autowired
	private TradeService tradeService;
	@Autowired
	private SysSettingsService sysSettingsService;
	@Autowired
	private LogisticsService logisticsService;
	@Autowired
	private ShopTradeService shopTradeService;

	@ApiOperation(value = "查询订单 有发货权限的门店的订单不返回")
	@PostMapping("searchTrades")
	public CommonPageVO<ShopTradeRespVO> listTrade(@ApiParam("1: 待发货 2: 待收货 3:已经完成") @RequestParam(name = "status", required = false) String status,
												   @RequestParam(name = "startTime", required = false) String startTime,
												   @RequestParam(name = "endTime", required = false) String endTime,
												   @RequestParam(required = false) String shopNo,
												   @RequestParam(required = false) String shopownerName,
												   @RequestParam("pageNo") int pageNo,
												   @RequestParam("pageSize") int pageSize) {
		ShopTradeReqVO shopTradeReqVO = new ShopTradeReqVO(status, startTime, endTime, shopNo, shopownerName, null);
		return shopTradeService.queryShopTradeList(shopTradeReqVO, pageNo, pageSize);
	}



	/**
	 * 批量发货处理
	 * @param deliveGoodsVO
	 * @return
	 */
	// 批量发货
	@ApiOperation(value = "订单批量发货")
	@PostMapping("deliveGoodsBatch")
	@ResponseBody
	public Success deliveGoodsBatch(@RequestBody DeliveGoodsVO deliveGoodsVO) {
		shopTradeService.deliveGoodsBatch(deliveGoodsVO);
		return Response.SUCCESS;
	}

	// 删除订单
//	@ApiOperation(value = "删除订单", notes = "")
//	@GetMapping("delTrade")
//	public Success delTrade(@RequestParam("tradeId") String tradeId, @RequestParam("shopId") String shopId) {
//		tradeService.delTradeSys(shopId, tradeId);
//		return Response.SUCCESS;
//	}

	// 订单详情
	@ApiOperation(value = "订单详情", notes = "")
	@GetMapping("detailsTrade")
	public TradeVO detailsTrade(@RequestParam(value = "tradeId") String tradeId,
			@RequestParam("shopId") String shopId) {
		return tradeService.detailsTrade(shopId, null, tradeId);
	}

	@ApiOperation(value = "物流公司列表", notes = "")
	@GetMapping("logisticsList")
	public List<LogisticsVO> getLogisticsList() {


		List<LogisticsVO>  logisticsVOList = shopTradeService.getLogisticsList();

		return logisticsVOList;
	}

	@ApiOperation(value = "查询订单物流信息", notes = "")
	@GetMapping("getLogistics")
	public LogisticsRespVO getLogistics(
			@ApiParam(required = true, value = "快递公司编码") @RequestParam("expCode") String expCode,
			@ApiParam(required = true, value = "物流单号") @RequestParam("expNo") String expNo) {
		LogisticsRespVO result = null;
		LogisticsReqVO logisticsReqVO = new LogisticsReqVO();
		logisticsReqVO.setCom(expCode);
		logisticsReqVO.setNum(expNo);
		result = logisticsService.getLogisticsInfo(logisticsReqVO);
		if (result != null) {
			SysSettings sysSettings = sysSettingsService.getLogisticsById("logistics", expCode);
			if (sysSettings != null) {
				result.setCom(sysSettings.getMark());
			}
		}
		return result;
	}

	@ApiOperation(value = "根据订单id查询查询订单物流信息", notes = "")
	@GetMapping("getLogisticsById")
	public LogisticsRespVO getLogisticsById(@RequestParam("tradeId") String tradeId,
			@RequestParam("shopId") String shopId) {
		LogisticsRespVO result = null;
		TradeLogEntity tradeLogEntity = tradeService.getLogisticsById(shopId, tradeId);
		A.check(tradeLogEntity == null, "订单还未发货");
		LogisticsReqVO logisticsReqVO = new LogisticsReqVO();
		logisticsReqVO.setCom(tradeLogEntity.getLogValue1());
		logisticsReqVO.setNum(tradeLogEntity.getLogValue2());
		result = logisticsService.getLogisticsInfo(logisticsReqVO);
		if (result != null) {
			SysSettings sysSettings = sysSettingsService.getLogisticsById("logistics", tradeLogEntity.getLogValue1());
			if (sysSettings != null) {
				result.setCom(sysSettings.getMark());
			}
		}
		return result;
	}

	// 取消订单
	@ApiOperation(value = "取消订单", notes = "")
	@PostMapping("cancelTrade")
	public Success cancelTrade(@RequestParam("tradeId") String tradeId, @RequestParam("shopId") String shopId) {
		tradeService.cancelTrade(shopId, null, tradeId);
		return Response.SUCCESS;
	}

	@ApiOperation(value = "导出订单列表数据")
	@IgnoreJwtAuth
	@GetMapping("exportTrade")
	@ResponseBody
	public void exportTrade(@ApiParam("1: 待发货 2: 待收货 3:已经完成") @RequestParam(name = "status", required = false) String status,
							   @RequestParam(name = "startTime", required = false) String startTime,
							   @RequestParam(name = "endTime", required = false) String endTime,
							   @RequestParam(name = "shopNo", required = false)  String shopNo,
							   @RequestParam(name = "shopownerName",required = false) String shopownerName,
							   @RequestParam(name = "excelName",required = false) String excelName,
							   @RequestParam(name="goodsName",required = false) String goodsName,
								@RequestParam(name="reciveName",required = false) String reciveName,
								@RequestParam(name="tradeNo",required = false) String tradeNo,
							   HttpServletResponse response)throws IOException, NoSuchFieldException, IllegalAccessException {

		ShopTradeReqVO shopTradeReqVO = new ShopTradeReqVO(
				StringUtils.isBlank(status) ? null : status,
				StringUtils.isBlank(startTime) ? null : startTime,
				StringUtils.isBlank(endTime) ? null : endTime,
				StringUtils.isBlank(shopNo) ? null : shopNo,
				StringUtils.isBlank(shopownerName) ? null : shopownerName,null);

		//ShopTradeReqVO shopTradeReqVO = new ShopTradeReqVO(status, startTime, endTime, shopNo, shopownerName, null);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8") + ".xls");

		List<ShopTradeRespVO> shopTradeList = shopTradeService.queryShopTradeList(shopTradeReqVO);


		List<ShopTradeRespVO> shopTradeNewList =new ArrayList<>();
		if(shopTradeList!=null&&shopTradeList.size()>0){
			for(ShopTradeRespVO vo:shopTradeList){

				if(vo.getStatus().equals("0")){
					vo.setStatus("待发货");
				}
				if(vo.getStatus().equals("1")){
					vo.setStatus("待收货-部分发货");
				}
				if(vo.getStatus().equals("2")){
					vo.setStatus("待收货");
				}
				if(vo.getStatus().equals("3")){
					vo.setStatus("已完成");
				}
				shopTradeNewList.add(vo);
			}
		}

		ExcelGenerator generator = new ExcelGenerator();
		ReportDefinition definition = new ReportDefinition(excelName);
		definition.addColumn(new ReportColumn("下单时间","createTime"));
		definition.addColumn(new ReportColumn("订单编号","tradeNo"));
		definition.addColumn(new ReportColumn("订单状态","status"));
		definition.addColumn(new ReportColumn("店铺ID","shopId"));
		definition.addColumn(new ReportColumn("店铺名称","shopName"));
		definition.addColumn(new ReportColumn("店长名称","name"));
		definition.addColumn(new ReportColumn("订单金额","totalPrice"));
		definition.addColumn(new ReportColumn("收货人","recvName"));
		definition.addColumn(new ReportColumn("配送地址","detailAddress"));
		definition.addColumn(new ReportColumn("收货人电话","recvMobile"));

		ReportData reportData = new ReportData(definition, shopTradeNewList);
		generator.addSheet(reportData);

		generator.write(response.getOutputStream());

		response.getOutputStream().flush();

	}

	/*@ApiOperation(value = "订单发货(这里可以进合单操作)")
	@PostMapping("deliveGoods")
	@ResponseBody
	public  Success deliveGoods(@RequestBody DeliveGoodsVO deliveGoodsVO {


		return Response.SUCCESS;

	}*/

}
