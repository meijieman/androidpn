package org.androidpn.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);

    /**
     * 格式化日期
     */
    public static String format(Date date) {
        return DEFAULT_SDF.format(date);
    }

    /**
     * 计算两个日期差值
     */
    public static String dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            long day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒

            return day + "天" + hour + "时" + min + "分" + sec + "秒";
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
