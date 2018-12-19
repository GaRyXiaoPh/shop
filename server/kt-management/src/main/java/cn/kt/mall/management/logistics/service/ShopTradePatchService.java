package cn.kt.mall.management.logistics.service;


import cn.kt.mall.common.http.Success;
import cn.kt.mall.management.logistics.vo.*;

import java.math.BigDecimal;
import java.util.List;

public interface ShopTradePatchService {



    Success mergeGoodTrade(MergeTradeBaseInfoVO vo);



    Success patchSendLogistics(PatchSendBaseVO patchSend);
}
