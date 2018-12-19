package cn.kt.mall.common.sms.vo;

import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.common.sms.entity.SmsRecordEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 短信记录VO
 * Created by wqt on 2017/11/27.
 */
@Getter
@Setter
@NoArgsConstructor
public class SmsRecordVO {

    @ApiModelProperty(notes = "短信记录Id", dataType = "string")
    private String id;

    @ApiModelProperty(notes = "玩家ID", dataType = "string")
    private String uid;

    @ApiModelProperty(notes = "手机号", dataType = "string")
    private String mobile;

    @ApiModelProperty(notes = "短信内容", dataType = "string")
    private String content;

    @ApiModelProperty(notes = "记录时间", dataType = "string")
    private String time;

    public SmsRecordVO(SmsRecordEntity entity, String userId) {
        this.id = entity.getId();
        this.mobile = entity.getMobile();
        this.content = entity.getContent();
        this.time = DateUtil.getTime(entity.getCreateTime());

        this.uid = userId;
    }
}
