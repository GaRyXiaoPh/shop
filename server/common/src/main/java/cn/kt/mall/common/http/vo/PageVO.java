package cn.kt.mall.common.http.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@ApiModel("分页结果")
@Getter
@Setter
@NoArgsConstructor
public class PageVO<T> {

    @ApiModelProperty(notes = "页码", dataType = "int")
    private int pageNo;

    @ApiModelProperty(notes = "页大小", dataType = "int")
    private int pageSize;

    @ApiModelProperty(notes = "总记录数", dataType = "int")
    private int allCount;

    @ApiModelProperty(notes = "列表数据")
    private List<T> list;

    public PageVO(int pageNo, int pageSize) {
        if(pageNo < 1) {
            pageNo = 1;
        }

        if(pageSize < 5) {
            pageSize = 5;
        }

        if(pageSize > 200) {
            pageSize = 200;
        }

        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageVO(int pageNo, int pageSize, int allCount) {
        this(pageNo, pageSize);
        this.allCount = allCount;
    }

    public PageVO(int pageNo, int pageSize, int allCount, List<T> list) {
        this(pageNo, pageSize, allCount);
        this.list = list;
    }

    public PageVO<T> setList(List<T> list) {
        this.list = list;
        return this;
    }
}
