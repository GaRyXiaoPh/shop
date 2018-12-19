package cn.kt.mall.web.shop.controller.trade;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.kt.mall.common.excel.*;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.shop.User.service.ShopUserService;
import cn.kt.mall.shop.logistics.vo.LogisticsVO;
import cn.kt.mall.shop.logistics.vo.ShopTransportVO;
import cn.kt.mall.shop.trade.entity.TradeItemEntity;
import cn.kt.mall.shop.trade.service.ShopTradeService;
import cn.kt.mall.shop.trade.vo.*;
import cn.kt.mall.web.shop.vo.TradeGoodLogisticsVo;
import io.swagger.annotations.ApiModelProperty;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.entity.SysSettings;
import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.shop.logistics.service.LogisticsService;
import cn.kt.mall.shop.logistics.vo.LogisticsReqVO;
import cn.kt.mall.shop.logistics.vo.LogisticsRespVO;
import cn.kt.mall.shop.trade.constant.Constants;
import cn.kt.mall.shop.trade.entity.TradeEntity;
import cn.kt.mall.shop.trade.entity.TradeLogEntity;
import cn.kt.mall.shop.trade.service.TradeAbnormalService;
import cn.kt.mall.shop.trade.service.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;

@Api(value = "商家端订单模块", tags = "online-shop-trade")
@RequestMapping("/shop/trade")
@RestController
public class TradeController {

    @Autowired
    private TradeService tradeService;
    @Autowired
    private SysSettingsService sysSettingsService;
    @Autowired
    private LogisticsService logisticsService;
    @Autowired
    private ShopTradeService shopTradeService;
    @Autowired
    private ShopUserService shopUserService;

    @ApiOperation(value = "获取订单")
    @PostMapping("searchTrades")
    @ResponseBody
    @ShopAuth
    public CommonPageVO<TradeManageRespVO> listTrade(@RequestParam(name = "tradeNo", required = false) String tradeNo,
                                                     @RequestParam(name = "recvName", required = false) String recvName,
                                                     @RequestParam(name = "goodName", required = false) String goodName,
                                                     @RequestParam(name = "startTime", required = false) String startTime,
                                                     @RequestParam(name = "endTime", required = false) String endTime,
                                                     @RequestParam(name = "goodList", required = false) List<String> goodList,
                                                     @RequestParam(name = "status", required = false) String status,
                                                     @RequestParam("pageNo") int pageNo,
                                                     @RequestParam("pageSize") int pageSize) {
        TradeManageReqVO tradeManageReqVO = new TradeManageReqVO(SubjectUtil.getCurrentShop().getId(), tradeNo,
                goodName, recvName, startTime, endTime, status, goodList);
        tradeManageReqVO.setShopFlag((short) 0);
        return tradeService.listTrade(tradeManageReqVO, pageNo, pageSize);
    }


    @ApiOperation(value = "订单发货")
    @PostMapping("deliveGoods")
    @ResponseBody
    @ShopAuth
    public Success deliveGoods(@RequestParam(name = "tradeId") String tradeId,
                               @ApiParam(value = "快递列表label") @RequestParam(name = "label", required = false) String label,
                               @ApiParam(value = "物流编号") @RequestParam(name = "logisticsNo", required = false) String logisticsNo) {
        tradeService.deliveGoods(SubjectUtil.getCurrentShop().getId(), tradeId, label, logisticsNo, SubjectUtil.getCurrent().getId());
        return Response.SUCCESS;
    }

//    // 批量发货
//    @ApiOperation(value = "订单批量发货")
//    @PostMapping("deliveGoodsBatch")
//    @ResponseBody
//    @ShopAuth
//    public Success deliveGoodsBatch(@RequestBody DeliveGoodsVO deliveGoodsVO) {
//        tradeService.deliveGoodsBatch(SubjectUtil.getCurrentShop().getId(), deliveGoodsVO);
//        return Response.SUCCESS;
//    }

    // 删除订单
    @ApiOperation(value = "删除订单-商家端", notes = "")
    @GetMapping("delTrade")
    @ResponseBody
    @ShopAuth
    public Success delTrade(@RequestParam("tradeId") String tradeId) {
        tradeService.delTradeManage(SubjectUtil.getCurrentShop().getId(), tradeId);
        return Response.SUCCESS;
    }

    // 订单详情
    @ApiOperation(value = "订单详情", notes = "")
    @GetMapping("detailsTrade")
    @ResponseBody
    @ShopAuth
    public TradeVO detailsTrade(@RequestParam(value = "tradeId") String tradeId) {
        return tradeService.detailsTrade(SubjectUtil.getCurrentShop().getId(), null, tradeId);
    }

    @ApiOperation(value = "查询订单物流信息", notes = "")
    @GetMapping("getLogistics")
    @ResponseBody
    @ShopAuth
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
    @ResponseBody
    @ShopAuth
    public LogisticsRespVO getLogisticsById(@RequestParam("tradeId") String tradeId) {
        LogisticsRespVO result = null;
        TradeLogEntity tradeLogEntity = tradeService.getLogisticsById(SubjectUtil.getCurrentShop().getId(), tradeId);
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
    @ResponseBody
    @ShopAuth
    public Success cancelTrade(@RequestParam("tradeId") String tradeId) {
        tradeService.cancelTrade(SubjectUtil.getCurrentShop().getId(), null, tradeId);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "修改收货人信息", notes = "")
    @PostMapping("updateRecvDetails")
    @ResponseBody
    @ShopAuth
    public Success updateRecvDetails(
            @ApiParam(required = true, value = "收货人姓名") @RequestParam("recvName") String recvName,
            @ApiParam(required = true, value = "收货人电话") @RequestParam("recvMobile") String recvMobile,
            @ApiParam(required = true, value = "详细地址") @RequestParam("detailAddress") String detailAddress,
            @ApiParam(required = true, value = "邮政编码") @RequestParam("zipcode") String zipcode,
            @ApiParam(required = true, value = "订单id") @RequestParam("tradeId") String tradeId,
            @ApiParam(required = true, value = "省") @RequestParam("province") Long province,
            @ApiParam(required = true, value = "市") @RequestParam("city") Long city,
            @ApiParam(required = true, value = "区") @RequestParam("county") Long county) {
        tradeService.updateSelective(new TradeEntity(tradeId, SubjectUtil.getCurrentShop().getId(), detailAddress,
                recvName, recvMobile, zipcode, province, city, county));
        return Response.SUCCESS;
    }

    @ApiOperation(value = "物流公司列表", notes = "")
    @GetMapping("logisticsList")
    public List<LogisticsVO> getLogisticsList() {


        List<LogisticsVO> logisticsVOList = shopTradeService.getLogisticsList();

        return logisticsVOList;
    }


    @ApiOperation(value = "导出订单列表数据")
    @GetMapping("exportTrade")
    @ShopAuth
    public void exportTrade(@RequestParam(name = "myShopId", required = false) String myShopId,
                            @RequestParam(name = "tradeNo", required = false) String tradeNo,
                            @RequestParam(name = "recvName", required = false) String recvName,
                            @RequestParam(name = "goodName", required = false) String goodName,
                            @RequestParam(name = "startTime", required = false) String startTime,
                            @RequestParam(name = "endTime", required = false) String endTime,
                            @RequestParam(name = "status", required = false) String status,
                            @RequestParam(name = "excelName", required = true) String excelName,
                            HttpServletResponse response, HttpServletRequest request) throws IOException, NoSuchFieldException, IllegalAccessException {
        TradeManageReqVO tradeManageReqVO = new TradeManageReqVO(myShopId, tradeNo,
                goodName, recvName, startTime, endTime, status, null);
        tradeManageReqVO.setShopFlag((short) 0);

        ExportToExcelUtil<TradeManageRespVO> excelUtil = new ExportToExcelUtil<TradeManageRespVO>();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response, "订单导出");
            String[] headers = {"下单时间", "订单编号", "订单状态", "订单总额", "等级", "关系", "收货人", "收货人电话", "配送地址"};
            String[] columns = {"createTime", "interiorNo", "status", "totalPrice", "level", "relationship", "recvName", "recvMobile", "detailAddress"};
            List<TradeManageRespVO> shopTradeList = tradeService.getTradeManageRespVOList(tradeManageReqVO);

            List<TradeManageRespVO> shopTradeNewList = new ArrayList<>();

            for (TradeManageRespVO vo : shopTradeList) {

                if (vo.getStatus().equals("1")) {
                    vo.setStatus("待发货");
                }
                if (vo.getStatus().equals("4")) {
                    vo.setStatus("部分发货");
                }
                if (vo.getStatus().equals("2")) {
                    vo.setStatus("已发货");
                }
                if (vo.getStatus().equals("3")) {
                    vo.setStatus("已完成");
                }
                vo.setDetailAddress(vo.getProvinceValue()+vo.getCityValue()+vo.getCountyValue()+vo.getDetailAddress());

                shopTradeNewList.add(vo);
            }
            excelUtil.exportExcel(headers, columns, shopTradeNewList, out, request, "yyyy-MM-dd hh:mm:ss");
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


	/*@ApiOperation(value = "退款订单列表", notes = "")
    @PostMapping("drawBackList")
	@ResponseBody
	@ShopAuth
	public CommonPageVO<TradeAbnormalRespVO> drawBackList(
			@RequestParam(name = "tradeNo", required = false) String tradeNo,
			@RequestParam(name = "userName", required = false) String userName,
			@RequestParam(name = "startTime", required = false) String startTime,
			@RequestParam(name = "endTime", required = false) String endTime,
			@RequestParam(name = "status", required = false) String status, @RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize) {
		TradeManageReqVO tradeManageReqVO = new TradeManageReqVO(SubjectUtil.getCurrentShop().getId(), tradeNo,
				userName, null, startTime, endTime, status);
		tradeManageReqVO.setLogStatus(Constants.TradeLogCode.LOG_MONEY_BACK);
		tradeManageReqVO.setShopFlag((short) 0);
		return tradeAbnormalService.problemTradeList(tradeManageReqVO, pageNo, pageSize);
	}*/

	/*@ApiOperation(value = "同意退款", notes = "")
    @PostMapping("agreeDrawBack")
	@ResponseBody
	@ShopAuth
	public Success agreeDrawBack(@RequestParam(value = "tradeId") String tradeId,
			@RequestParam(value = "mark", required = false) String mark) {
		tradeAbnormalService.agreeDrawBack(SubjectUtil.getCurrentShop().getId(), tradeId, mark);
		return Response.SUCCESS;
	}*/

	/*@ApiOperation(value = "拒绝退款", notes = "")
    @PostMapping("refuseDrawBack")
	@ResponseBody
	@ShopAuth
	public Success refuseDrawBack(@RequestParam(value = "tradeId") String tradeId,
			@RequestParam(value = "mark", required = false) String mark) {
		tradeAbnormalService.refuseDrawBack(SubjectUtil.getCurrentShop().getId(), tradeId, mark);
		return Response.SUCCESS;
	}*/

	/*@ApiOperation(value = "退货订单列表", notes = "")
    @PostMapping("refundList")
	@ResponseBody
	@ShopAuth
	public CommonPageVO<TradeAbnormalRespVO> refundList(
			@RequestParam(name = "tradeNo", required = false) String tradeNo,
			@RequestParam(name = "userName", required = false) String userName,
			@RequestParam(name = "startTime", required = false) String startTime,
			@RequestParam(name = "endTime", required = false) String endTime,
			@RequestParam(name = "status", required = false) String status, @RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize) {
		TradeManageReqVO tradeManageReqVO = new TradeManageReqVO(SubjectUtil.getCurrentShop().getId(), tradeNo,
				userName, null, startTime, endTime, status);
		tradeManageReqVO.setLogStatus(Constants.TradeLogCode.LOG_GOODS_BACK);
		tradeManageReqVO.setShopFlag((short) 0);
		return tradeAbnormalService.problemTradeList(tradeManageReqVO, pageNo, pageSize);
	}*/

	/*@ApiOperation(value = "同意退货", notes = "")
    @PostMapping("agreeRefund")
	@ResponseBody
	@ShopAuth
	public Success agreeRefund(@RequestParam(value = "tradeId") String tradeId,
			@RequestParam(value = "mark") String mark, @RequestParam(value = "backName") String backName,
			@RequestParam(value = "backMobile") String backMobile,
			@RequestParam(value = "detailsAddress") String detailsAddress,
			@RequestParam(value = "province") String province, @RequestParam(value = "city") String city,
			@RequestParam(value = "county") String county) {
		TradeRefundVO tradeRefundVO = new TradeRefundVO();
		tradeAbnormalService.agreeRefund(SubjectUtil.getCurrentShop().getId(), tradeId, tradeRefundVO, mark);
		return Response.SUCCESS;
	}*/

	/*@ApiOperation(value = "拒绝退货", notes = "")
    @PostMapping("refuseRefund")
	@ResponseBody
	@ShopAuth
	public Success refuseRefund(@RequestParam(value = "tradeId") String tradeId,
			@RequestParam(value = "mark", required = false) String mark) {
		tradeAbnormalService.refuseRefund(SubjectUtil.getCurrentShop().getId(), tradeId, mark);
		return Response.SUCCESS;
	}

	@ApiOperation(value = "确认到货", notes = "")
	@PostMapping("sureRefund")
	@ResponseBody
	@ShopAuth
	public Success sureRefund(@RequestParam(value = "tradeId") String tradeId) {
		tradeAbnormalService.sureRefund(SubjectUtil.getCurrentShop().getId(), tradeId, "卖家确认退货成功");
		return Response.SUCCESS;
	}*/

    //部分发货
    @ApiOperation(value = "商品部分发货", notes = "")
    @PostMapping("tradePartialShipments")
    @ResponseBody
    @ShopAuth
    /**
     *商铺内部分发货
     */
    public Success tradePartialShipments(@RequestBody TradeGoodLogisticsVo tradeGoodLogisticsVo) {
        tradeService.tradePartialShipments(tradeGoodLogisticsVo.getTradeId(), tradeGoodLogisticsVo.getGoodId(), tradeGoodLogisticsVo.getLogistics(), tradeGoodLogisticsVo.getOgisticsNumber(), SubjectUtil.getCurrent().getId());
        return Response.SUCCESS;
    }

    /**
     * 查询部分发货商品的物流信息
     */
    @ApiOperation(value = "查询部分发货商品的物流信息", notes = "")
    @GetMapping("getShopTransportByShopIdAndGoodId")
    @ResponseBody
    @ShopAuth
    public ShopTransportVO getShopTransportByShopIdAndGoodId(@RequestParam("shopId") String shopId,
                                                             @RequestParam("goodId") String goodId,
                                                             @RequestParam("tradeNo") String tradeNo) {
        return tradeService.getShopTransportByShopIdAndGoodId(shopId, goodId, tradeNo);

    }
}
