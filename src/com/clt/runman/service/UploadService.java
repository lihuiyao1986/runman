package com.clt.runman.service;

import android.app.IntentService;
import android.content.Intent;

import com.clt.runman.upload.UploadManager;

/**
 * 
* @ClassName: UploadService 
* @Description: TODO(自动上传图片服务) 
* @author fuxianwei 
* @date 2015-4-8 上午10:01:44 
*
 */
public class UploadService extends IntentService {

    public static final String TYPE = "TYPE";

    public UploadService() {
        this ("UploadService");
    }

    public UploadService(String name) {
        super (name);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        final int type = intent.getIntExtra (TYPE, -1);
        switch (type) {
            case UploadServiceType.TYPES_START:
                UploadManager.getInstance (getApplicationContext ()).startUpload ();
                break;
            case UploadServiceType.TYPES_STOP:
                UploadManager.getInstance (getApplicationContext ()).stopUpload ();
                break;

            default:
                stopSelf ();
                break;
        }
    }

    public static class UploadServiceType {

        public static final int TYPES_START = 0;
        public static final int TYPES_STOP  = 1;
    }
}
