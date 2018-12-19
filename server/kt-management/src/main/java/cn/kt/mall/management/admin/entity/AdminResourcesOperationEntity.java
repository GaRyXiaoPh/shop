package cn.kt.mall.management.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminResourcesOperationEntity {

    private Integer resId;
    private Integer opId;
    private Integer roleId;
}
