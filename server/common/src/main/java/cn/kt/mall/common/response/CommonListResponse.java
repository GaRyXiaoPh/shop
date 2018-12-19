package cn.kt.mall.common.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by linmingren on 2017/8/29.
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class CommonListResponse<T> extends CommonResponse {
    @ApiModelProperty("当前页，若接口带分布，则有效")
    private Integer page;

    @ApiModelProperty("分页数，若接口带分布，则有效")
    private Integer pageNum;

    @ApiModelProperty("总数量")
    private Integer total;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;



    public void addItem(T item) {
        if (this.content == null) {
            this.content = new ArrayList<T>();
        }

        ((List)this.content).add(item);
    }

    public void addAllItem(List<T> itemList) {
        if (this.content == null) {
            this.content = new ArrayList<T>();
        }

        ((List)this.content).addAll(itemList);
    }
}
