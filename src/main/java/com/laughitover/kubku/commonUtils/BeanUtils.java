package com.laughitover.kubku.commonUtils;

import com.laughitover.kubku.api.BeanConverter;
import com.laughitover.kubku.form.BaseEnum;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Description: bean工具类 （反射实现）
 * @CreateDate: 2018/1/26 14:48
 * Copyright: Copyright (c) 2018
 */
public class BeanUtils {

    /**
     * 获取对象的属性
     *
     * @param src
     * @param propertyName
     * @param <S>
     * @return
     */
    public static <S> Object getProperty(S src, String propertyName) {
        return invokeGetMethod(src, propertyName, null);
    }

    /**
     * 复制Bean 属性（依据属性名称 不为null的进行复制）
     *
     * @param src
     * @param dest
     * @param <S>
     * @param <D>
     * @return
     */
    public static <S, D> D copyValue(S src, D dest) {
        if (src == null) return null;
        Field[] fields = getAllDeclaredFields(src.getClass());
        for (Field field : fields) {
            String fieldName = field.getName();
            if (fieldName.toUpperCase().equals("SERIALVERSIONUID")) {
                continue;
            }
            Object requestObj;
            if ((requestObj = invokeGetMethod(src, fieldName, null)) == null) {
                continue;
            }

            try {
                getDeclaredField(dest.getClass(), (fieldName));
                invokeSetMethod(dest, fieldName, requestObj);
            } catch (NoSuchFieldException e) {
                continue;
            }
        }
        return dest;
    }

    /**
     * 支持 Date pattern  yyyy-MM-dd 格式的日期类型
     *
     * @param map
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E> E mapToBean(Map<String, String> map, Class clazz) {
        if (map == null) return null;
        Object data = null;
        E e = null;
        try {
            e = (E) clazz.newInstance();
        } catch (InstantiationException e1) {
            return null;
        } catch (IllegalAccessException e1) {
            return null;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Field temFiels = null;
            try {
                temFiels = BeanUtils.getDeclaredField(clazz, entry.getKey());
                temFiels.setAccessible(true);
                Class filedClazz = temFiels.getType();
                if (filedClazz.isEnum() && BaseEnum.class.isAssignableFrom(filedClazz)) {
                    for (Object object : filedClazz.getEnumConstants()) {
                        if (((BaseEnum) object).getKey().equals(entry.getValue())) {
                            data = object;
                        }
                    }
                } else if (String.class.isAssignableFrom(filedClazz)) {
                    data = entry.getValue();
                } else if (long.class.isAssignableFrom(filedClazz)) {
                    data = Double.valueOf(entry.getValue()).longValue();
                } else if (Date.class.isAssignableFrom(filedClazz)) {
                    data = DateUtils.parseDay(entry.getValue());
                    if (data == null) {
                        System.out.print(entry.getKey() + " property's Date pattern is not correct");
                    }
                }
                temFiels.set(e, data);
            } catch (NoSuchFieldException exception) {
//                exception.printStackTrace();
            } catch (IllegalAccessException e1) {
//                e1.printStackTrace();
            }
        }
        return e;
    }

    /**
     * 根据Bean 的属性值 构建查询条件
     *
     * @param request
     * @param criteria
     * @return
     */
    public static Object getQueryExample(Object request, Object criteria) {
        return getQueryExample(request, criteria, true);
    }

    /**
     * 根据Bean 的属性值 构建查询条件
     * 不包含List的 查询条件
     *
     * @param request
     * @param criteria
     * @return
     */
    public static Object getNoListQueryExample(Object request, Object criteria) {
        return getQueryExample(request, criteria, false);
    }

    /**
     * 根据Bean 的属性值 构建查询条件
     *
     * @param request
     * @param criteria
     * @return
     */
    public static Object getQueryExample(Object request, Object criteria, boolean containList) {
        Field[] fields = getAllDeclaredFields(request.getClass());
        for (Field field : fields) {
            String fieldName = field.getName();
            if (fieldName.toUpperCase().equals("SERIALVERSIONUID")) {
                continue;
            }

            Object requestObj;
            if ((requestObj = invokeGetMethod(request, fieldName, null)) == null) {
                continue;
            }

            // 不包含List的条件
            if (!containList && field.getType().getSimpleName().endsWith("List")) {
                continue;
            }

            if (field.getType().getSimpleName().endsWith("List")) {
                List requestList = (List) requestObj;
                if (isListEmpty(requestList)) {
                    continue;
                }
                String orCriteriaMethod = andCriteriaIn(fieldName);
                try {
                    Method method = criteria.getClass().getDeclaredMethod(orCriteriaMethod, List.class);
                    method.invoke(criteria, requestList);
                } catch (NoSuchMethodException e) {
                    continue;
                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
                }
                continue;
            }
            String criteriaMethod = andCriteriaEqual(fieldName);
            try {
                Method method = getDeclaredMethod(criteria.getClass(), criteriaMethod, new Class[]{requestObj.getClass()}, Boolean.TRUE);
                if (requestObj.getClass().isEnum()) {
                    requestObj = ((Enum) requestObj).name();
                }
                method.invoke(criteria, requestObj);
            } catch (Exception e) {
                System.out.print("method.invoke failed for " + criteriaMethod);
                continue;
            }
        }
        return criteria;
    }

    /**
     * 执行某个Field的getField方法
     *
     * @param obj       类实例
     * @param fieldName 类的属性名称
     * @param args      参数，默认为null
     * @return
     */
    private static Object invokeGetMethod(Object obj, String fieldName, Object[] args) {
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = null;
        try {
            method = getDeclaredMethod(obj.getClass(), "get" + methodName);
            return method.invoke(obj);
        } catch (Exception e) {
            System.out.print("getDeclaredMethod failed for " + methodName + e);
            return "";
        }
    }

    public static String getCamelFieldName(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static String andCriteriaEqual(String fieldName) {
        return "and" + getCamelFieldName(fieldName) + "EqualTo";
    }

    public static String andCriteriaIn(String fieldName) {
        return "and" + getCamelFieldName(fieldName) + "In";
    }


    public static Field[] getAllDeclaredFields(Class<?> tempClass) {
        Field[] fields = new Field[0];
        List<Field> fieldList = new ArrayList<Field>();
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList.toArray(fields);
    }

    public static Method[] getAllDeclaredMethods(Class<?> tempClass) {
        List<Method> methodList = new ArrayList<Method>();
//        Class tempClass = ;
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            methodList.addAll(Arrays.asList(tempClass.getDeclaredMethods()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        return methodList.toArray(new Method[0]);
    }

    public static Method getDeclaredMethod(Class<?> tempClass, String methodName) throws NoSuchMethodException {
        Method method = null;
//        Class tempClass = ;
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            try {
                method = tempClass.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
            }
            if (method != null) {
                break;
            }
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        if (method == null) {
            throw new NoSuchMethodException(methodName);
        }
        return method;
    }

    public static Field getDeclaredField(Class<?> tempClass, String fieldName) throws NoSuchFieldException {
        Field field = null;
        while (tempClass != null) {
            try {
                field = tempClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {

            }

            if (field != null) {
                break;
            }
            tempClass = tempClass.getSuperclass();
        }
        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        }
        return field;
    }

    public static Method getDeclaredMethod(Class<?> tempClass, String methodName, Class[] paramClass, boolean isQuery) throws NoSuchMethodException {
        Method method = null;
//        Class tempClass = ;
        //把枚举转化成String类
        if (paramClass[0].isEnum() && isQuery) {
            paramClass[0] = String.class;
        }
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            try {
                method = tempClass.getDeclaredMethod(methodName, paramClass);
            } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
            }
            if (method != null) {
                break;
            }
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        if (method == null) {
            throw new NoSuchMethodException(methodName);
        }
        return method;
    }

    /**
     * 执行某个Field的setField方法
     *
     * @param dest      类实例
     * @param fieldName 类的属性名称
     * @param args      参数，默认为null
     * @return
     */
    private static Object invokeSetMethod(Object dest, String fieldName, Object args) {
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = null;
        try {
            Class[] parameterTypes = new Class[1];
            Class c = dest.getClass();
            Field field = getDeclaredField(c, fieldName);
            parameterTypes[0] = field.getType();
            if (args.getClass().isEnum() && field.getType().equals(String.class)) {
                args = ((Enum) args).name();
            } else if (field.getType().isEnum() && !args.getClass().isEnum()) {
                Class enumClass = field.getType();
                args = EnumUtils.getEnum(enumClass, args.toString());
            }
            method = getDeclaredMethod(dest.getClass(), "set" + methodName, parameterTypes, Boolean.FALSE);
            return method.invoke(dest, args);
        } catch (Exception e) {
            System.out.println("obj: " + dest + " args:" + args + fieldName);
            return null;
        }
    }

    public static boolean isListEmpty(List list) {
        if (CollectionUtils.isEmpty(list)) return true;
        for (Object o : list) {
            if (o == null) return true;
        }
        return false;
    }

    public static <S, T> T convert(S s, BeanConverter beanConverter) {
        return (T) beanConverter.convert(s);
    }

    public static <S, T> List<T> convertList(List<S> list, BeanConverter beanConverter) {
        if (CollectionUtils.isEmpty(list)) return Collections.emptyList();
        List tList = new ArrayList();
        for (S s : list) {
            tList.add(beanConverter.convert(s));
        }
        return tList;
    }
}
