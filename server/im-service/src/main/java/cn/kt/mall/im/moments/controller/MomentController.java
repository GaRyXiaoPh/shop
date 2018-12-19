package cn.kt.mall.im.moments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Result;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.im.moments.entity.MomentsCommentEntity;
import cn.kt.mall.im.moments.service.MomentService;
import cn.kt.mall.im.moments.vo.MomentsReqVO;
import cn.kt.mall.im.moments.vo.MomentsRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "莱粉圈", tags = "lem-moments")
@RequestMapping("/moments")
@RestController
public class MomentController {

	@Autowired
	private MomentService momentService;

	// 获取莱粉圈列表
	@ApiOperation(value = "获取莱粉圈列表", notes = "")
	@GetMapping("listMoments")
	@ResponseBody
	public CommonPageVO<MomentsRespVO> listMoments(@RequestParam(value = "friendId", required = false) String friendId,
			@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
		return momentService.listMoments(SubjectUtil.getCurrent().getId(), friendId, pageNo, pageSize);
	}

	// 发布莱粉圈
	@ApiOperation(value = "发布莱粉圈", notes = "")
	@PostMapping("addMoments")
	@ResponseBody
	public Success addMoments(@RequestBody MomentsReqVO momentsReqVO) {
		momentService.addMoments(SubjectUtil.getCurrent().getId(), momentsReqVO);
		return Response.SUCCESS;
	}
	
	// 评论莱粉圈信息
	@ApiOperation(value = "评论莱粉圈信息", notes = "")
	@PostMapping("addMomentComment")
	@ResponseBody
	public Result<Long> addMomentComment(@RequestBody MomentsCommentEntity momentsCommentEntity) {
		Long id = momentService.addMomentComment(SubjectUtil.getCurrent().getId(), momentsCommentEntity);
		return Response.result(id);
	}

	// 点赞or取消点赞
	@ApiOperation(value = "点赞/取消点赞", notes = "momentsId,莱粉圈信息id")
	@PostMapping("likeMoments")
	@ResponseBody
	public Success likeMoments(@RequestParam("momentsId") Long momentsId) {
		momentService.likeMoments(SubjectUtil.getCurrent().getId(), momentsId);
		return Response.SUCCESS;
	}

	// 删除莱粉圈记录
	@ApiOperation(value = "删除莱粉圈记录", notes = "")
	@DeleteMapping("delMoments")
	@ResponseBody
	public Success delMoments(@RequestParam("momentsId") Long momentsId) {
		momentService.delMoments(SubjectUtil.getCurrent().getId(), momentsId);
		return Response.SUCCESS;
	}
}
