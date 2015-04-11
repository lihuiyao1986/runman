package com.clt.runman.upload;

import com.clt.runman.db.model.WashcarPicInfoDaoModel;
import com.clt.runman.service.UploadService;
import com.clt.runman.utils.MyLog;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * 
* @ClassName: UploadTask 
* @Description: TODO(依次从队列中取出数据) 
* @author fuxianwei 
* @date 2015-4-8 上午9:21:18 
*
 */
public class UploadTask extends AsyncTask<Void, Integer, Void> {

    private static final String TAG = UploadTask.class.getSimpleName ();

    private Context             mContext;

    public UploadTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute ();
    }

    @Override
    protected Void doInBackground(Void... params){
        // 初始化任务队列
        UploadManager.getInstance (mContext).initQueues ();
        while (!isCancelled ()) {
            WashcarPicInfoDaoModel model = UploadManager.getInstance (mContext).poll ();
            if (model == null) break;
            MyLog.d (TAG, "正在执行任务:" + model.getPicUrl () + "  任务id:" + model.getOrderid ());
            // 如果追加任务成功,则开始上传文件
            while (!UploadManager.getInstance (mContext).offer (model)) {
                try {
                    Thread.sleep (200);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
            new WashCarAfterUploader (mContext,model).upload ();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute (result);
        // 任务执行完成，关闭上传service
        mContext.stopService (new Intent (mContext.getApplicationContext (),UploadService.class));
    }
}
