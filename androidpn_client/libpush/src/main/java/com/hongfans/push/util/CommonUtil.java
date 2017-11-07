package com.hongfans.push.util;

import java.util.List;

/**
 * TODO
 * Created by MEI on 2017/11/6.
 */
public class CommonUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(List list){
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List list){
        return !isEmpty(list);
    }

    public static boolean isEmpty(String[] strs) {
        return strs == null || strs.length == 0;
    }
}
