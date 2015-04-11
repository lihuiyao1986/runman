package com.clt.runman.upload;

import android.content.Context;

import com.clt.runman.http.HttpRequestClient;
import com.clt.runman.http.HttpRequestClientCallback;
import com.clt.runman.model.BaseRespBean;

import net.tsz.afinal.http.AjaxParams;

/**
 * 
* @ClassName: Uploader 
* @Description: TODO(文件上传基类) 
* @author fuxianwei 
* @date 2015-4-9 上午9:19:56 
*
 */
public abstract class Uploader {

    // 上下文对象
    protected Context mContext;

    protected Uploader(Context context) {
        this.mContext = context;
    }

    /**
     * 得到上传url
     */
    protected abstract String getUrl();

    /**
     * 得到上传requestCode
     */
    protected abstract int getReqTag();

    /**
     * 组装参数
     */
    protected abstract AjaxParams getParams();

    /**
     * 上传文件回调事件
     */
    protected HttpRequestClientCallback getCallback(){
        return new HttpRequestClientCallback () {

            @Override
            public void httpRespSuccess(BaseRespBean result,int reqTag,Class<?> clazz){

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
        };
    }

    /**
     * 
    * @Title: upload 
    * @Description: TODO(上传文件) 
    * @param @param params    
    * @return void    
    * @throws
     */
    protected void upload(){
        HttpRequestClient.uploadFiles (mContext, getParams (), getUrl (), BaseRespBean.class, getReqTag (), getCallback ());
    }
}
