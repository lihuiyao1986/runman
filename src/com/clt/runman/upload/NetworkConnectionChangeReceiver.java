package com.clt.runman.upload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.clt.runman.service.UploadService;
import com.clt.runman.service.UploadService.UploadServiceType;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.MyLog;

/**
 * 
* @ClassName: NetworkConnectionChangeReceiver 
* @Description: TODO(网络状态改变广播) 
* @author fuxianwei 
* @date 2015-4-10 下午1:59:18 
*
 */
public class NetworkConnectionChangeReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkConnectionChangeReceiver.class.getSimpleName ();

    @Override
    public void onReceive(Context context,Intent intent){
        final String action = intent.getAction ();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals (action)) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo ();
            if (null != networkInfo && networkInfo.isConnected ()) {
                String flag = CommonUtils.getUploadOnlyByWifiFlag (context);
                boolean onlyByWifi = !TextUtils.isEmpty (flag) && flag.equals ("true");
                // 如果配置了仅wifi上传,当前不是wifi模式,return
                if (onlyByWifi && ConnectivityManager.TYPE_WIFI != networkInfo.getType ()) {
                    MyLog.d (TAG, "当前不是wifi网络,仅在wifi网络下上传");
                    return;
                }
                MyLog.d (TAG, "网络已连接,开启上传服务");
                final Intent service = new Intent (context,UploadService.class);
                service.putExtra (UploadService.TYPE, UploadServiceType.TYPES_START);
                context.startService (service);
            } else {
                MyLog.d (TAG, "网络已断开,停止上传服务");
                final Intent service = new Intent (context,UploadService.class);
                service.putExtra (UploadService.TYPE, UploadServiceType.TYPES_STOP);
                context.stopService (service);
            }
        }
    }
}
