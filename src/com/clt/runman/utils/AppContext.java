package com.clt.runman.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.clt.runman.R;
import com.clt.runman.interfaces.BaiMapLocationCallBack;
import com.clt.runman.push.GeTuiUtil;
import com.clt.runman.service.DeleteWashcarPicService;
import com.clt.runman.service.LocationService;
import com.clt.runman.upload.NetworkConnectionChangeReceiver;

/**
 * Created by yanshengli on 15-1-17.
 */
public class AppContext extends Application {

    /** 百度地图定位的client **/
    public LocationClient                   mLocationClient;
    /** 获取定位结果的回调接口 **/
    public MyLocationListener               mMyLocationListener;
    /** 百度定位结果回调接口 **/
    private BaiMapLocationCallBack          locationCallBack;

    /** 设备状态中文名称 **/
    public String                           machineStatusTitle = "未配对";
    /** 设备状态 **/
    public int                              machineStatus;
    /**监听网络变化广播**/
    private NetworkConnectionChangeReceiver mNetworkChangeReceiver;

    @Override
    public void onCreate(){
        // 1.调用父类方法
        super.onCreate ();
        // 2.初始化一些参数
        init ();
    }

    /**
     * 初始化Application
     */
    private void init(){
        // 1.初始化百度地图的定位client
        mLocationClient = new LocationClient (this.getApplicationContext ());
        mMyLocationListener = new MyLocationListener ();
        mLocationClient.registerLocationListener (mMyLocationListener);

        // 2.启动个推
        GeTuiUtil.initialize (getApplicationContext ());

        // 3.设置是否是调试模式
        String isDebug = getApplicationContext ().getResources ().getString (R.string.isDebug);
        isDebug = StringUtils.trimNull (isDebug, "0");
        MyLog.isDeBug = "1".equals (isDebug);
    }

    /**
     * 启动删除洗车照片信息的服务
     */
    public void startDeleteWashcarPicService(){
        if (!CommonUtils.isServiceRunning (this, DeleteWashcarPicService.class.getCanonicalName ())) {
            Intent intent = new Intent (DeleteWashcarPicService.ACTION);
            startService (intent);
        }
    }

    /**
     * 启动实时定位
     */
    public void startRealtimeLocService(){
        if (!CommonUtils.isServiceRunning (this, LocationService.class.getCanonicalName ())) {
            stopUploadRunmanLoc ();
            Intent intent = new Intent (LocationService.ACTION);
            startService (intent);
        }
    }

    /**
     * 停止上传跑男地理位置
     */
    public void stopUploadRunmanLoc(){
        CommonUtils.saveUploadRunmanLocFlag ("false", this);
    }

    /**
     * 恢复上传跑男地理位置
     */
    public void resumeUploadRunmanLoc(){
        CommonUtils.saveUploadRunmanLocFlag ("true", this);
    }

    /**
     * 开始定位
     * 
     * @param clientOption
     * @param callback
     */
    public void startLocation(LocationClientOption clientOption,BaiMapLocationCallBack callback){
        locationCallBack = callback;
        if (clientOption == null) {
            clientOption = new LocationClientOption ();
            clientOption.setLocationMode (LocationMode.Hight_Accuracy);
            clientOption.setCoorType ("gcj02");
            clientOption.setScanSpan (1000);
            clientOption.setIsNeedAddress (true);
            clientOption.setAddrType ("all");
        }
        mLocationClient.setLocOption (clientOption);
        mLocationClient.start ();
    }

    /**
     * 定位结果回调接口类
     * @author yanshengli
     * @since 2015-1-30
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location){
            if (locationCallBack != null) {
                locationCallBack.onLocationReceived (location, mLocationClient);
            }
            mLocationClient.stop ();
        }
    }

    /**
     * 是否是第一次启动App
     * 
     * @return
     */
    public boolean isFristStart(){
        String firstUseFlag = CommonUtils.getFirstUseFlag (getApplicationContext ());
        if (StringUtils.isEmpty (firstUseFlag)) {
            CommonUtils.saveFirstUseFlag (getApplicationContext ());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo ();
        return networkInfo != null && networkInfo.isConnectedOrConnecting ();
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo(){
        PackageInfo info = null;
        try {
            info = getPackageManager ().getPackageInfo (getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace (System.err);
        }
        if (info == null) info = new PackageInfo ();
        return info;
    }

    public String getMachineStatusTitle(){
        return "设备状态:" + machineStatusTitle;
    }

    public void setMachineStatusTitle(String machineStatusTitle){
        this.machineStatusTitle = machineStatusTitle;
    }

    public int getMachineStatus(){
        return machineStatus;
    }

    public void setMachineStatus(int machineStatus){
        this.machineStatus = machineStatus;
    }

    /**
     * 
    * @Title: startUploadService 
    * @Description: TODO(开启定时上传文件服务,并注册网络状态广播监听,登录成功后调用) 
    * @param     
    * @return void    
    * @throws
     */
    public void startUploadService(){
        if (mNetworkChangeReceiver == null) mNetworkChangeReceiver = new NetworkConnectionChangeReceiver ();
        registerReceiver (mNetworkChangeReceiver, new IntentFilter (ConnectivityManager.CONNECTIVITY_ACTION));
        AlarmManagerUtils.sendScanFilesRepeat (getApplicationContext ());
    }

    /**
     * 
    * @Title: stopUploadService 
    * @Description: TODO(停止定时上传文件服务,取消网络状态广播监听,退出登录时调用) 
    * @param     
    * @return void    
    * @throws
     */
    public void stopUploadService(){
        if (mNetworkChangeReceiver != null) unregisterReceiver (mNetworkChangeReceiver);
        AlarmManagerUtils.cancelScanFiles (getApplicationContext ());
    }
}
