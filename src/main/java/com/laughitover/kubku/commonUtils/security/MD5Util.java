package com.laughitover.kubku.commonUtils.security;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MD5Util {
    private static final String encryModel = "MD5";

    /**
     * 32位md5.
     * 32位小写md5加密
     * @param str
     * @return
     */
    public static String md5(String str) {
        return encrypt(encryModel, str);
    }

    public static String md5WithTrim(String str) {
        String temp = replaceBlank(str);
        return encrypt(encryModel, temp);
    }

    private static String encrypt(String algorithm, String str) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(str.getBytes());
            StringBuffer sb = new StringBuffer();
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i] & 0xFF;
                if (b < 0x10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static void main(String args[]) {
        System.out.println(md5("1后台管理"));
    }
}
