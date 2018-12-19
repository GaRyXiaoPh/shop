package cn.kt.mall.management.admin.service.excel;

import cn.kt.mall.shop.cash.mapper.CashDAO;

import cn.kt.mall.shop.cash.vo.CashRespVO;
import org.apache.log4j.Logger;

import org.apache.poi.hssf.usermodel.*;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ExcelService {


    private static Logger logger = Logger.getLogger(ExcelService.class);

    /**
     *
     * @Description 设置样式
     * @author        <p style="color:#8e8e8e;font-family:微软雅黑;font-size=16px;font-weight:bold;">Cloud</p>
     * @date        <p style="color:#000;font-family:微软雅黑;font-size=16px;">2016-11-25下午3:06:34</p>
     * @param row    行
     * @param wb    表格
     * @param height    高度
     * @param fontSize    字体大小
     * @param fontWeight    字体粗细
     * @return
     */
    public HSSFCellStyle setHeadStyle(HSSFRow row, HSSFWorkbook wb, int height, int fontSize, short fontWeight){
        //设置高度
        row.setHeight((short) height);
        //创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        //设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) fontSize);//设置字体大小
       /* font.setBoldweight(fontWeight);//粗体粗细
        font.setW*/
        style.setFont(font);//选择需要用到的字体格式
        return style;
    }


    /**
     *
     * @Description 设置导出数据
     * @author        <p style="color:#8e8e8e;font-family:微软雅黑;font-size=16px;font-weight:bold;">Cloud</p>
     * @date        <p style="color:#000;font-family:微软雅黑;font-size=16px;">2016-11-25下午3:44:41</p>
     * @param dataMap    导出数据Map
     * @param sheet        导出表格
     * @param row        行
     * @param style        样式
     * @param exportType    导出类型
     */
    @SuppressWarnings("unchecked")
    public void setExportDate(Map<String, Object> dataMap, HSSFSheet sheet, HSSFRow row, HSSFCellStyle style, Integer exportType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (exportType == 5) {
           // List<CashRespVO> cashReqVOList = cashDAO.getCashList(null, null);
           // List<CashRespVO> cashReqVOList = cashDAO.getCashByID();
            List<CashRespVO> cashReqVOList = (List<CashRespVO>)dataMap.get("cashReqVOList");
            //设置表格标题
            String[] title = new String[]{"用户ID", "真实姓名", "用户PID", "提现时间", "批准时间", "提币地址", "提币数量", "操作状态","备注"};
           // setHead(row, style, title);
            int c = 0;
            for (CashRespVO data : cashReqVOList) {
                c++;
                row = sheet.createRow((int) c);
                //申请时间
                Date applyTime = data.getCreateTime();
                //批准时间
                Date updateTime = data.getUpdateTime();
                //时间处理
                String applyTimeStr = applyTime != null ? sdf.format(applyTime) : "";
                String updateTimeStr = updateTime != null ? sdf.format(updateTime) : "";
                String statusStr ="未处理";

                if(data.getStatus().equals("1")) {
                    statusStr="同意";

                }else if(data.getStatus().equals("2")) {
                    statusStr="拒绝";
                }

                // 第四步，创建单元格，并设置值
                String[] dataArray = new String[]{
                        data.getId(),
                        data.getTrueName(),
                        data.getPid(),
                        applyTimeStr,
                        updateTimeStr,
                        data.getPopcAddress(),
                        data.getCashAmount().toString(),
                        statusStr,
                        data.getRemark()
                };
                for (int j = 0; j < dataArray.length; j++) {
                    row.createCell(j).setCellValue(dataArray[j]);
                    if (dataArray[j] != null && dataArray[j].length() > 0) {
                        sheet.setColumnWidth(j, dataArray[j].toString().length() * 712);
                    }
                }
            }

        }
    }
    /**
     *
     * @Description 弹出下载提示框
     * @author        <p style="color:#8e8e8e;font-family:微软雅黑;font-size=16px;font-weight:bold;">Cloud</p>
     * @date        <p style="color:#000;font-family:微软雅黑;font-size=16px;">2016-11-25下午1:25:51</p>
     * @param response    请求头信息
     * @param wb        表格
     * @param filePath    文件路径
     * @throws IOException
     */
    public void popDownload (HttpServletResponse response, HSSFWorkbook wb, String filePath) throws IOException {
                //初始化流
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                //表格定稿数据
                wb.write(os);
                byte[] content = os.toByteArray();
                InputStream is = new ByteArrayInputStream(content);
                // 设置response参数，可以打开下载页面
                response.reset();
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String((filePath).getBytes(), "iso-8859-1"));
                ServletOutputStream out = response.getOutputStream();
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                logger.info("文件下载失败");
                e.printStackTrace();
            } finally {
                //关闭流
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
                if (os != null)
                    os.close();
            }
        }
}

