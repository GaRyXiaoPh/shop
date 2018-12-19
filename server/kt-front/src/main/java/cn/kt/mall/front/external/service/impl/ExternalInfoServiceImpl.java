package cn.kt.mall.front.external.service.impl;

import cn.kt.mall.front.external.dao.ExternalInfoDAO;
import cn.kt.mall.front.external.entity.NationalCodeEntity;
import cn.kt.mall.front.external.service.ExternalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 外部信息服务实现类
 * Created by wqt on 2018/1/29.
 */
@Service
public class ExternalInfoServiceImpl implements ExternalInfoService {

    private ExternalInfoDAO externalInfoDAO;

    @Autowired
    public ExternalInfoServiceImpl(ExternalInfoDAO externalInfoDAO) {
        this.externalInfoDAO = externalInfoDAO;
    }

    @Override
    public List<NationalCodeEntity> getNationalCodes() {
        return this.externalInfoDAO.getNationalCodes();
    }
}
