package cn.kt.mall.management.admin.controller;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.offline.entity.GoodEntity;
import cn.kt.mall.offline.service.GoodsService;
import cn.kt.mall.offline.vo.OffGoodVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2018/5/31.
 */
@Api(description = "线下商家商品管理", tags = "offline-good")
@RequestMapping("/manage/offShopGood")
@RestController
public class OfflineShopGoodController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation(value = "地面商户商品管理")
    @PostMapping("list")
    public PageVO<GoodEntity> list(@RequestBody OffGoodVO offGoodVO) {
        if(offGoodVO.getStatus() == 0){
            offGoodVO.setStatus(null);
        }
        return goodsService.getOffGoods(offGoodVO);
    }

    @ApiOperation(value = "地面商户商品管理")
    @GetMapping("update")
    public Success update(@ApiParam(value = "1:审核通过  2:上架  3:下架 4:审核不通过") @RequestParam Integer status,
                          @ApiParam(value = "商品id") @RequestParam String id) {
        if(status == 1){
            status = 2;
        }
        GoodEntity goodEntity = new GoodEntity(id,status);

        goodsService.updateGoodInfo(goodEntity);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "查看")
    @GetMapping("view")
    public GoodEntity view(@ApiParam(value = "商品编号", required = true) String id) {
        return goodsService.getGoodDetail(id);
    }

}
