package cn.kt.mall.management.logistics.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@ApiModel("查询请求")
@Getter
@Setter
@NoArgsConstructor
public class InneriornoResqVO implements Serializable {

    private List<String> inneriornoList;

    private List<String> goodNames;
}
