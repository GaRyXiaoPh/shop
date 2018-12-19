package cn.kt.mall.management.admin.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AdminOperationEntity implements Serializable {
    private static final long serialVersionUID = -1661830493897252605L;
    private Integer opId;
    private String name;
    private String englishName;
    private Date createTime;
    private Date lastUpTime;
}
