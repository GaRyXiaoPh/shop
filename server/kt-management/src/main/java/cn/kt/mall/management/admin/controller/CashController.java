package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.excel.*;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.shop.cash.service.CashService;
import cn.kt.mall.shop.cash.vo.CashReqVO;
import cn.kt.mall.shop.cash.vo.CashRespVO;
import cn.kt.mall.shop.cash.vo.DeliveCashesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Api(value = "提现记录模块", tags = "cash-manager")
@RequestMapping("/cash")
@RestController
public class CashController {

    @Autowired
    private CashService cashService;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CashController.class);
    @ApiOperation(value = "获取提现记录")
    @PostMapping("getCashList")
    @ResponseBody
    @ShopAuth
    public CommonPageVO<CashRespVO> getCashList(@RequestParam(name = "status", required = false) String status,
                                                @RequestParam(name = "startTime", required = false) String startTime,
                                                @RequestParam(name = "endTime", required = false) String endTime,
                                                @RequestParam(name = "trueName", required = false) String trueName,
                                                @RequestParam(name = "isShop", required = false) String isShop,
                                                @RequestParam("pageNo") int pageNo,
                                                @RequestParam("pageSize") int pageSize) {
        CashReqVO cashReqVO = new CashReqVO(null,
                status, startTime, endTime, trueName, isShop);
        return cashService.getCashList(cashReqVO, pageNo, pageSize);
    }

    @ApiOperation(value = "导出提现记录")
    @GetMapping("export")
    @ResponseBody
    public void exportCashList(
                   HttpServletResponse response,
        @RequestParam(name = "status", required = false) String status,
        @RequestParam(name = "startTime", required = false) String startTime,
        @RequestParam(name = "endTime", required = false) String endTime,
        @RequestParam(name = "trueName", required = false) String trueName,
        @RequestParam(name = "isShop", required = false) String isShop,
                   @RequestParam(name = "userName", required = false) String userName,
        @RequestParam(name = "excelName", required = true) String excelName
                                                ) throws IOException, NoSuchFieldException, IllegalAccessException {
            CashReqVO cashReqVO = new CashReqVO(null,
                    StringUtils.isBlank(status) ? null : status,
                    StringUtils.isBlank(startTime) ? null : startTime,
                    StringUtils.isBlank(endTime) ? null : endTime,
                    StringUtils.isBlank(trueName) ? null : trueName,
                    StringUtils.isBlank(isShop) ? null : isShop);

            logger.info("d导出提币记录表名"+excelName);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8") + ".xls");

            List<CashRespVO> cashList = cashService.getCashList(cashReqVO);

            List<CashRespVO> cashNewList =new ArrayList<>();
            if(cashList!=null&&cashList.size()>0){
                for(CashRespVO vo:cashList){
                    if(vo.getStatus().equals("0")){
                        vo.setStatus("未处理");
                    }
                    if(vo.getStatus().equals("1")){
                        vo.setStatus("同意");
                    }
                    if(vo.getStatus().equals("2")){
                        vo.setStatus("拒绝");
                    }
                    cashNewList.add(vo);
                }
            }

            ExcelGenerator generator = new ExcelGenerator();

            ReportDefinition definition = new ReportDefinition(excelName);
            definition.addColumn(new ReportColumn("提币时间","createTime"));
            definition.addColumn(new ReportColumn("批准时间","updateTime"));
            definition.addColumn(new ReportColumn("提币地址","popcAddress"));
            definition.addColumn(new ReportColumn("用户名","trueName"));
            definition.addColumn(new ReportColumn("提币数量","cashAmount"));
            definition.addColumn(new ReportColumn("提现状态","status"));
            ReportData reportData = new ReportData(definition, cashNewList);
            generator.addSheet(reportData);


            generator.write(response.getOutputStream());

            response.getOutputStream().flush();

    }

    @ApiOperation(value = "修改提现状态")
    @PostMapping("deliveCashes")
    @ResponseBody
    @ShopAuth
    public Success deliveCashes(@RequestParam(name = "cashId") String cashId,
                                @ApiParam(value = "状态", required = true) @RequestParam(name = "status") String status) {
        cashService.deliveCashes(cashId, status);
        return Response.SUCCESS;
    }

    // 批量发货
    @ApiOperation(value = "批量修改提现状态")
    @PostMapping("deliveCashesBatch")
    @ResponseBody
    @ShopAuth
    public Success deliveCashesBatch(@RequestBody DeliveCashesVO deliveCashesVO) {
        cashService.deliveCashesBatch(deliveCashesVO);
        return Response.SUCCESS;
    }
}
