package cn.kt.mall.common.user.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户等级
 * Created by wqt on 2018/2/7.
 */
@Getter
public enum UserLevel {
    DEFAULT("l1", "普通会员", 1),
    L1("l4", "准站长", 2),
    L2("l2", "站长", 3),
    L3("l3", "中心主任", 4),
    L4("l00", "系统总账号", 100);
    private String code;
    private String name;
    //sort越大级别越高
    private int sort;

    private static final Map<String, UserLevel> MAP = new HashMap<>(UserLevel.values().length);

    UserLevel(String code, String name, int sort) {
        this.code = code;
        this.name = name;
        this.sort = sort;
    }

    public static UserLevel getByCode(String code) {
        if(code == null) {
            return null;
        }

        if(MAP.size() != UserLevel.values().length) {
            for(UserLevel level : UserLevel.values()) {
                MAP.put(level.code, level);
            }
        }

        return MAP.get(code);
    }

}
