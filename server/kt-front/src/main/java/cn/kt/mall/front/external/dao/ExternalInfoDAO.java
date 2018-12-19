package cn.kt.mall.front.external.dao;

import cn.kt.mall.front.external.entity.NationalCodeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 外部信息数据访问类
 * Created by wqt on 2018/1/29.
 */
@Mapper
public interface ExternalInfoDAO {

    List<NationalCodeEntity> getNationalCodes();
}
