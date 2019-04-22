package com.blackheart.brightscreenstatistics.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ConvertTimeUtil {
    public ConvertTimeUtil() {
    }

    /**
     * 毫秒转时分秒
     */
    public static String ms2HHmmss(Long ms) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH小时mm分ss秒");
        Date date = new Date(ms);
        return sdf.format(date);
    }

    /**
     * 毫秒转时分秒
     */
    public static String ms2HHmmssTimeZone0(Long ms) {
        Date date = new Date(ms);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GTM+0"));
        return sdf.format(date);
    }


    /**
     * 毫秒转年月日
     */
    public static String ms2YyyyMMdd(Long ms) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(ms);
        return sdf.format(date);
    }

    /**
     * 毫秒转年月日时分秒
     */
    public static String ms2YyyyMMddHHmmss(Long ms) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(ms);
        return sdf.format(date);
    }

    /**
     * 毫秒转时分秒--0时区
     */
    public static String ms2HHmmTimeZone0(Long ms) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GTM+0"));
        Date date = new Date(ms);
        return sdf.format(date);
    }


}