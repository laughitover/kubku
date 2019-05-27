package com.laughitover.kubku.api;

/**
 * @Description: 类型转换
 * @Author: wanglin
 * @CreateDate: 2018/8/27 14:46
 * Copyright: Copyright (c) 2018
 */
public interface BeanConverter<S,T> {
    /**
     *
     * @param instance  原对象实例
     * @param <S>  原对象类
     * @param <T>  要转换成的对象类
     * @return
     */
    <S,T> T convert(S instance) ;
}
