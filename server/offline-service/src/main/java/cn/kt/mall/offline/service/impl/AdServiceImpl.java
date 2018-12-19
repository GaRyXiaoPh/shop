package cn.kt.mall.offline.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.offline.service.AdService;
import cn.kt.mall.shop.advertise.dao.AdDao;
import cn.kt.mall.shop.advertise.vo.AdResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
@Service("adService")
public class AdServiceImpl  implements AdService {

    @Autowired
    private AdDao adDao;

    @Override
    public void add(AdResVO adResVO) {
        int count = adDao.add(adResVO);
        A.check(count<1,"添加广告失败");
    }

    @Override
    public PageVO<AdResVO> getADList(String startTime, String endTime, Integer pageNo, Integer pageSize) {
        int srcPageNo = pageNo;
        if (pageNo>0) pageNo=pageNo-1;
        int offset = pageNo*pageSize;
        int count = adDao.getADCount(startTime,endTime);
        List<AdResVO> list = adDao.getADList(startTime,endTime,offset,pageSize);
        return new PageVO<>(srcPageNo, pageSize, count, list);
    }

    @Override
    public void updateStatus(Integer status, String id) {
        A.check(adDao.getAdById(id)==null,"此条广告不存在");
        int count = adDao.updateStatus(status,id);
        A.check(count<1,"更新失败");
    }

    @Override
    public void delById(String id) {
        A.check(adDao.getAdById(id)==null,"此条广告不存在");
        AdResVO adResVO = adDao.getAdById(id);
        A.check(adResVO.getStatus()==0,"禁止删除已上线的广告！");
        int count = adDao.delById(id);
        A.check(count<1,"删除失败");
    }

    /**
     * 批量删除广告
     *
     * @param ids
     */
    @Override
    public void delAdvertiseBatch(String ids)  {
        String[] id = ids.split(",");
        for(int i = 0; i < id.length; i++) {
            delById(id[i]);
        }
    }

    @Override
    public void updateADInfo(AdResVO adResVO) {
        int count = adDao.updateADInfo(adResVO);
        A.check(count<1,"更新失败");
    }

    @Override
    public List<AdResVO> queryADList() {
        return adDao.queryADList();
    }


}
