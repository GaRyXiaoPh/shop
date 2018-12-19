package cn.kt.mall.offline.web.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.offline.entity.GoodEntity;
import cn.kt.mall.offline.service.GoodsService;
import cn.kt.mall.offline.vo.GoodUpVO;
import cn.kt.mall.offline.vo.GoodVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商圈
 *
 * Create by chenhong on 2018/04/19.
 */
@Api(description = "Web商圈", tags = "circle_web")
@RequestMapping("/circle/web")
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation(value = "添加商品")
    @PostMapping("addGoods")
    public Success add(@RequestBody GoodVO goodsVO) {
        goodsService.addGoods(goodsVO, SubjectUtil.getCurrent().getId());
        return Response.SUCCESS;
    }

    @ApiOperation(value = "商品修改")
    @PostMapping("updateGoods")
    public Success update(@RequestBody GoodUpVO goodUpVO) {
        goodsService.updateGood(goodUpVO);
        return Response.SUCCESS;
    }


    @ApiOperation(value = "商户商品列表")
    @GetMapping("getGoodsList")
    public PageVO<GoodEntity> getGoodsList(@ApiParam(value = "页码", required = true) Integer pageNo,
                                           @ApiParam(value = "页数", required = true) Integer pageSize,
                                           @ApiParam(value = "商品状态") Integer status,
                                           @ApiParam(value = "商品名称") String name) {

        return goodsService.getGoodsList(SubjectUtil.getCurrent().getId(),pageNo,pageSize,status,name);
    }

    @ApiOperation(value = "商品上架下架")
    @GetMapping("shelves")
    public Success shelves(@ApiParam(value = "标识符(1:上架 2:下架)", required = true) int flag,
                           @ApiParam(value = "商品编号", required = true) String id) {
        String fail = null;
        GoodEntity goodEntity = new GoodEntity();
        goodEntity.setId(id);
        if(flag == 1){
            goodEntity.setStatus(2);
            fail = "上架失败";
        }
        if(flag == 2){
            goodEntity.setStatus(3);
            fail = "下架失败";
        }
        int count = goodsService.updateGoodInfo(goodEntity);
        A.check(count<1,fail);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "商品详情")
    @GetMapping("getGoodDetail")
    public GoodEntity getGoodDetail(@ApiParam(value = "商品编号", required = true) String id) {
        return goodsService.getGoodDetail(id);
    }

    @ApiOperation(value = "删除商品")
    @GetMapping("delGood")
    public Success delGood(@ApiParam(value = "商品编号", required = true) String id) {
        goodsService.delGood(id);
        return Response.SUCCESS;
    }



}
