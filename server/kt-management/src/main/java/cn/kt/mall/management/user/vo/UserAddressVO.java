package cn.kt.mall.management.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("用户账户地址信息")
@Getter
@Setter
@NoArgsConstructor
public class UserAddressVO {

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("账户类型：lem,aec,btc,ltc")
    private String type;

    @ApiModelProperty("账户钱包地址")
    private String address;

    public UserAddressVO(String userId, String type, String address) {
        this.userId = userId;
        this.type = type;
        this.address = address;
    }
}
