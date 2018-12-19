package cn.kt.mall.common.entity;

import cn.kt.mall.common.util.IDUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用实体基类
 * Created by wqt on 2017/10/17.
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 504715887762599964L;

    /** 主键 */
    private String id;

    /** 创建人、操作人 */
    private String creator;

    /** 创建时间 */
    private Date createTime;

    public BaseEntity(String creator) {
        this.id = IDUtil.getUUID();
        this.creator = creator;
    }
}
