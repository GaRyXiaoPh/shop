package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@ApiModel("查询列表时间排序列表")
@Getter
@Setter
@NoArgsConstructor
public class CurrentTimeVO implements Serializable {

    private Long currentTime;
}
