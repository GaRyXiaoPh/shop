package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@ApiModel("时间搓列表")
@Getter
@Setter
@NoArgsConstructor

public class CurrentTimeResqVO implements Serializable {

    private List<Long> currentTimeVOList;
}
