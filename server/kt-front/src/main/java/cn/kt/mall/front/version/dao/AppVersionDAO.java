package cn.kt.mall.front.version.dao;

import cn.kt.mall.front.version.vo.VersionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018/1/31.
 */

@Mapper
public interface AppVersionDAO {

    VersionVO getLastVersion(@Param("platform") String platform);

}
