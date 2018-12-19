package cn.kt.mall.offline.service;

import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.shop.advertise.vo.AdResVO;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
public interface AdService {

    /**
     * 添加广告
     *
     * @param adResVO
     * @return
     */
    void add (AdResVO adResVO);

    /**
     * 查询广告列表
     *
     * @param startTime
     * @param endTime
     * @return
     */
    PageVO<AdResVO> getADList(String startTime, String endTime, Integer pageNo, Integer pageSize);

    /**
     * 更新广告状态
     *
     * @param status
     * @param id
     * @return
     */
    void updateStatus(Integer status,String id);

    /**
     * 删除广告
     *
     * @param id
     */
    void delById(String id);

    /**
     * 批量删除广告
     *
     * @param ids
     */
    void delAdvertiseBatch(String ids);

    /**
     * 更新广告信息
     *
     * @param adResVO
     * @return
     */
    void updateADInfo(AdResVO adResVO);

    /**
     * 获取商圈首页轮播图
     *
     * @return
     */
    List<AdResVO> queryADList();
}
