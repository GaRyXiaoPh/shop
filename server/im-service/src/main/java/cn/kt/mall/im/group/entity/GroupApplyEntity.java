package cn.kt.mall.im.group.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GroupApplyEntity implements Serializable {
    private static final long serialVersionUID = 7915207165507488058L;

    private String id;
    private String groupId;
    private String message;
    private String status;
    private String creator;
    private Date createTime;
    private String operatorId;
    private Date operateTime;
}
