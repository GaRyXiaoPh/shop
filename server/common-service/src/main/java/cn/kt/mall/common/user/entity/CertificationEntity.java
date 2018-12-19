package cn.kt.mall.common.user.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户身份证信息
 * Created by jerry on 2017/12/21.
 */
@Getter
@Setter
@NoArgsConstructor
public class CertificationEntity implements Serializable {

    private static final long serialVersionUID = -1661830493897252605L;

    /** 主键 */
    private String id;
    /** 用户Id */
    private String userId;
    /** 真实姓名 */
    private String trueName;
    /** 身份证号 */
    private String cardId;
    /** 身份证正面照 */
    private String faceImg;
    /** 身份证反面照 */
    private String conImg;
    /** 手持身份证照 */
    private String holdImg;
    /** 创建时间 or 注册时间 */
    private Date createTime;
    /** 最后更新时间 */
    private Date updateTime;
    // 审核状态，0未审核，1已通过，2已拒绝，3未实名
    private String status;
    //分类（身份证0，其它证件1）
    private String type;

    public CertificationEntity(String userId, String trueName, String cardId,String faceImg,String conImg,String holdImg,String type,String status) {
        this.userId = userId;
        this.trueName = trueName;
        this.cardId = cardId;
        this.faceImg = faceImg;
        this.conImg = conImg;
        this.holdImg = holdImg;
        this.type = type;
        this.status = status;
    }
}
