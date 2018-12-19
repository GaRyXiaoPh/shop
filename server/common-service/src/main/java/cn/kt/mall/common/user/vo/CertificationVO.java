package cn.kt.mall.common.user.vo;

import cn.kt.mall.common.user.entity.CertificationEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 身份证信息VO
 * Created by jerry on 2017/12/29.
 */
@ApiModel(description = "用户添加身份证信息")
@Getter
@Setter
@NoArgsConstructor
public class CertificationVO  implements Serializable {
    private static final long serialVersionUID = -1661830493897252605L;
    @ApiModelProperty(hidden = true)
    private String userId;

    @ApiModelProperty(notes = "真实姓名", dataType = "string")
    private String trueName;

    @ApiModelProperty(notes = "身份证正面照", dataType = "string")
    private String faceImg;

    @ApiModelProperty(notes = "身份证反面照", dataType = "string")
    private String conImg;

    @ApiModelProperty(notes = "手持身份证照", dataType = "string")
    private String holdImg;

    @ApiModelProperty(notes = "身份证号", dataType = "string")
    private String cardId;

    @ApiModelProperty(notes = "分类", dataType = "string")
    private String type;
    // 审核状态，0未审核，1已通过，2已拒绝，3未实名
    private String status;
    public CertificationEntity getCertificationEntity() {

        return new CertificationEntity(userId, trueName,cardId,faceImg,conImg,holdImg,type,status);
    }
}
