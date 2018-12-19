package cn.kt.mall.common.wallet.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Setter
@Getter
public class WalletLemEntity implements Serializable {

    private static final long serialVersionUID = 7633547813177187580L;

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("币地址")
    private String popcAddress;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("注册时间")
    private String createTime;

    @ApiModelProperty("修改时间")
    private String updateTime;
}
