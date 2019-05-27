package com.laughitover.kubku.commonUtils;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: wangjs
 * @Date: 2019/5/27 9:53
 * @description: 字符串相关方法
 */
public class StringUtil extends StringUtils {
    /**
     * 根据分隔符分割字符串
     *
     * @param str
     * @param separator
     * @return List<String>
     */
    public static List<String> splitString(String str, String separator) {
        List<String> list = new ArrayList<>();
        if (StringUtil.isNotEmpty(str)) {
            String[] subList = str.split(separator);
            Collections.addAll(list, subList);
        }
        return list;
    }

    /**
     * 数组转字符串
     * @param array
     * @param separator
     * @return
     */
    public static String arrayToSegString(Object[] array, String separator) {
        StringBuilder sb = new StringBuilder();
        if (array != null && array.length > 0){
            for (Object object : array) {
                sb.append(object).append(separator);
            }
        }
        return sb.length() == 0 ? sb.toString() : sb.substring(0, sb.length() - separator.length());
    }

    /**
     * list转字符串
     * @param list
     * @param separator
     * @return
     */
    public static String listToSegString(List list, String separator) {
        StringBuilder sb = new StringBuilder();
        if(list!=null&& list.size()>0){
            for (Object object : list) {
                sb.append(object).append(separator);
            }
        }
        return sb.length() == 0 ? sb.toString() : sb.substring(0, sb.length() - separator.length());
    }

    /**
     * 对象转字符串
     * @param object
     * @return
     */
    public static String safeToString(Object object) {
        return object == null ? null : object instanceof String ? (String) object : object.toString();
    }

    /**
     * 对象转字符串（有默认值）
     * @param object
     * @param defaults
     * @return
     */
    public static String safeToString(Object object, String defaults) {
        return object == null ? defaults : object instanceof String ? (String) object : object.toString();
    }

    /**
     * 字符串首字母大写，其余不变
     * @param src
     * @return
     */
    public static String headUpperCase(String src) {
        if (isEmpty(src)) {
            return src;
        }
        if (src.length() == 1) {
            return src.toUpperCase();
        }
        return src.substring(0, 1).toUpperCase() + src.substring(1);
    }

    /**
     * 字符串首字母小写，其余不变
     * @param src
     * @return
     */
    public static String headLowerCase(String src) {
        if (isEmpty(src)) {
            return src;
        }
        if (src.length() == 1) {
            return src.toLowerCase();
        }
        return src.substring(0, 1).toLowerCase() + src.substring(1);
    }

    private static final char SEPARATOR = '_';

    public static String toUnderlineName(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 驼峰转下划线 大写
     * @param s
     * @return
     */
    public static String toUnderlineNameUpper(String s) {
        if (s == null) {
            return null;
        }
        return toUnderlineName(s).toUpperCase();
    }

    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.toUnderlineName("ISOCertifiedStaff"));
        System.out.println(StringUtil.toUnderlineName("CertifiedStaff"));
        System.out.println(StringUtil.toUnderlineName("UserID"));
        System.out.println(StringUtil.toUnderlineNameUpper("timeCreate"));
        System.out.println(StringUtil.toUnderlineNameUpper("amount"));
        System.out.println(StringUtil.toUnderlineNameUpper("dueDate"));
        System.out.println(StringUtil.toCamelCase("iso_certified_staff"));
        System.out.println(StringUtil.toCamelCase("certified_staff"));
        System.out.println(StringUtil.toCamelCase("user_id"));
        System.out.println(StringUtil.toCamelCase("loan_request_id"));
        System.out.println(StringUtil.toCapitalizeCamelCase("loan_request_id"));
        System.out.println(StringUtil.toCapitalizeCamelCase("loanRequestId"));
    }

}
