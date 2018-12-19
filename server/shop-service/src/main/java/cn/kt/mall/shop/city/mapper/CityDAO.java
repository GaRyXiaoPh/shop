package cn.kt.mall.shop.city.mapper;

import cn.kt.mall.shop.city.entity.CityEntity;
import cn.kt.mall.shop.city.vo.CityBaseVO;
import cn.kt.mall.shop.city.vo.CityRespVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CityDAO {

	/**
	 * 获取省列表
	 *
	 * @return
	 */
	List<CityEntity> getProvinceList();

	/**
	 * 根据省id查询市区列表
	 *
	 * @return
	 */
	List<CityEntity> getCityList(@Param("id") Long id);

	/**
	 * 根据市区id获取相应的区县列表
	 *
	 * @param id
	 * @return
	 */
	List<CityEntity> getCountyList(@Param("id") Long id);

	CityRespVO getDataById(@Param("proId") Long proId, @Param("cityId") Long cityId,
			@Param("districtId") Long districtId);

	/**
	 * 查询所有城市(搜索)
	 *
	 * @param name
	 * @return
	 */
	List<CityBaseVO> queryCityList(@Param("name") String name);

	/**
	 * 根据名称查询城市信息
	 *
	 * @param name
	 * @return
	 */
	CityBaseVO selectCodeById(@Param("name") String name);
}
