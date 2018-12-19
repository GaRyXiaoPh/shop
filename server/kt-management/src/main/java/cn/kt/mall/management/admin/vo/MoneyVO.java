package cn.kt.mall.management.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MoneyVO implements Serializable {

    private static final long serialVersionUID = -862265174963657895L;
    private String id;
    private String userId;
    private Date createTime;
    private Date updateTime;
    private long money;
    private String nick;
    private String userName;
    private String mobile;
    private String bankCard;
    private String bankName;
    private String openBank;
    private List<String> statusList;
    @ApiModelProperty(notes = "备注")
    private String remark;
    @ApiModelProperty(notes = "钱包地址")
    private String coinAddress;
}
