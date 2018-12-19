package cn.kt.mall.front.external.service;

import cn.kt.mall.front.external.entity.NationalCodeEntity;

import java.util.List;

/**
 * 外部信息业务接口
 * Created by wqt on 2018/1/29.
 */
public interface ExternalInfoService {

    List<NationalCodeEntity> getNationalCodes();
}
