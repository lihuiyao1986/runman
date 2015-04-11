package com.clt.runman.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 *@Description:日期工具类
 *@Author:张聪
 *@Since:2015年1月10日下午3:21:52
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

    private DateUtils() {

    }

    /**
     * 获取当前时间对应的时间戳
     * @return
     */
    public static String getNowTimeStamp(){
        Date now = new Date ();
        DateFormat format = new SimpleDateFormat ("yyyyMMddHHmmssSSS");
        return format.format (now);
    }

    /**
     * 获取最后更新时间
     * @return
     */
    public static String getLastUpdateTime(){
        return new SimpleDateFormat ("HH:mm").format (new Date ());
    }

    /**
     * 转换预约时间
     * @param serviceBeginTime
     * @param serviceEndTime
     * @return
     */
    public static String convertHopeTime(String serviceBeginTime,String serviceEndTime){
        String resultStr = "";
        DateFormat format = new SimpleDateFormat ("yyyyMMddHHmmss");
        DateFormat format1 = new SimpleDateFormat ("yyyyMMdd");
        DateFormat format2 = new SimpleDateFormat ("HH:mm");
        DateFormat format3 = new SimpleDateFormat ("MM月dd日");
        try {
            Date beginDate = format.parse (serviceBeginTime);
            Date endDate = format.parse (serviceEndTime);
            Date today = new Date ();
            Calendar calendar = Calendar.getInstance ();
            calendar.setTime (today);
            calendar.add (Calendar.DAY_OF_MONTH, 1);
            Date tommorow = calendar.getTime ();
            String dateStr = "";
            if (format1.format (beginDate).equals (format1.format (today))) {
                dateStr = "今天";
            } else if (format1.format (beginDate).equals (format1.format (tommorow))) {
                dateStr = "明天";
            } else {
                dateStr = format3.format (beginDate);
            }
            String beginTime = format2.format (beginDate);
            String endTime = format2.format (endDate);
            resultStr = dateStr + " " + beginTime + "-" + endTime;
        } catch (ParseException e) {}
        return resultStr;
    }

}
