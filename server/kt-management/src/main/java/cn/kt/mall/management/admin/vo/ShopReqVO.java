package cn.kt.mall.management.admin.vo;

import cn.kt.mall.shop.good.vo.GoodReqVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
@Getter
@Setter
public class ShopReqVO {
    @ApiModelProperty("店长电话")
    private String mobile;
    private String shopName;
    @ApiModelProperty("门店编号")
    private String shopNo;
    @ApiModelProperty("true: 允许订单操作 false:不允许")
    private boolean enableOrderOperation;
    //    @ApiModelProperty("true: 允许提币操作 false: 不允许")
//    private boolean enableWithdrawalOperation;
    @ApiModelProperty("2:零售店  3:批发店")
    private String shopLevel;

//    @ApiModelProperty("商品列表")
//    private List<String> goodIds;

}
