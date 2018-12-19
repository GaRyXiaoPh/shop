package cn.kt.mall.common.user.vo;

import cn.kt.mall.common.user.entity.BankCardEntity;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.PasswordUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 注册信息VO
 * Created by jerry on 2017/12/29.
 */
@ApiModel(description = "用户添加银行卡信息")
@Getter
@Setter
@NoArgsConstructor
public class BankCardVO {

    @ApiModelProperty(hidden = true)
    private String userId;

    @ApiModelProperty(notes = "真实姓名", dataType = "string")
    private String trueName;

    @ApiModelProperty(notes = "银行卡号", dataType = "string")
    private String bankCard;

    @ApiModelProperty(notes = "银行名称", dataType = "string")
    private String bankName;

    @ApiModelProperty(notes = "开户行", dataType = "string")
    private String openBank;

    public BankCardEntity getBankCardEntity() {

        return new BankCardEntity(userId, trueName,  bankCard, bankName, openBank);
    }
}
