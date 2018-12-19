package cn.kt.mall.task.controller;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.task.ReleaseTask;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


//定时任务的接口，当定时任务的接口有问题时，可以手工调用接口完成功能
@RestController
@RequestMapping("/task")
public class TaskController {

    @Value("${task.api-key}")
    private String taskKey;
    @Autowired
    private ReleaseTask releaseTask;


    @ApiOperation(value = "解冻金额")
    @GetMapping("release-coins")
    @IgnoreJwtAuth
    @ResponseBody
    public Success getAdList() {
        // @ResponseBody
        //@ApiParam(value = "") @RequestParam String taskKeyParam
      //  A.check(!taskKey.equalsIgnoreCase(taskKeyParam),"key不匹配");
       releaseTask.couponsDailyTask();
       return Response.SUCCESS;
    }

    @ApiOperation(value = "手动去解冻，一天可以多次")
    @GetMapping("handle-release-coins")
    @IgnoreJwtAuth
    @ResponseBody
    public Success handRelease() {
        // @ResponseBody
        //@ApiParam(value = "") @RequestParam String taskKeyParam
        //  A.check(!taskKey.equalsIgnoreCase(taskKeyParam),"key不匹配");
        releaseTask.handleToRelease();
        return Response.SUCCESS;
    }
}
