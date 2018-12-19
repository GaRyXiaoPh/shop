package cn.kt.mall.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 通用实体基类
 * Created by wqt on 2017/10/17.
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseDataEntity extends BaseEntity {

    private static final long serialVersionUID = 2978693004399723033L;

    /** 最后更新时间 */
    private Date lastTime;

    public BaseDataEntity(String creator) {
        super(creator);
    }
}
