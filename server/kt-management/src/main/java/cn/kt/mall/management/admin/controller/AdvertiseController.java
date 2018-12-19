package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.offline.service.AdService;
import cn.kt.mall.shop.advertise.vo.AdResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * Created by Administrator on 2018/5/31.
 */
@Api(description = "广告管理模块", tags = "Advertise-Manager")
@RequestMapping("/manage/")
@RestController
public class AdvertiseController {

    @Autowired
    private AdService adService;

    @ApiOperation(value = "添加广告")
    @PostMapping("addAdvertise")
    public Success addAdvertise(@ApiParam(value = "status(状态：0:上线 1:下线)、type(类型：0:链接 1:图文)、content富文本") @RequestBody AdResVO adResVO) {
        A.check(adResVO.getUrl() == null || adResVO.getUrl().equals(""),"请上传广告图片");
        adResVO.setId(IDUtil.getUUID());
        adResVO.setCreateTime(DateUtil.getTime(new Date()));
        adService.add(adResVO);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "查询广告列表")
    @GetMapping("getAdList")
    public PageVO<AdResVO> getAdList(@ApiParam(value = "创建开始时间") @RequestParam(name = "start", required = false) String start,
                                     @ApiParam(value = "创建结束时间") @RequestParam(name = "end", required = false) String end,
                                     @ApiParam(value = "页数") @RequestParam Integer pageNo,
                                     @ApiParam(value = "页码") @RequestParam Integer pageSize) {
        return adService.getADList(start, end, pageNo, pageSize);
    }

    @ApiOperation(value = "上线下线")
    @GetMapping("onlineOrOffline")
    public Success onlineOrOffline(@ApiParam(value = "0:上线 1:下线") @RequestParam Integer status,
                                   @ApiParam(value = "广告id") @RequestParam String id) {
        adService.updateStatus(status,id);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "单条删除广告")
    @GetMapping("delAdvertise")
    public Success delAdvertise(@ApiParam(value = "广告id") @RequestParam String id) {
        adService.delById(id);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "批量删除广告")
    @GetMapping("delAdvertiseBatch")
    public Success delAdvertiseBatch(@ApiParam(value = "广告id集合：使用,分隔") @RequestParam String ids) {
        adService.delAdvertiseBatch(ids);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "编辑广告")
    @PostMapping("updateADInfo")
    public Success updateADInfo(@RequestBody AdResVO adResVO) {
        adResVO.setEndTime(DateUtil.getTime(new Date()));
        adService.updateADInfo(adResVO);
        return Response.SUCCESS;
    }
}
