package cn.kt.mall.management.ExecelPatchExport;


import cn.kt.mall.common.excel.ExportToExcelUtil;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Api(description = "导出测试类")
@RequestMapping("/test-sheet")
@RestController
public class SheetExcel {
    @ApiOperation(value = "导出分页例子")
    @GetMapping("excelTest")
    @ResponseBody
    @IgnoreJwtAuth
    public void exportStatResourceBrowse(HttpServletResponse response, HttpServletRequest request) {
        ExportToExcelUtil<ExceltestVO> excelUtil = new ExportToExcelUtil<ExceltestVO>();
        // 导出总记录数
      //  excelTatol = request.getParameter("excelTatol") == null ? 10 : Integer.parseInt(request.getParameter("excelTatol"));
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            excelUtil.setResponseHeader(response,"资源浏览统计表");
            String[] headers = { "会员id"};
            String[] columns = { "index"};

            List<ExceltestVO> list = new ArrayList<>();
            for(int i=0;i<100000;i++){
                ExceltestVO exceltestVO = new ExceltestVO();
                exceltestVO.setIndex(i);
                list.add(exceltestVO);
            }


            excelUtil.exportExcel( headers, columns, list, out, request, "");
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

}
