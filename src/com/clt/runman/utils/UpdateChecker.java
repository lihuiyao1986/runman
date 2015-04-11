package com.clt.runman.utils;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.WindowManager;

import com.clt.runman.R;
import com.clt.runman.db.dao.VersionDao;
import com.clt.runman.http.HttpRequestClient;
import com.clt.runman.http.HttpRequestClientCallback;
import com.clt.runman.interfaces.AlertDialogueCallBack;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.VersionInfoRespBean;
import com.clt.runman.model.VersionInfomation;

/**
 * 
* @ClassName: UpdateChecker 
* @Description: TODO(版本更新检测工具) 
* @author fuxianwei 
* @date 2015-4-10 上午9:52:50 
*
 */
public class UpdateChecker {

    private static UpdateChecker INSTANCE;
    private Context              mContext;
    private String               mUrl;
    private long                 downloaderId = -1;
    private DownloadReceiver     mReceiver;
    private boolean              isRegistered = false;

    public static UpdateChecker getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (UpdateChecker.class) {
                if (INSTANCE == null) INSTANCE = new UpdateChecker (context);
            }
        }
        return INSTANCE;
    }

    private UpdateChecker() {

    }

    private UpdateChecker(Context context) {
        this.mContext = context.getApplicationContext ();
        this.mUrl = context.getResources ().getString (R.string.getSoftVersion);
        this.mReceiver = new DownloadReceiver ();
    }

    /**
     * 
    * @Title: checkForUpdate 
    * @Description: TODO(检查更新) 
    * @param     
    * @return void    
    * @throws
     */
    public void checkForUpdate(){
        checkForUpdate (true);
    }

    public void checkForUpdate(final boolean showTips){
        HttpRequestClient.doPost (mContext, null, mUrl, VersionInfoRespBean.class, 0, new HttpRequestClientCallback () {

            @Override
            public void httpRespSuccess(BaseRespBean result,int reqTag,Class<?> clazz){
                VersionInfomation version = ((VersionInfoRespBean) result).getSoftVersion ();
                VersionDao.newInstance (mContext).saveVersion (version);
                check (version, showTips);
            }

            @Override
            public void httpRespFail(String errorcode,String errorMsg,int reqTag){

            }

            @Override
            public void httpReqBegin(int reqTag){

            }

            @Override
            public void httpOnloading(long count,long current,int reqTag){

            }

            @Override
            public void httpOnProgress(boolean progress,int rate,int reqTag){

            }
        });
    }

    public void check(VersionInfomation version,boolean showTips){
        try {
            // 获取当前版本号
            int versionCode = mContext.getPackageManager ().getPackageInfo (mContext.getPackageName (), PackageManager.GET_META_DATA).versionCode;
            int newVersionCode = version.getVersionCode ();
            // 根据版本号判断,是否是最新版本
            if (newVersionCode > versionCode) {
                showUpdateDialog (version);
            } else {
                if (showTips) {
                    DialogUtils.showToast (mContext, "当前已是最新版本");
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace ();
        }
    }

    /**
     * 
    * @Title: showUpdateDialog 
    * @Description: TODO(显示版本更新对话框) 
    * @param     
    * @return void    
    * @throws
     */
    private void showUpdateDialog(final VersionInfomation version){

        DialogUtils.showAlertDialog (mContext.getResources ().getString (R.string.version_found_tips), mContext, "取消", "更新",
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, new AlertDialogueCallBack () {

                    @Override
                    public void doCallBack(){
                        downloadApk (Uri.parse (version.getVersionUrl ()));
                    }
                });
    }

    /**
     * 
    * @Title: downloadApk 
    * @Description: TODO(启动一个下载任务) 
    * @param @param uri    
    * @return void    
    * @throws
     */
    private void downloadApk(Uri uri){
        // 开始下载，注册广播监听
        callOnResume ();
        DownloadManager downloader = (DownloadManager) mContext.getSystemService (Context.DOWNLOAD_SERVICE);
        Query query = new Query ();
        query.setFilterById (downloaderId);
        Cursor cur = downloader.query (query);
        // 下载任务已经存在的话
        if (cur != null && cur.moveToNext ()) return;
        try {
            DownloadManager.Request task = new DownloadManager.Request (uri);
            task.setTitle (mContext.getResources ().getString (R.string.app_name));
            task.setNotificationVisibility (DownloadManager.Request.VISIBILITY_VISIBLE);
            task.setVisibleInDownloadsUi (true);
            task.setAllowedNetworkTypes (DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            task.setDestinationInExternalPublicDir (Environment.DIRECTORY_DOWNLOADS, "runMan.apk");
            downloaderId = downloader.enqueue (task);
        } catch (Exception e) {
            e.printStackTrace ();
            DialogUtils.showToast (mContext, "下载地址异常");
            callOnPause ();
        }
    }

    /**
     * 注册广播监听
     */
    public void callOnResume(){
        if (isRegistered) return;
        isRegistered = true;
        this.mContext.registerReceiver (mReceiver, new IntentFilter (DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 取消广播监听
     */
    public void callOnPause(){
        if (isRegistered) {
            isRegistered = false;
            this.mContext.unregisterReceiver (mReceiver);
        }
    }

    /**
     * 
    * @ClassName: DownloadReceiver 
    * @Description: TODO(广播,监听下载状态) 
    * @author fuxianwei 
    * @date 2015-4-9 下午3:55:34 
    *
     */
    final class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context,Intent intent){
            DownloadManager downloadManager = (DownloadManager) mContext.getSystemService (Context.DOWNLOAD_SERVICE);
            long completeId = intent.getLongExtra (DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            // 监听到版本更新任务下载完成后，跳转到版本安装
            if (completeId == downloaderId) {
                Query query = new Query ();
                query.setFilterById (downloaderId);
                Cursor cur = downloadManager.query (query);
                if (cur.moveToFirst ()) {
                    int columnIndex = cur.getColumnIndex (DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == cur.getInt (columnIndex)) {
                        String uriString = cur.getString (cur.getColumnIndex (DownloadManager.COLUMN_LOCAL_URI));
                        File apkFile = new File (Uri.parse (uriString).getPath ());
                        Intent installIntent = new Intent ();
                        installIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                        installIntent.setAction (android.content.Intent.ACTION_VIEW);
                        installIntent.setDataAndType (Uri.fromFile (apkFile), "application/vnd.android.package-archive");
                        context.startActivity (installIntent);
                        // 下载完成后取消广播注册
                        callOnPause ();
                    }
                }
                cur.close ();
            }
        }
    }
}
