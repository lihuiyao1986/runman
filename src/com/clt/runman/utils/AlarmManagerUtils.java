package com.clt.runman.utils;

import java.io.IOException;
import java.util.Properties;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * 
* @ClassName: AlarmManagerUtils 
* @Description: TODO(闹钟工具类) 
* @author fuxianwei 
* @date 2015-4-9 下午1:37:13 
*
 */
public class AlarmManagerUtils {

    // 定时扫描,默认间隔10分钟
    public static final long DEFAULT_UPLOAD_INTERVAL = 10 * 60 * 1000;

    /**
     * 
    * @Title: sendScanFilesRepeat 
    * @Description: TODO(开启定时扫描文件服务) 
    * @param     
    * @return void    
    * @throws
     */
    public static void sendScanFilesRepeat(Context context){

        long intervalTimes = DEFAULT_UPLOAD_INTERVAL;
        // 读取文件配置
        Properties properties = new Properties ();
        try {
            properties.load (context.getAssets ().open ("property.properties"));
            String time = properties.getProperty ("upload_interval");
            intervalTimes = Integer.parseInt (time) * 60 * 1000;
        } catch (IOException e) {
            intervalTimes = DEFAULT_UPLOAD_INTERVAL;
        } catch (Exception e) {
            intervalTimes = DEFAULT_UPLOAD_INTERVAL;
        }
        Intent service = new Intent ();
        PendingIntent pendingIntent = PendingIntent.getService (context, 0, service, PendingIntent.FLAG_UPDATE_CURRENT);

        // 先取消定时闹钟任务,再打开闹钟任务
        AlarmManager alarmManager = (AlarmManager) context.getSystemService (Context.ALARM_SERVICE);
        alarmManager.cancel (pendingIntent);
        alarmManager.setRepeating (AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime (), intervalTimes, pendingIntent);
    }

    /**
     * 
    * @Title: cancelScanFiles 
    * @Description: TODO(取消闹钟任务,关闭定时上传文件功能) 
    * @param @param context    
    * @return void    
    * @throws
     */
    public static void cancelScanFiles(Context context){

        Intent service = new Intent ();
        PendingIntent pendingIntent = PendingIntent.getService (context, 0, service, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService (Context.ALARM_SERVICE);
        alarmManager.cancel (pendingIntent);
    }

}
