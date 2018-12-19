package cn.kt.mall.offline.web.controller;

import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.ShopAuth;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.offline.entity.CommentEntity;
import cn.kt.mall.offline.entity.OrderDetailEntity;
import cn.kt.mall.offline.entity.OrderEntity;
import cn.kt.mall.offline.service.CommentService;
import cn.kt.mall.offline.service.OrderService;
import cn.kt.mall.offline.vo.CommentResVO;
import cn.kt.mall.offline.vo.OrderRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by Administrator on 2018/5/12.
 */
@Api(description = "Web商圈", tags = "circle_web")
@RequestMapping("/circle/web")
@RestController
public class OrderManageController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "订单管理列表")
    @PostMapping("orderManageList")
    @ShopAuth
    public PageVO<OrderEntity> orderManageList(@ApiParam("订单管理请求参数类") @RequestBody OrderRequestVO orderRequestVO) {
        return orderService.queryOrderManageList(orderRequestVO);
    }

    @ApiOperation(value = "订单详情")
    @GetMapping("orderDetail")
    public OrderDetailEntity orderDetail(@ApiParam(value = "订单号") @RequestParam String orderId) {
        return orderService.getOrderDetailInfo(orderId);
    }

    @ApiOperation(value = "评论管理")
    @GetMapping("commentManage")
    @ShopAuth
    public PageVO<CommentEntity> commentManage(@ApiParam(value = "页码", required = true) @RequestParam Integer pageNo,
                                               @ApiParam(value = "页数", required = true) @RequestParam Integer pageSize,
                                               @ApiParam(value = "开始时间", required = true) @RequestParam String startTime,
                                               @ApiParam(value = "结束时间", required = true) @RequestParam String endTime,
                                               @ApiParam(value = "用户名") @RequestParam(required = false) String username) {
        CommentResVO commentResVO = new CommentResVO();
        commentResVO.setStartTime(startTime);
        commentResVO.setEndTime(endTime);
        commentResVO.setUsername(username);
        commentResVO.setPageNo(pageNo);
        commentResVO.setPageSize(pageSize);
        return commentService.getCommentManage(commentResVO);
    }

}
