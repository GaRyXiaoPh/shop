package cn.kt.mall.shop.advertise.dao;

import cn.kt.mall.shop.advertise.vo.AdResVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
@Mapper
public interface AdDao {

    /**
     * 添加广告
     *
     * @param adResVO
     * @return
     */
    int add (AdResVO adResVO);

    /**
     * 查询广告列表
     *
     * @param startDate
     * @param endDate
     * @return
     */
    List<AdResVO> getADList(@Param("startDate") String startDate,
                            @Param("endDate") String endDate,
                            @Param("offset") Integer offset,
                            @Param("pageSize") Integer pageSize);

    /**
     * 查询广告列表长度
     *
     * @param startDate
     * @param endDate
     * @return
     */
    int getADCount(@Param("startDate") String startDate,
                   @Param("endDate") String endDate);

    /**
     * 更新广告状态
     *
     * @param status
     * @param id
     * @return
     */
    int updateStatus(@Param("status") Integer status,@Param("id") String id);

    /**
     * 查询广告的详细信息
     *
     * @param id
     * @return
     */
    AdResVO getAdById(@Param("id") String id);

    /**
     * 删除广告信息
     *
     * @param id
     * @return
     */
    int delById(@Param("id") String id);

    /**
     * 更新广告信息
     *
     * @param adResVO
     * @return
     */
    int updateADInfo(AdResVO adResVO);

    /**
     * 获取商圈首页轮播图
     *
     * @return
     */
    List<AdResVO> queryADList();
    /**
     * 前台获取商圈首页轮播图
     *
     * @return
     */
    List<cn.kt.mall.shop.good.vo.AdResVO> getAdResVOList();
}

