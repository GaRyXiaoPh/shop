package cn.kt.mall.im.group.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GroupMemberEntity implements Serializable {
    private static final long serialVersionUID = -5481604915309419659L;

    private String id;
    private String groupId;
    private String userId;
    private String nickname;
    private String isManager;
    private String creator;
    private Date createTime;
}
