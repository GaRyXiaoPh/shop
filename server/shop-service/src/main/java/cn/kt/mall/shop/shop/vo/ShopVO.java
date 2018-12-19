package cn.kt.mall.shop.shop.vo;

import cn.kt.mall.shop.shop.entity.ShopEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ShopVO implements Serializable {

    @ApiModelProperty("店铺id")
    private String id;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("店铺编号")
    private String shopNo;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("店铺状态，0 平台(自营)商户, 1 地面商户, 2 网上商户")
    private String shopType;
    @ApiModelProperty("店铺等级,  2:零售店 3:批发店")
    private String shopLevel;
    @ApiModelProperty("店铺现金")
    private String shopPoint;
    @ApiModelProperty("店铺级别")
    private int shopRank;
    @ApiModelProperty("店铺销售总计")
    private BigDecimal shopConsume;
    @ApiModelProperty("店铺头像")
    private String avatar;
    @ApiModelProperty("满则减免邮费")
    private BigDecimal feightRate;
    @ApiModelProperty("0申请中, 1审核通过，2审核不通过")
    private String status;

    @ApiModelProperty("注册用户手机号码")
    private String userMobile;
    @ApiModelProperty("收藏状态，0否，1是")
    private int collectType;

    @ApiModelProperty("商户标签code")
    private String shopTag;
    @ApiModelProperty("商户标签值")
    private String shopTagValue;

    @ApiModelProperty("联系人地址")
    private String shopAddress;
    @ApiModelProperty("联系人")
    private String name;
    @ApiModelProperty("联系方式")
    private String mobile;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("累计收益")
    private BigDecimal incomeLem;
    @ApiModelProperty("商品种类数量")
    private int goodCounts;
    @ApiModelProperty("true: 允许订单操作 false:不允许")
    private boolean enableOrderOperation;
    @ApiModelProperty("true: 允许提币操作 false: 不允许")
    private boolean enableWithdrawalOperation;
    @ApiModelProperty("店铺销售额度")
    private BigDecimal shopSalesAmount;
    private String whetherLogistics;
    private String whetherPay;
    private Date createTime;
    //隶属上级标记
    private int pidFlag;
    @ApiModelProperty("信用金")
    private BigDecimal point;

    public static ShopVO fromEntity(ShopEntity entity) {
        ShopVO vo = new ShopVO();
        BeanUtils.copyProperties(entity, vo);
//        vo.setEnableOrderOperation(entity.getWhetherLogistics().equalsIgnoreCase("1"));
//        vo.setEnableWithdrawalOperation(entity.getWhetherPay().equalsIgnoreCase("1"));
        return vo;
    }

    public static List<ShopVO> fromEntity(List<ShopEntity> entityList) {
        List<ShopVO> voList = new ArrayList<>();
        for (ShopEntity se : entityList) {
            voList.add(fromEntity(se));
        }

        return voList;

    }
}
