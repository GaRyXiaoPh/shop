package cn.kt.mall.common.wallet.base;

import lombok.Getter;

@Getter
public enum TradeType {
    PAY_ORDER((short) 1, "消费"),
    ORDER_INCOME((short) 2, "订单收款"),
    RECHARGE((short) 3, "充值"),
    RECHARGE_USER((short) 4, "给会员充值"),
    REDUCE((short) 5, "扣款"),
    REDUCE_USER((short) 6, "扣除会员资金"),
    RELEASE((short) 7, "解冻"),
    WITHDRAWAL((short) 8, "提现"),
    RED_PAPER((short) 9, "红包"),
    REFERRER((short) 10, "推荐奖励"),
    GIVE((short) 11, "消费赠送优惠券"),
    SELL((short) 12, "店铺销售"),
    TRANSFEROUT((short) 13, "优惠券转出"),
    BASE((short) 14, "初始化优惠券数据"),
    TRANSFERIN((short) 15, "优惠券转入"),
    SHOP_FREEZE_LESS((short) 19, "充值操作-通过审核-店铺冻结减少"),
    MEMBER_AVAILABLE_SUB((short) 20, "充值操作-通过审核-会员可用增加"),
    SHOP_AVAILABLE_SUB((short) 21, "充值操作-拒绝审核-商铺可用增加"),
    SHOP_AVAILABLE_SUB_K((short) 22, "扣除操作-通过审核-商铺可用增加"),
    MEMBER_AVAILABLE_SUB_K((short) 23, "扣除操作-拒绝审核-会员可用增加"),
    ASSET_BASE_RELEASE((short) 24, "基础资产表POPC释放记录"),
    SHOP_ASSETS((short) 25, "创建店铺同步资产"),
    COUPON_TRANSFER_POINT((short) 26, "彩票积分转余额");



    private Short type;
    private String name;

    TradeType(Short type, String name){
        this.type = type;
        this.name = name;
    }
}
