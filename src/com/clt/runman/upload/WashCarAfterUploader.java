package com.clt.runman.upload;

import java.io.File;
import java.io.FileNotFoundException;

import net.tsz.afinal.http.AjaxParams;
import android.content.Context;

import com.clt.runman.R;
import com.clt.runman.db.dao.WashcarPicInfoDao;
import com.clt.runman.db.model.WashcarPicInfoDaoModel;
import com.clt.runman.http.HttpRequestClientCallback;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.utils.MyLog;

/**
 * 
* @ClassName: WashCarAfterUploader 
* @Description: TODO(洗车后上传照片) 
* @author Administrator 
* @date 2015-4-9 上午9:24:58 
*
 */
public class WashCarAfterUploader extends Uploader {

    private static final String    TAG = WashCarAfterUploader.class.getSimpleName ();

    // 洗车完成后照片对象
    private WashcarPicInfoDaoModel model;

    protected WashCarAfterUploader(Context context) {
        super (context);
    }

    public WashCarAfterUploader(Context context, WashcarPicInfoDaoModel model) {
        super (context);
        this.model = model;
    }

    @Override
    protected String getUrl(){
        return mContext.getResources ().getString (R.string.uploadAfterwashCar);
    }

    @Override
    protected int getReqTag(){
        return 0;
    }

    @Override
    protected HttpRequestClientCallback getCallback(){
        return new HttpRequestClientCallback () {

            @Override
            public void httpRespSuccess(BaseRespBean result,int reqTag,Class<?> clazz){
                // 文件上传成功,更新数据库状态
                MyLog.d (TAG, "上传成功");
                model.setIsUpload (1);
                WashcarPicInfoDao.newInstance (mContext).updatePicInfo (model);
                System.out.println ("任务执行完成,移除任务");
                UploadManager.getInstance (mContext).remove (model);
            }

            @Override
            public void httpRespFail(String errorcode,String errorMsg,int reqTag){
                MyLog.e (TAG, "图片上传失败,errorCode:" + errorcode + "   errorMsg:" + errorMsg);
                System.out.println ("任务上传失败,移除任务");
                UploadManager.getInstance (mContext).remove (model);
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
        };
    }

    @Override
    protected AjaxParams getParams(){
        AjaxParams params = new AjaxParams ();
        if (model == null) throw new IllegalArgumentException ("上传图片对象不能为空");
        params.put ("orderId", model.getOrderid ());
        params.put ("photoType", model.getUploadPhotoType ());
        try {
            params.put ("photos", new File (model.getPicUrl ()));
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        }
        return params;
    }

}
