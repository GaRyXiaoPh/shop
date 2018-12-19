package cn.kt.mall.front.circle;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.front.password.service.TransactionPasswordService;
import cn.kt.mall.offline.entity.*;
import cn.kt.mall.offline.service.*;
import cn.kt.mall.offline.vo.*;
import cn.kt.mall.shop.advertise.vo.AdResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9.
 */
@Api(description = "线下商圈", tags = "circle")
@RequestMapping("/circle")
@RestController
public class CircleController {

    @Autowired
    private OfflineShopService offlineShopService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TransactionPasswordService transactionPasswordService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private  AdService adService;


    // 查询商圈首页商户列表
    @ApiOperation(value = "查询商圈首页商户列表", notes = "")
    @PostMapping("selectHomeShopList")
    @ResponseBody
    @IgnoreJwtAuth
    public Map selectHomeShopList(@RequestBody HomeShopVO homeShopVO) {
        List<ShopEntity> shopList = offlineShopService.selectHomeShopList(homeShopVO);
        List<AdResVO> adList = adService.queryADList();
        Map map = new HashMap();
        map.put("shopList",shopList);
        map.put("adList",adList);
        return  map;
    }


    //搜索
    @ApiOperation(value = "搜索", notes = "")
    @PostMapping("search")
    @ResponseBody
    @IgnoreJwtAuth
    public List<ShopEntity> search(@RequestBody SearchShopVO searchShopVO) {
            if(Long.valueOf(0).equals(searchShopVO.getCountry())){
                searchShopVO.setCountry(null);
            }
        return offlineShopService.searchShopList(searchShopVO);
    }


    //店铺主页
    @ApiOperation(value = "店铺主页", notes = "")
    @GetMapping("homePage")
    @ResponseBody
    public ShopInfoEntity homePage(@ApiParam(value = "店铺id") @RequestParam String shopId) {
        return shopInfoService.selectShopInfo(shopId);
    }

    //商品详情
    @ApiOperation(value = "商品详情", notes = "")
    @GetMapping("goodDetail")
    @ResponseBody
    @IgnoreJwtAuth
    public GoodEntity goodDetail(@ApiParam(value = "商品id") @RequestParam String id) {
        return goodsService.getGoodDetail(id);
    }

    //添加评论
    @ApiOperation(value = "添加评论", notes = "")
    @PostMapping("addComment")
    @ResponseBody
    public Success addComment(@ApiParam(value = "添加评论") @RequestBody CommentVO commentVO) {
        commentService.addComment(commentVO);
        return Response.SUCCESS;
    }

    //下单
    @ApiOperation(value = "下单", notes = "")
    @PostMapping("order")
    @ResponseBody
    public Map addOrder(@RequestBody OrderVO orderVO) {
        Map map = new HashMap();
        map.put("orderId",orderService.addOrder(orderVO));
        return map;
    }

    //支付
    @ApiOperation(value = "支付", notes = "")
    @PostMapping("pay")
    @ResponseBody
    public Success pay(@ApiParam(value="订单id") @RequestParam String orderId,
                       @ApiParam(value="支付密码") @RequestParam String payPwd) {
        //判断交易密码是否正确
        transactionPasswordService.check(SubjectUtil.getCurrent().getId(), payPwd);
        //支付
        orderService.pay(orderId,SubjectUtil.getCurrent().getId());
        return Response.SUCCESS;
    }

    //商圈订单
    @ApiOperation(value = "商圈订单", notes = "")
    @GetMapping("circleOrder")
    @ResponseBody
    public PageVO<CircleOrderEntity> circleOrder(@ApiParam(value = "页码" ) @RequestParam int pageNo,
                                                 @ApiParam(value = "页数") @RequestParam int pageSize) {
        return orderService.getCircleOrderInfo(SubjectUtil.getCurrent().getId(),pageNo,pageSize);
    }

    //订单详细信息
    @ApiOperation(value = "订单详细信息", notes = "")
    @GetMapping("getOrderInfo")
    @ResponseBody
    public OrderInfoEntity getOrderInfo(@ApiParam(value = "订单号") @RequestParam String orderId) {
        return orderService.getOrderInfo(orderId);
    }

    //我的推荐
    @ApiOperation(value = "我的推荐", notes = "")
    @GetMapping("myRecommend")
    @ResponseBody
    public Map myRecommend(@ApiParam(value = "标识符(1:推荐的商户  2:推荐的会员)") @RequestParam Integer flag) {
        return recommendService.myRecommend(SubjectUtil.getCurrent().getId(),flag);
    }
}
