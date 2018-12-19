package cn.kt.mall.management.admin.service;

import cn.kt.mall.common.http.Success;
import cn.kt.mall.management.admin.vo.AddRoleVO;
import cn.kt.mall.management.admin.vo.AdminResourcesVO;

import java.util.List;

public interface AuthorityCacheService {



    List<AdminResourcesVO> getMenuListCache(Integer roleId);

    Success deleteVisibleResourceListCache(Integer roleId);

    void addRoleResourceAndOperationCache(Integer roleId, AddRoleVO addRoleVO);
}
