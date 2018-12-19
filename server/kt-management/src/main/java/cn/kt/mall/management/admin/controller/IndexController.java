package cn.kt.mall.management.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.management.admin.service.impl.UserService;
import cn.kt.mall.management.admin.vo.IndexVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "管理后台首页", tags = "index-page")
@RequestMapping("/manage/index")
@RestController
public class IndexController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "获取首页统计数据")
    @PostMapping("info")
	public IndexVO getIndexInfo() {
		return userService.getIndexInfo();
	}
}
