package com.clt.runman.push;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.clt.runman.utils.MyLog;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;

/**
 * 个推工具类
 */
public class GeTuiUtil {

    private static String PAGETAG = "GeTuiUtil";

    /**
     * 初始化推送服务
     * @param context
     */
    public static void initialize(Context context){
        PushManager.getInstance ().initialize (context);
    }

    /**
     * 终止SDK的服务
     * @param context
     */
    public static void stopService(Context context){
        PushManager.getInstance ().stopService (context);
    }

    /**
     * 检查SDK当前服务状态
     * @param context
     * @return
     */
    public static boolean isPushTurnOn(Context context){
        return PushManager.getInstance ().isPushTurnedOn (context);
    }

    /**
     * 开启push推送
     * @param context
     */
    public static void turnOnPush(Context context){
        PushManager.getInstance ().turnOnPush (context);
    }

    /**
     * 关闭push推送
     * @param context
     */
    public static void turnOffPush(Context context){
        PushManager.getInstance ().turnOffPush (context);
    }

    /**
     * 获取当前个推的SDK版本
     * @param context
     */
    public static void getVersion(Context context){
        // 测试getVersion获取版本号接口
        String version = PushManager.getInstance ().getVersion (context);
        MyLog.d (PAGETAG, "当前SDK版本为：" + version);
    }

    /**
     * 添加Tag
     * @param context
     * @param tag - tags 以逗号分隔
     */
    public static void addTag(Context context,String tag){
        String[] tags = tag.split (",");
        Tag[] tagParam = new Tag[tags.length];
        for ( int i = 0 ; i < tags.length ; i++ ) {
            Tag t = new Tag ();
            t.setName (tags[i]);
            tagParam[i] = t;
        }

        int i = PushManager.getInstance ().setTag (context, tagParam);
        String text = "ERROR";
        switch (i) {
            case PushConsts.SETTAG_SUCCESS:
                text = "设置标签成功";
                break;
            case PushConsts.SETTAG_ERROR_COUNT:
                text = "设置标签失败，tag数量过大";
                break;
            default:
                text = "设置标签失败，setTag异常";
                break;
        }
        MyLog.d (PAGETAG, "添加Tag：" + text);
    }
    
    /**
     * 解析透传消息
     */
    public static Map<String, String> parseTransmissionMessage(String message) {
        Map<String, String> map = new HashMap<String, String>();
        String[] array = message.split("&");
        for (int i=0; i < array.length; i++) {
            String temp = array[i].trim();
            if (null != temp && temp.length()>0) {
                int position = temp.indexOf("=");
                if (position != -1) {
                    String property = temp.substring(0, position);
                    String value = temp.substring(position+1, temp.length());
                    map.put(property, value);
                }
            }
        }
        return map;
    }
}
