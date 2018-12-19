package cn.kt.mall.common.wallet.enums;

public enum AssetType {
    CREDIT("point"), //现金，与现金等值
    POPC("popc"); //popc币

    private String type;

    private AssetType(String type) {
        this.type = type;
    }

    public static AssetType getType(String state) {
        for (AssetType deviceType : AssetType.values()) {
            if (deviceType.type.equalsIgnoreCase(state)) {
                return deviceType;
            }
        }
        throw new RuntimeException(state + " is not a valid Asset Type!");
    }

    public String getStrType() {
        return type;
    }
}