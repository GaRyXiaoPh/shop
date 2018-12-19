package cn.kt.mall.common.sdk;

import java.io.Serializable;

/**
 * IM SDK 访问证书
 * Created by wqt on 2017/8/4.
 */
public class SdkCredentials implements Serializable {

    private static final long serialVersionUID = 6842256517321252836L;

    private String publicKey;

    private String privateKey;

    public SdkCredentials(String appKey) {
        super();
    }

    public SdkCredentials(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
