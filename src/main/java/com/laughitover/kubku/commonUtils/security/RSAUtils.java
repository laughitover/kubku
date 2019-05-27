package com.laughitover.kubku.commonUtils.security;

import com.laughitover.kubku.commonUtils.JSONUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 封装同RSA非对称加密算法有关的方法，可用于数字签名，RSA加密解密
 * </p>
 */
@Log4j2
public class RSAUtils {

    /**
     * 建立新的密钥对，以JSON形式返回私钥和公钥
     *
     * @param seed
     * @return JSON
     */
    public static String makeRSAKeyPair(long seed) {
        KeyPair newKeyPair = createKeyPair(seed);
        if (newKeyPair == null) {
            return null;
        }
        PrivateKey priv = newKeyPair.getPrivate();
        byte[] b_priv = priv.getEncoded();
        String privateKey = Base64.encodeBase64String(b_priv);
        PublicKey pub = newKeyPair.getPublic();
        byte[] b_pub = pub.getEncoded();
        String publicKey = Base64.encodeBase64String(b_pub);
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("privateKey", privateKey);
        keyPairMap.put("publicKey", publicKey);
        String keyPair = JSONUtil.toJson(keyPairMap);
        return keyPair;
    }

    /**
     * 新建密钥对
     *
     * @param seed
     * @return KeyPair对象
     */
    private static KeyPair createKeyPair(long seed) {
        KeyPair myPair;
        long mySeed = seed;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.setSeed(mySeed);
            keyGen.initialize(1024, random);
            myPair = keyGen.generateKeyPair();
        } catch (Exception e) {
            return null;
        }
        return myPair;
    }


    /**
     * @param privateKey String形式私钥
     * @param source     String形式元数据
     * @return 加密数据经过base64编码
     */
    public static String encryptByRSAPriv(String privateKey, String source) {
        byte[] privKeyInByte = Base64.decodeBase64(privateKey);
        byte[] data = new byte[0];
        try {
            data = source.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            //不可能
        }
        byte[] temp = encryptByRSAPrivByte(privKeyInByte, data);
        if (temp == null) {
            return null;
        }
        String result = Base64.encodeBase64String(temp);
        return result;
    }


    /**
     * 使用RSA私钥加密数据
     *
     * @param privKeyInByte 打包的byte[]形式私钥
     * @param data          要加密的数据
     * @return 加密数据
     */
    private static byte[] encryptByRSAPrivByte(byte[] privKeyInByte, byte[] data) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
                    privKeyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * @param publicKey String形式公钥
     * @param result    需要解密的数据（经过base64编码）
     * @return
     */
    public static String decryptByRSAPub(String publicKey, String result) {
        byte[] pubKeyInByte = Base64.decodeBase64(publicKey);
        byte[] data = Base64.decodeBase64(result);
        String source = null;
        try {
            byte[] temp = decryptByRSAPubByte(pubKeyInByte, data);
            if (temp == null) {
                return null;
            }
            source = new String(temp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //不可能
        }
        return source;
    }

    /**
     * 用RSA公钥解密
     *
     * @param pubKeyInByte 公钥打包成byte[]形式
     * @param data         要解密的数据
     * @return 解密数据
     */
    private static byte[] decryptByRSAPubByte(byte[] pubKeyInByte, byte[] data) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }


    /**
     * 测试
     */
    public static void main(String[] args) {
//        Map<String, String> map = (Map<String, String>) JsonUtil.toJava(makeRSAKeyPair(System.currentTimeMillis()), HashMap.class);
//        String privateKey = map.get("privateKey");
//        System.out.println("私钥:" + privateKey);
//        String publicKey = map.get("publicKey");
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKAccIZvLbmnZNJ9b0L6vn1ySUFtxjQBUC5iq9UaPf9zp59AA+NBn5K7y+I30g6yvyV7e/QHeVcFwbEJeaka45YkWBW8ZI6FZKgrV1amkKNkdQ8XlagCaLZVjSHptCri7sklwnCb1fa6wc/nqtaWfTJQdVUFzfiCJsSO3URGn/jQIDAQAB";
        System.out.println("公钥:" + publicKey);
//        String result = encryptByRSAPriv(privateKey, "d7e26f579ca0d2c0bd6df4f91fd38c88");
        String result = "MszT6C2zLfwDbp94tAShZq6pS8+XRk60KJ+UjUvzPnz6C5NReTX0FYhkqAwTOBCDJ/YwG6ghEokwmIhczK168XGg4phrhl57nEMfhcayF+cLyp+evuTINNaaEAuGocahEIrSorl1jB3hGyL78oTbdbCT6dA/p9iIm5iZkoq+A20=";
//        System.out.println("私钥加密后:" + result);
        String source = decryptByRSAPub(publicKey, result);
        System.out.println("公钥解密后:" + source);
//        if ("d7e26f579ca0d2c0bd6df4f91fd38c88".equals(source)) {
//            System.out.println("解密成功！");
//        }

    }
}
