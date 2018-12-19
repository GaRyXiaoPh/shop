package cn.kt.mall.offline.service.impl;

import cn.kt.mall.offline.dao.OfflineShopDAO;
import cn.kt.mall.offline.dao.OfflineUserDAO;
import cn.kt.mall.offline.entity.MerInfoEntity;
import cn.kt.mall.offline.entity.ShopEntity;
import cn.kt.mall.offline.service.OfflineShopService;
import cn.kt.mall.offline.util.Distribution;
import cn.kt.mall.offline.vo.HomeShopVO;
import cn.kt.mall.offline.vo.SearchShopVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */
@Service("offlineShopService")
public class OfflineShopServiceImpl implements OfflineShopService {

    @Autowired
    private OfflineShopDAO offlineShopDAO;

    @Autowired
    private OfflineUserDAO offlineUserDAO;

    /**
     * 查询商圈首页商户列表
     *
     * @param homeShopVO
     * @return
     */
    @Override
    public List<ShopEntity> selectHomeShopList(HomeShopVO homeShopVO) {
        //将当前位置的经度纬度存放到对象中

        long city = homeShopVO.getCity();
        List<ShopEntity> list = offlineShopDAO.selectHomeShopList(city);

        Distribution start = new Distribution();
        start.setLongitude(Double.parseDouble(homeShopVO.getAddressLon()));
        start.setDimensionality(Double.parseDouble(homeShopVO.getAddressLat()));
        if(list.size()>0){
            for(ShopEntity l:list){
                //经度
                String addressLon = l.getAddressLon();
                //纬度
                String addressLat = l.getAddressLat();

                Distribution end = new Distribution();
                //经度
                end.setLongitude(Double.parseDouble(addressLon));
                //纬度
                end.setDimensionality(Double.parseDouble(addressLat));
                //获取当前位置与店铺之间的距离
                double distance = Distribution.getDistance(start,end);
                //将距离存放到对象中
                l.setDistance(distance);
            }

            Collections.sort(list, new Comparator<ShopEntity>() {

                @Override
                public int compare(ShopEntity o1, ShopEntity o2) {
                    // 按照距离进行升序排列
                    if (o1.getDistance() > o2.getDistance()) {
                        return 1;
                    }
                    if (o1.getDistance() == o2.getDistance()) {
                        return 0;
                    }
                    return -1;
                }
            });
        }
        return list;
    }

    @Override
    public List<ShopEntity> searchShopList(SearchShopVO searchShopVO) {
        //将当前位置的经度纬度存放到对象中!
        Distribution start = new Distribution();
        start.setLongitude(Double.parseDouble(searchShopVO.getAddressLon()));
        start.setDimensionality(Double.parseDouble(searchShopVO.getAddressLat()));

        List<ShopEntity> shopList = offlineShopDAO.searchShopList(searchShopVO);
        if(shopList.size()>0){
            for(ShopEntity s:shopList){
                /**经度*/
                String longitude = s.getAddressLon();
                /**纬度*/
                String latitude = s.getAddressLat();
                Distribution end = new Distribution();
                /**经度*/
                end.setLongitude(Double.parseDouble(longitude));
                /**纬度*/
                end.setDimensionality(Double.parseDouble(latitude));
                /**获取当前位置与店铺之间的距离*/
                double distance = Distribution.getDistance(start,end);
                s.setDistance(distance);
            }

            if(searchShopVO.getSort() == 2){
                Collections.sort(shopList, new Comparator<ShopEntity>() {

                    @Override
                    public int compare(ShopEntity o1, ShopEntity o2) {
                        // 按照距离进行升序排列
                        if (o1.getDistance() > o2.getDistance()) {
                            return 1;
                        }
                        if (o1.getDistance() == o2.getDistance()) {
                            return 0;
                        }
                        return -1;
                    }
                });
            }
        }
        return shopList;
    }

    @Override
    public MerInfoEntity getMerInfo(String userId) {
        String shopId = offlineUserDAO.getShopId(userId);
        return offlineShopDAO.getMerInfo(shopId);
    }
}
