package com.laughitover.kubku.commonUtils.security;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;

/**
 * 运算模式CBC。<br>
 * 在CBC模式下使用key,向量iv;<br>
 * 对字符加密时，双方采用的都是UTF-8编码
 *
 * @version 1.0.0
 */
@Log4j2
public class DesUtil {

    // 向量
    private static final byte[] keyiv = {1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * CBC解密
     * @param key  密钥
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static byte[] desDecodeCBC(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESKeySpec spec = new DESKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * CBC解密
     * @param key  密钥
     * @param data Base64加密后的密文
     * @return 明文
     * @throws Exception
     */
    public static String desDecodeCBC(String key, String data) {
        byte[] _data = Base64.decodeBase64(data);
        byte[] _key = new byte[0];
        try {
            _key = key.getBytes("UTF-8");
            byte[] bOut = desDecodeCBC(_key, _data);
            return new String(bOut, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
        return null;
    }

    /**
     * CBC加密
     * @param key  密钥
     * @param data 明文
     * @return 密文
     * @throws Exception
     */
    public static byte[] desEncodeCBC(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESKeySpec spec = new DESKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); // 加密方法／运算模式／填充模式
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * CBC加密
     * @param key  密钥
     * @param data 明文
     * @return Base64加密后的密文
     * @throws Exception
     */
    public static String desEncodeCBC(String key, String data) {
        byte[] _data = new byte[0];
        try {
            _data = data.getBytes("UTF-8");
            byte[] _key = key.getBytes("UTF-8");
            byte[] bOut = desEncodeCBC(_key, _data);
            return Base64.encodeBase64String(bOut); // Base64加密后的密文
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String key = "hdhhdhdmZtmcOlmT2";
        String data = "bd35f82a09b211e9881072153045f609_entityDetail_REGISTER_2018122797771658@laughitover";
        System.out.println("加密解密 测试 ");
        String str1 = desEncodeCBC(key, data);// 加密
        System.out.println("加密："+str1);
//        System.out.println(URLEncoder.encode(str1, "utf-8"));
        String str2 = desDecodeCBC(key, str1);// 解密
        System.out.println("解密："+str2);
    }
}