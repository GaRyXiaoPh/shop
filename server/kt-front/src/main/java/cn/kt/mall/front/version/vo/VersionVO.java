package cn.kt.mall.front.version.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 密码设置信息
 * Created by wqt on 2018/1/29.
 */
@ApiModel(description = "版本信息")
@Getter
@Setter
@NoArgsConstructor
public class VersionVO {

    @ApiModelProperty(notes = "内部版本号", dataType = "int")
    private int code;

    @ApiModelProperty(notes = "外部显示版本号", dataType = "string")
    private String versionName;

    @ApiModelProperty(notes = "platform", dataType = "string")
    private String platform;

    @ApiModelProperty(notes = "url", dataType = "string")
    private String url;

    @ApiModelProperty(notes = "force", dataType = "int")
    private int force;

    @ApiModelProperty(notes = "content", dataType = "string")
    private String content;
}
