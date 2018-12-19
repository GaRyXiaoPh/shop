package cn.kt.mall.im.friend.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 黑名单实体类
 * Created by wangjie on 2017/7/11.
 */
@Setter
@Getter
public class HateUserEntity implements Serializable {

    private static final long serialVersionUID = 1744249457643223093L;

    /** 主键 */
    private String id;

    /** 用户Id */
    private String userId;

    /** 黑名单用户Id */
    private String hateId;

    /** 创建时间 */
    private String createTime;
}
