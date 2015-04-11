package com.clt.runman.utils;

import java.io.File;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.clt.runman.R;
import com.clt.runman.interfaces.AlertDialogueCallBack;
import com.clt.runman.model.RunMan;
import com.clt.runman.model.RunmanLocationBean;

/**
 * 公共的工具类
 * author: liys
 * Date: 14-11-6
 * Time: 下午5:28
 */
public class CommonUtils {

    /**
     *@Description: 保存信息到SharedPreferences
     *@Author: 张聪
     *@Since: 2015年1月24日下午5:16:11
     *@param context 上下文对象
     *@param sharedFileName 文件名称
     *@param key 键
     *@return
     */
    public static String getSharedPreferences(Context context,String key){
        SharedPreferences tokenSharedPreferences = context.getSharedPreferences (AppConstant.sharePreferenceFileName, Activity.MODE_PRIVATE);
        return tokenSharedPreferences.getString (key, "");
    }

    /**
     *@Description: 从SharedPreferences中取信息
     *@Author: 张聪
     *@Since: 2015年1月24日下午5:16:46
     *@param context 上下文对象
     *@param sharedFileName 文件名称
     *@param key 键
     *@param value 值
     */
    public static void saveSharedPreferences(Context context,String key,String value){
        SharedPreferences tokenSharedPreferences = context.getSharedPreferences (AppConstant.sharePreferenceFileName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenSharedPreferences.edit ();
        editor.putString (key, value);
        editor.commit ();
    }

    /**
     * 保存登录后跑男的uuid
     * @param runManUUID
     * @param context
     */
    public static void saveRunmanUUID(String runManUUID,Context context){
        saveSharedPreferences (context, AppConstant.LOGIN_USER_TOKEN, StringUtils.trimNull (runManUUID));
    }

    /**
     * 获取登录后跑男的uuid
     * @param context
     * @return
     */
    public static String getRunmanUUID(Context context){
        return getSharedPreferences (context, AppConstant.LOGIN_USER_TOKEN);
    }

    /**
     * 获取是否是第一使用app的标识
     * @param context
     * @return
     */
    public static String getFirstUseFlag(Context context){
        return getSharedPreferences (context, AppConstant.FIRST_USE_FLAG);
    }

    /**
     * 保存第一使用的标识
     * @param context
     */
    public static void saveFirstUseFlag(Context context){
        saveSharedPreferences (context, AppConstant.FIRST_USE_FLAG, "true");
    }

    /**
     *@Description: 获取登录跑男信息 
     *@Author: 张聪
     *@Since: 2015年1月22日上午10:42:36
     *@param context
     *@return
     */
    public static RunMan getLoginedRunManInfo(Context context){
        String jsonStr = getSharedPreferences (context, AppConstant.LOGINED_RUN_MAN_INFO);
        return JsonUtils.parseObject (jsonStr, RunMan.class);
    }

    /**
     * 保存登录后的跑男信息
     * @param runman
     * @param ctx
     */
    public static void saveLoginedRunmanInfo(RunMan runman,Context ctx){
        saveSharedPreferences (ctx, AppConstant.LOGINED_RUN_MAN_INFO, JsonUtils.toJsonFromObject (runman));
    }

    /**
     * 保存跑男的地理位置信息
     * @param runmanLoc
     * @param context
     */
    public static void saveRunmanLocInfo(RunmanLocationBean runmanLoc,Context context){
        saveSharedPreferences (context, AppConstant.RUNMAN_LOCATION_INFO, JsonUtils.toJsonFromObject (runmanLoc));
    }

    /**
     * 获取跑男的地理位置信息
     * @param context
     * @return
     */
    public static RunmanLocationBean getRunmanLocInfo(Context context){
        String jsonStr = getSharedPreferences (context, AppConstant.RUNMAN_LOCATION_INFO);
        return JsonUtils.parseObject (jsonStr, RunmanLocationBean.class);
    }

    /**
     * 保存记住密码的标识
     * @param autoLoginFlag
     * @param context
     */
    public static void saveRememberPwdFlag(String autoLoginFlag,Context context){
        saveSharedPreferences (context, AppConstant.REMEMBER_PWD_FLAG, StringUtils.trimNull (autoLoginFlag));
    }

    /**
     * 获取记住密码的标识
     * @param context
     * @return
     */
    public static String getRememberPwdFlag(Context context){
        return getSharedPreferences (context, AppConstant.REMEMBER_PWD_FLAG);
    }

    /**
     * 保存登录密码
     * @param loginPwd
     * @param context
     */
    public static void saveLoginPwd(String loginPwd,Context context){
        saveSharedPreferences (context, AppConstant.LOGIN_PWD, StringUtils.trimNull (loginPwd));
    }

    /**
     * 获取登录密码
     * @param context
     * @return
     */
    public static String getLoginPwd(Context context){
        return getSharedPreferences (context, AppConstant.LOGIN_PWD);
    }

    /**
     * 保存登录账号
     * @param loginAcct
     * @param context
     */
    public static void saveLoginAccount(String loginAcct,Context context){
        saveSharedPreferences (context, AppConstant.LOGIN_ACCOUNT, StringUtils.trimNull (loginAcct));

    }

    /**
     * 获取登录账号
     * @param context
     * @return
     */
    public static String getLoginAccount(Context context){
        return getSharedPreferences (context, AppConstant.LOGIN_ACCOUNT);
    }

    /**
     * 保存个信的clientId
     * @param clientId
     * @param context
     */
    public static void saveGexinClientId(String clientId,Context context){
        if (null != context && !StringUtils.isEmpty (clientId)) {
            saveSharedPreferences (context, AppConstant.GEXIN_CLIENTID, StringUtils.trimNull (clientId));
        }
    }

    /**
     * 获取个信的clientId
     * @param context
     * @return
     */
    public static String getGeXinClientId(Context context){
        return getSharedPreferences (context, AppConstant.GEXIN_CLIENTID);
    }

    /**
     * 保存当前已接到的订单号
     * @param orderId
     */
    public static void saveCurrentReceivedOrderId(String orderId,Context ctx){
        saveSharedPreferences (ctx, AppConstant.ORDER_ID_RECEIVED, StringUtils.trimNull (orderId));
    }

    /**
     * 查询已接单的订单编号
     * @param ctx
     * @return
     */
    public static String queryCurrentReceivedOrderId(Context ctx){
        return getSharedPreferences (ctx, AppConstant.ORDER_ID_RECEIVED);
    }

    /***
     * 保存上传跑男地理位置的标识
     * @param flag
     * @param context
     */
    public static void saveUploadRunmanLocFlag(String flag,Context context){
        saveSharedPreferences (context, AppConstant.NEED_UPLOAD_RUNMAN_FLAG, StringUtils.trimNull (flag));
    }

    /**
     * 获取上传跑男地理位置的标识
     * @param context
     * @return
     */
    public static String getUploadRunmanLocFlag(Context context){
        return getSharedPreferences (context, AppConstant.NEED_UPLOAD_RUNMAN_FLAG);
    }

    /**
     * 获取仅wifi上传文件标识
     */
    public static String getUploadOnlyByWifiFlag(Context context){
        return getSharedPreferences (context, AppConstant.UPLOAD_ONLY_WIFI);
    }

    /*
     * 保存仅wifi上传文件标识
     */
    public static void saveUploadOnlyByWifiFlag(String flag,Context context){
        saveSharedPreferences (context, AppConstant.UPLOAD_ONLY_WIFI, flag);
    }

    /**
     *@Description: 获取uuid
     *@Author: 张聪
     *@Since: 2015年1月22日上午10:43:18
     *@return
     */
    @SuppressLint("DefaultLocale")
    public static String uuid(){
        return UUID.randomUUID ().toString ().replaceAll ("-", "").toUpperCase ();
    }

    /**
     *@Description: 获取请求对应的UserAgent
     *@Author: 张聪
     *@Since: 2015年1月22日上午10:43:29
     *@param activity
     *@return
     */
    public static String reqUserAgent(android.app.Activity activity){

        // 获取手机的分辨率
        DisplayMetrics mDisplayMetrics = new DisplayMetrics ();
        activity.getWindowManager ().getDefaultDisplay ().getMetrics (mDisplayMetrics);
        int screenWidth = mDisplayMetrics.widthPixels;
        int screenHeight = mDisplayMetrics.heightPixels;
        float density = mDisplayMetrics.density;

        // 获取手机的型号信息
        TelephonyManager phoneMgr = (TelephonyManager) activity.getSystemService (Context.TELEPHONY_SERVICE);
        String mobileType = "";
        if (phoneMgr != null) {
            mobileType = android.os.Build.MODEL;// 手机型号
        }

        // 获取手机的网络状态信息
        ConnectivityManager conMan = (ConnectivityManager) activity.getSystemService (Context.CONNECTIVITY_SERVICE);
        String netType = "UNKNOW";
        if (conMan != null) {
            NetworkInfo info = conMan.getActiveNetworkInfo ();
            if (info != null && info.isConnected ()) {
                netType = info.getTypeName ();
            }
        }

        // 获取版本名称和版本号
        int versionCode = 0;
        String versionName = "";
        try {
            PackageManager packagemanager = activity.getPackageManager ();
            String packageName = activity.getPackageName ();
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

        // 手机操作系统的版本号
        String osType = Build.VERSION.RELEASE;

        // 手机唯一串
        String uuid = MD5.md5 (AppUtils.getAndroidId ());

        // 组装userAgent数据
        String userAgent = "android_" + osType + "_" + versionCode + "#" + versionName + "_" + screenWidth + "*" + screenHeight + "*" + density + "_"
                + mobileType + "_" + uuid + "_" + netType;
        return userAgent;
    }

    /**
     *@Description: 判断是否有网络链接
     *@Author: 李彦生
     *@Since: 2015年1月22日上午10:45:59
     *@return
     */
    public static boolean checkNet(){
        try {
            ConnectivityManager connectivity = (ConnectivityManager) AppUtils.getActivity ().getSystemService (Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo ();
                if (info != null && info.isConnected ()) {
                    if (info.getState () == NetworkInfo.State.CONNECTED) { return true; }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     *@Description: 获取登录之前信息
     *@Author: 张聪
     *@Since: 2015年1月22日上午10:42:36
     *@param context
     *@return
     */
    public static RunMan getLoginBeforeAccount(Context context){
        SharedPreferences tokenSharedPreferences = context.getSharedPreferences (AppConstant.sharePreferenceFileName, 0);
        String jsonStr = tokenSharedPreferences.getString (AppConstant.LOGIN_BEFORE, "");
        return JsonUtils.parseObject (jsonStr, RunMan.class);
    }

    /**
     * 动画效果,进入
     *
     * @param context
     * @author liys
     * @date 2015-1-30 上午11:41:06
     */
    public static void inAnim(){
        leftInTranslateAnim ();
    }

    /**
     * 动画效果,退出
     *
     * @param context
     * @author liys
     * @date 2015-1-30 上午11:41:06
     */
    public static void outAnim(){
        rightOutTranslateAnim ();
    }

    /**
     * 左右移动动画效果:左进
     *
     * @param context
     * @author liys
     * @date 2015-1-30 上午11:41:06
     */
    public static void leftInTranslateAnim(){
        AppUtils.getActivity ().overridePendingTransition (R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 左右移动动画效果:右出
     *
     * @param context
     * @author liys
     * @date 2015-1-30 上午11:41:06
     */
    public static void rightOutTranslateAnim(){
        AppUtils.getActivity ().overridePendingTransition (R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 上下移动动画效果:上进
     *
     * @param context
     * @author liys
     * @date 2015-1-30 上午11:41:06
     */
    public static void upInTranslateAnim(){
        AppUtils.getActivity ().overridePendingTransition (R.anim.in_from_top, R.anim.push_static);
    }

    /**
     *@Description: 从下进入
     *@Author: 张聪
     *@Since: 2015-1-31下午2:57:00
     */
    public static void bottomInTranslateAnim(){
        AppUtils.getActivity ().overridePendingTransition (R.anim.in_from_bottom, R.anim.push_static);
    }

    /**
     * 上下移动动画效果:下出
     *
     * @param context
     * @author liys
     * @date 2015-1-30 上午11:41:06
     */
    public static void downOutTranslateAnim(){
        AppUtils.getActivity ().overridePendingTransition (R.anim.push_static, R.anim.push_down_out);
    }

    /**
     * 淡入淡出动画效果
     *
     * @param context
     * @author liys
     * @date 2015-1-30 上午11:41:06
     */
    public static void fadeAlphaAnim(){
        AppUtils.getActivity ().overridePendingTransition (R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * Check service state
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context,String className){
        ActivityManager am = (ActivityManager) context.getSystemService (Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> list = am.getRunningServices (30);
        for ( RunningServiceInfo info : list ) {
            if (info.service.getClassName ().equals (className)) { return true; }
        }
        return false;
    }

    /**
     * 将dp转换成px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context,float dpValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将像素转换成dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context,float pxValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取存放拍照的的目录路径
     * @param context
     * @return
     */
    public static String takePicRootDir(Context context){
        if (SDCardUtils.checkSDCardAvailable ()) {
            return Environment.getExternalStorageDirectory () + File.separator + "runman";
        } else {
            return context.getFilesDir ().getAbsolutePath () + File.separator + "runman";
        }
    }

    /**
     * 获取订单状态描述
     * @param status
     * @return
     */
    public static String getOrderStatusStr(String status){
        status = StringUtils.trimNull (status);
        String statusStr = "--";
        if ("0".equals (status)) {
            statusStr = "待接单";
        } else if ("2000".equals (status)) {
            statusStr = "已接单";
        } else if ("2020".equals (status)) {
            statusStr = "已找到";
        } else if ("2030".equals (status)) {
            statusStr = "洗车前拍照";
        } else if ("2040".equals (status)) {
            statusStr = "正在洗车";
        } else if ("2050".equals (status)) {
            statusStr = "洗车完成";
        } else if ("3000".equals (status)) {
            statusStr = "暂未到账";
        } else if ("4000".equals (status)) {
            statusStr = "已到账";
        } else if ("5000".equals (status)) {
            statusStr = "已撤单";
        }
        return statusStr;
    }

    /**
     * 获取订单描述
     * @param payType
     * @return
     */
    public static String getPaywayStr(String payType){
        payType = StringUtils.trimNull (payType);
        String payTypeStr = "";
        if ("0".equals (payType)) {
            payTypeStr = "现金支付";
        } else if ("1".equals (payType)) {
            payTypeStr = "体验券";
        } else if ("2".equals (payType)) {
            payTypeStr = "微信支付";
        }
        return payTypeStr;
    }

    /**
     * 拨打电话
     * @param tipMsg
     * @param phone
     */
    public static void callPhone(String tipMsg,final String phone,final Context context){
        DialogUtils.showAlertDialog (tipMsg, context, "不拨打", "拨打", new AlertDialogueCallBack () {

            @Override
            public void doCallBack(){
                Intent intent = new Intent (Intent.ACTION_CALL,Uri.parse ("tel:" + phone));
                context.startActivity (intent);
            }
        });
    }

    /***
     * 根据大图获取洗车后照片的缩图
     * @param largeImagePath
     * @return
     */
    public static String getWashcarThumbnailPath(String largeImagePath){
        int index = largeImagePath.lastIndexOf (File.separator);
        String filepath = largeImagePath.substring (0, index);
        String filename = largeImagePath.substring (index);
        filename = filename.substring (0, filename.indexOf (".")) + "_thumbnail.jpg";
        return filepath + filename;
    }

}
