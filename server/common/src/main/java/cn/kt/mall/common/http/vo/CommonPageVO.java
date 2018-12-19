/*
 * @Project Name: base-domain
 * @File Name: CommonPageVO.java
 * @Package Name: com.github.bigdown.domain.vo
 * @Date: 2018年1月23日下午6:02:30
 * @Creator: tanshen519
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */

package cn.kt.mall.common.http.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TS
 */
@Setter
@Getter
public class CommonPageVO<T> {
	
    @ApiModelProperty(notes = "页码", dataType = "int")
    private int pageNo;

    @ApiModelProperty(notes = "页大小", dataType = "int")
    private int pageSize;

    @ApiModelProperty(notes = "总记录数")
    private long allCount;

    @ApiModelProperty(notes = "列表数据")
    private List<T> list;
	// 总页数
    @ApiModelProperty(notes = "总页数", dataType = "int")
	private int pages;
	// 是否为第一页
    @ApiModelProperty(notes = "是否为第一页")
	private boolean isFirstPage;
	// 是否为最后一页
    @ApiModelProperty(notes = "是否为最后一页")
	private boolean isLastPage;
	
}
