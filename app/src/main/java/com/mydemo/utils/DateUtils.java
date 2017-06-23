package com.mydemo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 获取指定格式的日期
     *
     * @param format 日期格式 yyyy-MM-dd hh:mm:ss	HH代表24小时制，hh代表12小时制
     * @param time
     * @return
     */
    public static String getTime(String format, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 把服务器的字符串日期转换成指定的年月日格式返回
     *
     * @param date
     * @return
     */
    public static String getTime(String format, String date) {
        String d = date.substring(date.indexOf("(") + 1, date.lastIndexOf(")"));
        return getTime(format, Long.parseLong(d));
    }
}
