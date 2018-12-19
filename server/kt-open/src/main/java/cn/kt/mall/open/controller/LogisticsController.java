package cn.kt.mall.open.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.kt.mall.common.jwt.IgnoreJwtAuth;
import cn.kt.mall.common.util.JSONUtil;
import cn.kt.mall.open.constants.LogisticsConstant;
import cn.kt.mall.open.vo.NoticeRequestVo;
import cn.kt.mall.open.vo.NoticeResponseVo;
import cn.kt.mall.open.vo.ResultVo;
import cn.kt.mall.shop.logistics.entity.Logistics;
import cn.kt.mall.shop.logistics.entity.LogisticsDetail;
import cn.kt.mall.shop.logistics.service.LogisticsService;
import cn.kt.mall.shop.logistics.vo.ResultItem;

/**
 * 物流第三方回调
 */
@RestController
@RequestMapping("/callback/logistics")
public class LogisticsController {

	private final static Logger LOG = LoggerFactory.getLogger(LogisticsController.class);
	@Autowired
	private LogisticsService logisticsService;

	@IgnoreJwtAuth
	@RequestMapping(value = "/kuaidi100", method = RequestMethod.POST)
	public void kuaidi100(HttpServletRequest request, HttpServletResponse response) throws Exception {
		NoticeResponseVo resp = initResult();
		try {
			String param = request.getParameter("param");
			NoticeRequestVo nReq = (NoticeRequestVo) JSONUtil.parseToObject(param, NoticeRequestVo.class);
			ResultVo result = nReq.getLastResult();
			LOG.info(" url : /kuaidi100  param : " + param + " result :" + result);
			dealNewLogisticDetail(result);
			getSuccessResult(resp);
			// 这里必须返回，否则认为失败，过30分钟又会重复推送。
			response.getWriter().print(JSONUtil.toJSONString(resp));
		} catch (Exception e) {
			resp.setMessage("保存失败" + e.getMessage());
			// 保存失败，服务端等30分钟会重复推送。
			response.getWriter().print(JSONUtil.toJSONString(resp));
			LOG.error("保存失败" + e.getMessage());
		}
	}

	private void dealNewLogisticDetail(ResultVo result) {
		if (LogisticsConstant.RESULT_OK.equals(result.getMessage())) {
			ArrayList<ResultItem> data = result.getData();
			
			if(!CollectionUtils.isEmpty(data)) {
				for (ResultItem resultItem : data) {
					LogisticsDetail l = new LogisticsDetail();
					l.setLogisticsNo(result.getNu());
					l.setLogisticsContext(resultItem.getContext());
					l.setFtime(resultItem.getFtime());
					logisticsService.addLogisticsDetail(l);
				}
			}

			Logistics ll = new Logistics();
			ll.setLogisticsNo(result.getNu());
			ll.setLogisticsStatus(Integer.valueOf(result.getState()));
			logisticsService.updateLogisticsStatus(ll);
			
		}
	}

	private void getSuccessResult(NoticeResponseVo resp) {
		resp.setResult(true);
		resp.setReturnCode("200");
		resp.setMessage("成功");
	}

	private NoticeResponseVo initResult() {
		NoticeResponseVo resp = new NoticeResponseVo();
		resp.setResult(false);
		resp.setReturnCode("500");
		resp.setMessage("保存失败");
		return resp;
	}
}
