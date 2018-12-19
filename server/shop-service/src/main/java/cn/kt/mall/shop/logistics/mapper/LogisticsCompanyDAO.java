package cn.kt.mall.shop.logistics.mapper;

import cn.kt.mall.shop.logistics.vo.LogisticsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogisticsCompanyDAO {
    List<LogisticsVO> getCompanyList();
}
