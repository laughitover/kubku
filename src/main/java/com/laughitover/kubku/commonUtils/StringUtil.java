package com.laughitover.kubku.commonUtils;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: wangjs
 * @Date: 2019/5/27 9:53
 * @description:
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
     * 把数组转成字符串
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

    public static String listToSegString(List list, String separator) {
        StringBuilder sb = new StringBuilder();
        if(list!=null&& list.size()>0){
            for (Object object : list) {
                sb.append(object).append(separator);
            }
        }
        return sb.length() == 0 ? sb.toString() : sb.substring(0, sb.length() - separator.length());
    }

    public static String safeToString(Object object) {
        return object == null ? null : object instanceof String ? (String) object : object.toString();
    }

    public static String safeToString(Object object, String defaults) {
        return object == null ? defaults : object instanceof String ? (String) object : object.toString();
    }

    public static String headUpperCase(String src) {
        if (isEmpty(src)) {
            return src;
        }
        if (src.length() == 1) {
            return src.toUpperCase();
        }
        return src.substring(0, 1).toUpperCase() + src.substring(1);
    }

    public static String headLowerCase(String src) {
        if (isEmpty(src)) {
            return src;
        }
        if (src.length() == 1) {
            return src.toLowerCase();
        }
        return src.substring(0, 1).toLowerCase() + src.substring(1);
    }

}
