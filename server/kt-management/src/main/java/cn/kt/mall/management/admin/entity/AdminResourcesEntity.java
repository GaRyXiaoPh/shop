package cn.kt.mall.management.admin.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AdminResourcesEntity implements Serializable {
    private static final long serialVersionUID = -1661830493897252605L;

    private Integer resId;
    private String name;
    private Integer resLever;
    private String url;
    private Integer preId;
    private Integer sortIndex;
    private Date createTime;
    private Date lastUpTime;
}
