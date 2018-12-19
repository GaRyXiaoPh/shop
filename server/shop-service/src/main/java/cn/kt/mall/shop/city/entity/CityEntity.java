package cn.kt.mall.shop.city.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CityEntity implements Serializable {

    private static final long serialVersionUID = -8091373752889077725L;
    //id
    private Long sid;
    //名称
    private String name;
    //区列表
    private List<CityEntity> countyList;
    //市列表
    private List<CityEntity> cityList;

}
