package com.clt.runman.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class AppUtils 
{
    private final static String TAG = AppUtils.class.getSimpleName();
    private static Context  context;
    private static Activity act;

    public static Activity getActivity(){
        return act;
    }

    public static void setActivity(Activity acti){
        act = acti;
        context = acti;
    }

    public static Context getContext(){
        if (context != null) { return context; }
        return act;
    }

    public static void setContext(Context ctx){
        context = ctx;
    }

    private static String phoneName = null;

    private static String getPhoneName(){
        if (phoneName != null) { return phoneName; }
        String phoneNameTmp = Build.MODEL;
        if (phoneNameTmp == null) {
            phoneNameTmp = "";
        } else {
            phoneNameTmp = phoneNameTmp.replace ("_", "-");
            if (phoneNameTmp.length () > 45) {
                phoneNameTmp = phoneNameTmp.substring (0, 45);
            }
        }
        phoneName = phoneNameTmp;
        return phoneName;
    }

    /**
     * 获取Android客户端信息
     * @param context
     * @return
     */
    public static String getClientInfo(){

        if (context == null) { return ""; }
        WindowManager win = (WindowManager) context.getSystemService ("window");
        if (win == null) { return ""; }
        float scale = context.getResources ().getDisplayMetrics ().density;
        Display display = win.getDefaultDisplay ();
        int h = display.getHeight ();
        int w = display.getWidth ();
        /**高度总是大于或等于宽度***/
        int width = 0;
        int height = 0;
        if (h > w) {
            width = w;
            height = h;
        } else {
            width = h;
            height = w;
        }
        int versionCode = 0;
        String versionName = null;
        try {
            PackageManager packagemanager = context.getPackageManager ();
            String packageName = context.getPackageName ();
            PackageInfo packageInfo = packagemanager.getPackageInfo (packageName, 8192);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            versionCode = 0;
            versionName = "0";
        }
        if (versionName != null) {
            int idx = versionName.indexOf (" ");
            if (idx != -1) {
                versionName = versionName.substring (0, idx);
            }
            idx = versionName.indexOf ("(");
            if (idx != -1) {
                versionName = versionName.substring (0, idx);
            }
        }
        String release = Build.VERSION.RELEASE;
        int idx = release.indexOf ("-");
        if (idx != -1) {
            release = release.substring (0, idx);
        }
        idx = release.indexOf (" ");
        if (idx != -1) {
            release = release.substring (0, idx);
        }
        String userAgent = "a" + release + "_v" + versionCode + "#" + versionName + "_wh" + width + "*" + height + "*" + scale + "_m" + getAndroidId () + "_p"
                + getPhoneName ();
        return userAgent;
    }

    private static String androidId = null;

    /**
     * ANDROID_ID是设备第一次启动时产生和存储的64bit的一个数
     * 缺点：当设备被wipe后该数改变
     * @return
     */
    public static String getAndroidId(){
        if (androidId != null && androidId.length () > 0) { return androidId; }
        String sep = "#&#";
        try {
            /*
             * Build.BOARD // 主板 Build.BRAND // android系统定制商 Build.CPU_ABI // cpu指令集 Build.DEVICE // 设备参数 Build.DISPLAY // 显示屏参数 Build.FINGERPRINT // 硬件名称 Build.HOST Build.ID // 修订版本列表
             * Build.MANUFACTURER // 硬件制造商 Build.MODEL // 版本 Build.PRODUCT // 手机制造商 Build.TAGS // 描述build的标签 Build.TIME Build.TYPE // builder类型 Build.USER
             */
            String aid = Secure.getString (context.getContentResolver (), Secure.ANDROID_ID);
            String info = Build.BOARD + sep + Build.BRAND + sep + Build.CPU_ABI + sep + Build.DEVICE + sep + Build.DISPLAY + sep + Build.HOST + sep + Build.ID
                    + sep + Build.MANUFACTURER + sep + Build.MODEL + sep + Build.PRODUCT + sep + Build.TAGS + sep + Build.TYPE + sep + Build.USER;
            aid = aid + sep + info;
            aid = SecurityUrils.encrypt (aid, null);
            androidId = aid.replace ("_", "-");
            return androidId;
        } catch (Exception e) {}

        return androidId;
    }

    /** 初始化异常处理机制 */
    public static void initExcpHandle(){
        // 崩溃异常捕获
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler () {
            @Override
            public void uncaughtException(Thread thread,Throwable t){
                t.printStackTrace ();
                String cashReport = getCrashReport(act,t);
                Log.e(TAG, cashReport);
            }
        });
    }
    
    /**
     * 获取APP崩溃异常报告
     * @param ex
     * @return
     */
    public static String getCrashReport(Context context, Throwable ex) 
    {
        PackageInfo pinfo = ((AppContext)context.getApplicationContext()).getPackageInfo();
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: "+pinfo.versionName+" ("+pinfo.versionCode+")\n");
        exceptionStr.append("API Level: "+android.os.Build.VERSION.SDK_INT + "\n");
        exceptionStr.append("Android: "+android.os.Build.VERSION.RELEASE+" ("+android.os.Build.MODEL+")\n\n\n");
        exceptionStr.append("异常信息: \n");
        exceptionStr.append("Exception: "+ex.getMessage()+"\n\n\n");
        exceptionStr.append("堆栈信息: \n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) 
        {
        	exceptionStr.append (elements[i].getClassName () + ":" + elements[i].getMethodName () + ":" + elements[i].getLineNumber () + "\n");
        }
        return exceptionStr.toString();
    }
}
