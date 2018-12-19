package cn.kt.mall.shop.collect.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopCollectVO implements Serializable{

	private static final long serialVersionUID = 7161472572882618877L;
	private String id;
    private String shopName;
    private String shopType;
    private String shopPoint;
    private String avatar;
    private String address;
    private String shopTagValue;
}
