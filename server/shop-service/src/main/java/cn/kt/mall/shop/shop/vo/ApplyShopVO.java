package cn.kt.mall.shop.shop.vo;

import cn.kt.mall.common.common.SerialCodeGenerator;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.shop.shop.constant.Constants;
import cn.kt.mall.shop.shop.entity.ShopAuthdataEntity;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyShopVO {

	@ApiModelProperty(notes = "个人姓名")
	private String name;
	@ApiModelProperty(notes = "个人手机号")
	private String mobile;
	@ApiModelProperty(notes = "个人email")
	private String email;
	@ApiModelProperty(notes = "个人地址")
	private String address;

	@ApiModelProperty(notes = "商铺业务分类（线上商铺取商品一级分类）")
	private String shopTag;

	@ApiModelProperty(notes = "公司名称")
	private String company;
	@ApiModelProperty(notes = "省")
	private Long province;
	@ApiModelProperty(notes = "市")
	private Long city;
	@ApiModelProperty(notes = "县")
	private Long county;
	@ApiModelProperty(notes = "详细地址")
	private String detailAddress;
	@ApiModelProperty(notes = "经度")
	private String addressLon;
	@ApiModelProperty(notes = "纬度")
	private String addressLat;
	@ApiModelProperty(notes = "商户类型")
	private String companyType;
	@ApiModelProperty(notes = "商户电话")
	private String companyMobile;
	@ApiModelProperty(notes = "商户Email")
	private String companyEmail;
	@ApiModelProperty(notes = "商户图片")
	private String companyImg;
	@ApiModelProperty(notes = "营业编号")
	private String businessNo;
	@ApiModelProperty(notes = "营业范围")
	private String businessScope;
	@ApiModelProperty(notes = "营业证明图片1")
	private String businessImg1;
	private String businessImg2;
	private String businessImg3;
	private String businessImg4;
	private String businessImg5;
	@ApiModelProperty("门牌号")
	private String addressNo;

	public ShopEntity getShopEntity(String userId, String shopId, String shopType) {
		ShopEntity shopEntity = new ShopEntity();
		shopEntity.setId(shopId);
		shopEntity.setUserId(userId);
		shopEntity.setShopNo(SerialCodeGenerator.getNext());
		shopEntity.setShopName(this.company);
		shopEntity.setAddress(this.address);
		shopEntity.setShopType(shopType);
		shopEntity.setName(this.name);
		shopEntity.setMobile(this.mobile);
		shopEntity.setEmail(this.email);
		shopEntity.setStatus(Constants.ShopStatus.SHOP_APPLY);
		shopEntity.setShopTag(this.shopTag);
		shopEntity.setShopAddress(detailAddress);
		shopEntity.setAvatar(this.companyImg);
		shopEntity.setShopLevel("0");
		shopEntity.setShopPoint("0");
		return shopEntity;
	}

	public ShopAuthdataEntity getShopAuthdataEntity(String userId, String shopId) {
		ShopAuthdataEntity authdataEntity = new ShopAuthdataEntity();
		authdataEntity.setId(IDUtil.getUUID());
		authdataEntity.setUserId(userId);
		authdataEntity.setShopId(shopId);
		authdataEntity.setProvince(this.province);
		authdataEntity.setCity(this.city);
		authdataEntity.setCounty(this.county);
		authdataEntity.setDetailAddress(this.detailAddress);
		authdataEntity.setAddressLon(this.addressLon);
		authdataEntity.setAddressLat(this.addressLat);
		authdataEntity.setCompany(this.company);
		authdataEntity.setCompanyType(this.companyType);
		authdataEntity.setCompanyMobile(this.companyMobile);
		authdataEntity.setCompanyImg(this.companyImg);
		authdataEntity.setCompanyEmail(this.companyEmail);
		authdataEntity.setBusinessNo(this.businessNo);
		authdataEntity.setBusinessScope(this.businessScope);
		authdataEntity.setBusinessImg1(this.businessImg1);
		authdataEntity.setBusinessImg2(this.businessImg2);
		authdataEntity.setBusinessImg3(this.businessImg3);
		authdataEntity.setBusinessImg4(this.businessImg4);
		authdataEntity.setBusinessImg5(this.businessImg5);
		authdataEntity.setAddressNo(this.addressNo);
		return authdataEntity;
	}
}
