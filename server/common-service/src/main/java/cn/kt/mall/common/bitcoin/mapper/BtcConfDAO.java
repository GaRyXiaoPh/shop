package cn.kt.mall.common.bitcoin.mapper;


import cn.kt.mall.common.bitcoin.entity.BtcConfEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BtcConfDAO {
    //添加配置
    int add(BtcConfEntity entity);

    //获取kv
    BtcConfEntity getValue(@Param("keyex")String keyex);
}
