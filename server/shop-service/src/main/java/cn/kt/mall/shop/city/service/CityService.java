package cn.kt.mall.shop.city.service;

import java.util.List;

import cn.kt.mall.shop.city.vo.CityBaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kt.mall.shop.city.entity.CityEntity;
import cn.kt.mall.shop.city.mapper.CityDAO;
import cn.kt.mall.shop.city.vo.CityRespVO;

@Service
public class CityService {

	@Autowired
	private CityDAO cityDAO;

	/**
	 * 获取省列表
	 *
	 * @return
	 */
	public List<CityEntity> getProvinceList() {
		return cityDAO.getProvinceList();
	}

	/**
	 * 获取市区列表
	 *
	 * @param id
	 * @return
	 */
	public List<CityEntity> getCityList(Long id) {
		return cityDAO.getCityList(id);
	}

	/**
	 * 获取区县列表
	 *
	 * @param id
	 * @return
	 */
	public List<CityEntity> getCountyList(Long id) {
		return cityDAO.getCountyList(id);
	}

	// 根据省市区获取对应的名称
	public CityRespVO getDataById(Long proId, Long cityId, Long districtId) {
		return cityDAO.getDataById(proId, cityId, districtId);
	}

	/**
	 * 获取省市区列表
	 *
	 * @return
	 */
    public List<CityEntity> getAllCityList(){
        List<CityEntity> provinceList = cityDAO.getProvinceList();
        for(CityEntity p:provinceList){
            List<CityEntity> cityList = cityDAO.getCityList(p.getSid());
            for(CityEntity c:cityList){
                c.setCountyList(cityDAO.getCountyList(c.getSid()));
            }
            p.setCityList(cityList);

        }
        return provinceList;
    }

	/**
	 * 查询所有城市(搜索)
	 *
	 * @param name
	 * @return
	 */
	public List<CityBaseVO> queryCityList(String name){
		return cityDAO.queryCityList(name);
	}

	/**
	 * 查询所有城市(搜索)
	 *
	 * @param name
	 * @return
	 */
	public CityBaseVO selectCodeById(String name){
		return cityDAO.selectCodeById(name);
	}

}
