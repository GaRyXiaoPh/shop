package cn.kt.mall.management.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.offline.entity.ShopTypeEntity;
import cn.kt.mall.offline.service.ShopTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 商圈
 *
 * Create by chenhong on 2018/04/19.
 */
@Api(description = "商圈分类管理", tags = "shopType")
@RequestMapping("/manage/shopType/")
@RestController
public class ShopTypeController {

    @Autowired
    private ShopTypeService shopTypeService;

    @ApiOperation(value = "添加商圈商户类型")
    @PostMapping("add")
    public Success add(@RequestBody ShopTypeEntity shopTypeEntity) {
        this.shopTypeService.insertShopType(shopTypeEntity);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "获取商圈商户类型列表")
    @RequestMapping(value = "list", method = GET)
    public List<ShopTypeEntity> list() {
        return shopTypeService.getTypeList();
    }

    @ApiOperation(value = "编辑商圈商户类型信息")
    @PostMapping("update")
    public Success update(@RequestBody ShopTypeEntity shopTypeEntity) {
        shopTypeService.updateShopType(shopTypeEntity);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "删除商圈商户类型信息")
    @RequestMapping(value = "del", method = GET)
    public Success del(@Param("id") String id) {
        shopTypeService.deleteShopType(id);
        return Response.SUCCESS;
    }
}
