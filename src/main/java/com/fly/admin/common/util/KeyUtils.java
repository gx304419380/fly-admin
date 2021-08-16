package com.fly.admin.common.util;

import com.fly.admin.common.exception.BaseException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.fly.admin.common.constant.SystemErrorMessage.DECODE_ERROR;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@UtilityClass
@Slf4j
public class KeyUtils {
    private static final KeyPairGenerator GENERATOR;
    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();
    public static final String RSA = "RSA";

    static {
        try {
            GENERATOR = KeyPairGenerator.getInstance(RSA);
            GENERATOR.initialize(1024);
        } catch (NoSuchAlgorithmException e) {
            log.error("init key utils fail:", e);
            throw new BaseException(e);
        }
    }

    /**
     * 生成密钥对
     *
     * @return 公钥
     */
    public static String generateKeyPair() {
        KeyPair keyPair = GENERATOR.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String privateKeyString = ENCODER.encodeToString(privateKey.getEncoded());
        String publicKeyString = ENCODER.encodeToString(publicKey.getEncoded());

        CacheUtils.put(publicKeyString, privateKeyString);
        return publicKeyString;
    }


    /**
     * 根据公钥获取私钥
     *
     * @param publicKey 公钥
     * @return 私钥
     */
    public static String getPrivateKey(String publicKey) {
        String key = CacheUtils.get(publicKey, String.class);
        CacheUtils.remove(publicKey);
        return key;
    }


    /**
     * RSA私钥解密
     *
     * @param value      加密字符串
     * @param privateKey 私钥
     * @return 铭文
     */
    public static String decrypt(String value, String privateKey) {
        try {
            return doDecrypt(value, privateKey);
        } catch (Exception e) {
            throw new BaseException(DECODE_ERROR, e);
        }
    }


    public static String doDecrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = DECODER.decode(str);
        //base64编码的私钥
        byte[] decoded = DECODER.decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(RSA).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }


    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = DECODER.decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return ENCODER.encodeToString(cipher.doFinal(str.getBytes()));
    }

}
