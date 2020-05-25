package com.book.command.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public final class PrintUtil {
    private static Properties properties = PropertiesUtil.load("/print/out.properties");
    private final static String TEMP = properties.getProperty("out.temp");
    private final static String YEAR = "yy";
    private final static String MONTH = "MM";
    private final static String DAY = "dd";
    private final static String HOUR = "HH";
    private final static String MINUTE = "mm";
    private final static String SECOND = "ss";
    private final static String MSG = "{msg}";
    private final static Calendar calendar = Calendar.getInstance();
    public static void print(String info) {
        try {
            info = new String(info.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        System.out.println(getTemp(info));
    }
    private static String getTemp(String msg){
        if(StringUtils.isBlank(TEMP)){
            throw new IllegalArgumentException("请先配置打印信息");
        }
        Date now = new Date();
        calendar.setTime(now);
        String temp = TEMP;
        temp = temp.replace(YEAR,calendar.get(Calendar.YEAR) + "");
        temp = temp.replace(MONTH,calendar.get(Calendar.MONTH) + 1 + "");
        temp = temp.replace(DAY,calendar.get(Calendar.DAY_OF_MONTH) + "");
        temp = temp.replace(HOUR,calendar.get(Calendar.HOUR) + "");
        temp = temp.replace(MINUTE,calendar.get(Calendar.MINUTE) + "");
        temp = temp.replace(SECOND,calendar.get(Calendar.SECOND) + "");
        temp = temp.replace(MSG,msg);
        return temp;
    }
}
