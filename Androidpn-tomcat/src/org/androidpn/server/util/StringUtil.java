package org.androidpn.server.util;

/**
 * TODO
 * Created by MEI on 2017/11/6.
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
