package cn.kt.mall.front.version.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kt.mall.front.version.dao.AppVersionDAO;
import cn.kt.mall.front.version.vo.VersionVO;

/**
 * Created by Administrator on 2018/1/31.
 */

@Service
public class AppVersionService {

    @Autowired
    private AppVersionDAO appVersionDAO;

    public VersionVO getVersion(String platform) {
        return appVersionDAO.getLastVersion(platform);
    }
}
