package cn.kt.mall.common.util;


import cn.kt.mall.common.sdk.SdkCredentials;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 加密算法工具类
 * Created by wqt on 2017/8/4.
 */
public class RSAUtils {

    /** *//**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /** *//**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";

    public final static String CHAR_SET = "UTF-8";

    /** *//**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** *//**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /** *//**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static SdkCredentials genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024, SecureRandom.getInstanceStrong());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new SdkCredentials(Base64Utils.encode(publicKey.getEncoded()),
                Base64Utils.encode(privateKey.getEncoded()));
    }

    /** *//**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     *
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return new String(Base64.getEncoder().encode(signature.sign()), CHAR_SET);
    }

    /**
     * 校验数字签名
     *
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     * @return boolean
     * @throws Exception 异常
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * 按私钥解密
     * @param encryptVal 加密后的Base64字符串
     * @param privateKey 私钥的Base64字符串
     * @return byte[]
     * @throws Exception 异常
     */
    public static byte[] decryptByPrivateKey(String encryptVal, String privateKey) throws Exception {
        byte[] encryptData = Base64Utils.decode(encryptVal);
        byte[] keyBytes = Base64Utils.decode(privateKey);
        return decryptByPrivateKey(encryptData, keyBytes);
    }

    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param keyBytes 私钥数据
     * @return byte[]
     * @throws Exception 异常
     */
    private static byte[] decryptByPrivateKey(byte[] encryptedData,  byte[] keyBytes)
            throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        return getFinalBytes(encryptedData, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * 按公钥解密
     * @param encryptVal 加密后的Base64字符串
     * @param privateKey 公钥的Base64字符串
     * @return byte[]
     * @throws Exception 异常
     */
    public static byte[] decryptByPublicKey(String encryptVal, String privateKey) throws Exception {
        byte[] encryptData = Base64Utils.decode(encryptVal);
        byte[] keyBytes = Base64Utils.decode(privateKey);
        return decryptByPublicKey(encryptData, keyBytes);
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param key 公钥数据
     * @return byte[]
     * @throws Exception 异常
     */
    private static byte[] decryptByPublicKey(byte[] encryptedData, byte[] key)
            throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        return getFinalBytes(encryptedData, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * 按公钥加密
     * @param source 源字符串
     * @param key 公钥(BASE64字符)
     * @return Base64
     * @throws Exception 异常
     */
    public static String encryptByPublicKey(String source, String key) throws Exception {
        byte[] data = Base64Utils.decode(source);
        byte[] publicKey = Base64Utils.decode(key);
        byte[] encrypted = encryptByPublicKey(data, publicKey);
        return Base64Utils.encode(encrypted);
    }

    /**
     * 按公钥加密
     * @param data 源数据
     * @param key 公钥(byte[])
     * @return byte[]
     * @throws Exception 异常
     */
    private static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        return getFinalBytes(data, cipher, MAX_ENCRYPT_BLOCK);
    }

    /**
     * 按私钥加密
     * @param source 源字符串
     * @param key 公钥(BASE64字符)
     * @return Base64
     * @throws Exception 异常
     */
    public static String encryptByPrivateKey(String source, String key) throws Exception {
        byte[] data = Base64Utils.decode(source);
        byte[] publicKey = Base64Utils.decode(key);
        byte[] encrypted = encryptByPrivateKey(data, publicKey);
        return Base64Utils.encode(encrypted);
    }

    /**
     * 按私钥加密
     * @param data 源数据
     * @param key 私钥数据
     * @return byte[] 加密后的数据
     * @throws Exception 加密异常
     */
    private static byte[] encryptByPrivateKey(byte[] data, byte[] key)
            throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        return getFinalBytes(data, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * 获取加密解密后数据
     * @param data 源数据
     * @param cipher 加密对象
     * @param maxBlock 操作块大小
     * @return byte[]
     * @throws IllegalBlockSizeException 运算块大小异常
     * @throws BadPaddingException 填充异常
     * @throws IOException IO异常
     */
    private static byte[] getFinalBytes(byte[] data, Cipher cipher, int maxBlock) throws IllegalBlockSizeException, BadPaddingException, IOException {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlock) {
                cache = cipher.doFinal(data, offSet, maxBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlock;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
    
	public static String md5(String plainText) {
		// 首先判断是否为空
		if (StringUtils.isEmpty(plainText)) {
			return null;
		}
		return DigestUtils.md5Hex(plainText);
	}

}
