package cn.kt.mall.shop.shop.enums;


import java.util.HashMap;
import java.util.Map;

public enum  ShopLevel {
    DEFAULT("l1", "体验店", 1),
    L1("l2", "零售店", 2),
    L2("l3", "批发店", 3);

    private String code;
    private String name;
    //sort越大级别越高
    private int sort;

    private static final Map<String, ShopLevel> MAP = new HashMap<>(ShopLevel.values().length);

    ShopLevel(String code, String name, int sort) {
        this.code = code;
        this.name = name;
        this.sort = sort;
    }

    public int getSort() {
        return sort;
    }

    public static ShopLevel getByCode(String code) {
        if(code == null) {
            return null;
        }

        if(MAP.size() != ShopLevel.values().length) {
            for(ShopLevel level : ShopLevel.values()) {
                MAP.put(level.code, level);
            }
        }

        return MAP.get(code);
    }
}
