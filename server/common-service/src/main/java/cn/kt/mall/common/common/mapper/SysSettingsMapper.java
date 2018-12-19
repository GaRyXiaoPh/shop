package cn.kt.mall.common.common.mapper;

import cn.kt.mall.common.common.entity.LevleConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import cn.kt.mall.common.common.entity.SysSettings;

import java.util.List;

@Mapper
public interface SysSettingsMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(SysSettings record);

	int insertSelective(SysSettings record);

	SysSettings selectByPrimaryKey(@Param("code") String code, @Param("label") String label);

	int updateByPrimaryKeySelective(SysSettings record);

	int updateByPrimaryKey(SysSettings record);

	SysSettings selectByCode(String code);

	List<SysSettings> getTranferConfig();

	int updateByLabel(@Param("label") String label, @Param("mark") String mark);
	// 根据code修改label
	int updateLabelByCode(@Param("label") String label, @Param("code") String code);
	// 获取阿里云oss配置
	List<SysSettings> getOssParam();
	//等级管理链表表
	List<LevleConfig> getLevleConfigList();

	//获取优惠券配置
    List<SysSettings> getCouponParam();
	//获取消费返优惠券配置
    List<SysSettings> getReturnParam();
	//彩票/游戏积分提取续费手续费
    List<SysSettings> getGameParam();
}