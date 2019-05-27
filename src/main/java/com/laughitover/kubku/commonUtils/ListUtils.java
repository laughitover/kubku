package com.laughitover.kubku.commonUtils;

import org.springframework.cglib.core.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 集合工具类
 * @CreateDate: 2018/1/3 14:53
 * Copyright: Copyright (c) 2018
 */
public class ListUtils {

    public static boolean isEmpty(List list) {
        return list != null && list.size() > 0;
    }

    public static List<String> enumToString(List<Enum> statusList) {
        List<String> result = new ArrayList<>();
        if (!isEmpty(statusList)) {
            for (Enum status : statusList) {
                result.add(status.name());
            }
        }
        return result;
    }

}
