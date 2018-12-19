/*
 * @Project Name: web-util
 * @File Name: CommonUtil.java
 * @Package Name: com.github.bigdown.web.util
 * @Date: 2018年1月23日下午6:08:32
 * @Creator: tanshen519
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package cn.kt.mall.common.util;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cn.kt.mall.common.http.vo.CommonPageVO;

/**
 * @description TODO
 * @author tanshen519
 * @date 2018年1月23日下午6:08:32
 * @see
 */
public class CommonUtil {
	
	public static <L, P> CommonPageVO<L> copyFromPageInfo(PageInfo<P> pageInfo, List<L> resultList) {
		CommonPageVO<L> commonPageVO = new CommonPageVO<L>();
		commonPageVO.setFirstPage(pageInfo.isIsFirstPage());
		commonPageVO.setLastPage(pageInfo.isIsLastPage());
		commonPageVO.setList(resultList);
		commonPageVO.setPageNo(pageInfo.getPageNum());
		commonPageVO.setPages(pageInfo.getPages());
		commonPageVO.setPageSize(pageInfo.getPageSize());
		commonPageVO.setAllCount(pageInfo.getTotal());
		return commonPageVO;
	}
}
