package cn.kt.mall.shop.good.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GoodPropertyEntity implements Serializable {
    private static final long serialVersionUID = 2973102638932590030L;

    @ApiModelProperty(hidden = true)
    private String id;
    @ApiModelProperty(hidden = true)
    private String goodId;
    @ApiModelProperty("属性名称")
    private String propertyName;
    @ApiModelProperty("属性值")
    private String propertyValue;

    public GoodPropertyEntity(String id, String goodId, String propertyName, String propertyValue){
        this.id = id;
        this.goodId = goodId;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }
}
