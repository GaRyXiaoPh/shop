package cn.kt.mall.shop.coupon.vo;

import cn.kt.mall.common.wallet.entity.StatementEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserAssertAndStateMentVO implements Serializable {

    private UserAssetEntityVO userAssetEntityVO;

    private StatementEntity statementEntity;
}
