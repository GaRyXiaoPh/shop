package cn.kt.mall.shop.address.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AddressEntity implements Serializable {
    private static final long serialVersionUID = -8091373752889077725L;

    private String id;
    private String userId;
    private Long country;
    private Long province;
    private Long city;
    private Long county;
    private String provinceValue;
    private String cityValue;
    private String countyValue;
    private String detailAddress;
    private String recvName;
    private String recvMobile;
    private String zipcode;
    private String def;
    private Date createTime;
    private Date lastTime;
}
