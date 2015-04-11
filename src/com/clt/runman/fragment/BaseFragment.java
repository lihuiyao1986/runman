package com.clt.runman.fragment;

import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.clt.runman.activity.orderflow.FoundActivity;
import com.clt.runman.activity.orderflow.OfflinePayConfirmActivity;
import com.clt.runman.activity.orderflow.StartWaskCarActivity;
import com.clt.runman.activity.orderflow.WashCarAfterActivity;
import com.clt.runman.activity.orderflow.WashCarBeforeActivity;
import com.clt.runman.http.HttpRequestClient;
import com.clt.runman.http.HttpRequestClientCallback;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppContext;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DialogUtils;

/**
 * Created by yanshengli on 15-1-23.
 */
@SuppressLint("NewApi")
public class BaseFragment extends Fragment implements HttpRequestClientCallback {

    public AppContext getRunManApplication(){
        return (AppContext) getActivity ().getApplication ();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume ();
    }

    @Override
    public void onPause(){
        super.onPause ();
    }

    @Override
    public void onDestroy(){
        super.onDestroy ();
    }

    /**
     * doPost请求--重载－1
     * @param url
     * @param classz
     */
    protected void doPost(String url,final Class<? extends BaseRespBean> classz){
        doPost (url, null, classz, 0, true);
    }

    /**
     * doPost请求--重载－2
     * @param url
     * @param classz
     */
    protected void doPost(String url,final Class<? extends BaseRespBean> classz,String progressTipInfo){
        doPost (url, null, classz, 0, true, progressTipInfo);
    }

    /**
     * doPost请求--重载－3
     * @param url
     * @param classz
     */
    protected void doPost(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz){
        doPost (url, params, classz, 0, true);
    }

    /**
     * doPost请求--重载－4
     * @param url
     * @param classz
     */
    protected void doPost(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,String progressTipInfo){
        doPost (url, params, classz, 0, true, progressTipInfo);
    }

    /**
     * doPost请求--重载－5
     * @param url
     * @param classz
     */
    protected void doPost(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,final int reqTag){
        doPost (url, params, classz, reqTag, true);
    }

    /**
     * doPost请求--重载－6
     * @param url
     * @param classz
     */
    protected void doPost(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,final int reqTag,String progressTipInfo){
        doPost (url, params, classz, reqTag, true, progressTipInfo);
    }

    /**
     * doPost请求--重载－7
     * @param url
     * @param params
     * @param classz
     * @param reqTag
     * @param showProgress
     */
    protected void doPost(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,final int reqTag,boolean showProgress){
        doPost (url, params, classz, reqTag, showProgress, null);
    }

    /**
     * doPost请求--重载－8
     * @param url
     * @param params
     * @param classz
     * @param reqTag
     * @param showProgress
     */
    protected void doPost(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,final int reqTag,boolean showProgress,
            String progressTipInfo){
        if (showProgress) {
            DialogUtils.showProgressDialog (progressTipInfo);
        }
        HttpRequestClient.doPost (getRunManApplication (), params, url, classz, reqTag, this);
    }

    /**
     * 上传文件请求--重载－1
     * @param url
     * @param classz
     */
    protected void uploadFile(String url,final Class<? extends BaseRespBean> classz){
        uploadFile (url, null, classz, 0, true);
    }

    /**
     * 上传文件请求--重载－2
     * @param url
     * @param classz
     */
    protected void uploadFile(String url,final Class<? extends BaseRespBean> classz,String progressTipInfo){
        uploadFile (url, null, classz, 0, true, progressTipInfo);
    }

    /**
     * 上传文件请求--重载－3
     * @param url
     * @param classz
     */
    protected void uploadFile(String url,AjaxParams params,final Class<? extends BaseRespBean> classz){
        uploadFile (url, params, classz, 0, true);
    }

    /**
     * 上传文件请求--重载－4
     * @param url
     * @param classz
     */
    protected void uploadFile(String url,AjaxParams params,final Class<? extends BaseRespBean> classz,String progressTipInfo){
        uploadFile (url, params, classz, 0, true, progressTipInfo);
    }

    /**
     * 上传文件请求--重载－5
     * @param url
     * @param classz
     */
    protected void uploadFile(String url,AjaxParams params,final Class<? extends BaseRespBean> classz,final int reqTag){
        uploadFile (url, params, classz, reqTag, true);
    }

    /**
     * 上传文件请求--重载－6
     * @param url
     * @param classz
     */
    protected void uploadFile(String url,AjaxParams params,final Class<? extends BaseRespBean> classz,final int reqTag,String progressTipInfo){
        uploadFile (url, params, classz, reqTag, true, progressTipInfo);
    }

    /**
     * 上传文件请求--重载－7
     * @param url
     * @param params
     * @param classz
     * @param reqTag
     * @param showProgress
     */
    protected void uploadFile(String url,AjaxParams params,final Class<? extends BaseRespBean> classz,final int reqTag,boolean showProgress){
        uploadFile (url, params, classz, reqTag, showProgress, null);
    }

    /**
     * 上传文件请求--重载－8
     * @param url
     * @param params
     * @param classz
     * @param reqTag
     * @param showProgress
     */
    protected void uploadFile(String url,AjaxParams params,final Class<? extends BaseRespBean> classz,final int reqTag,boolean showProgress,
            String progressTipInfo){
        if (showProgress) {
            DialogUtils.showProgressDialog (progressTipInfo);
        }
        HttpRequestClient.uploadFiles (getRunManApplication (), params, url, classz, reqTag, this);
    }

    /**
     * doGet请求--重载－1
     * @param url
     * @param classz
     */
    protected void doGet(String url,final Class<? extends BaseRespBean> classz){
        doGet (url, null, classz, 0, true);
    }

    /**
     * doGet请求--重载－2
     * @param url
     * @param classz
     */
    protected void doGet(String url,final Class<? extends BaseRespBean> classz,String progressTipInfo){
        doGet (url, null, classz, 0, true, progressTipInfo);
    }

    /**
     * doGet请求--重载－3
     * @param url
     * @param classz
     */
    protected void doGet(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz){
        doGet (url, params, classz, 0, true);
    }

    /**
     * doGet请求--重载－4
     * @param url
     * @param classz
     */
    protected void doGet(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,String progressTipInfo){
        doGet (url, params, classz, 0, true, progressTipInfo);
    }

    /**
     * doGet请求--重载－5
     * @param url
     * @param classz
     */
    protected void doGet(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,final int reqTag){
        doGet (url, params, classz, reqTag, true);
    }

    /**
     * doGet请求--重载－6
     * @param url
     * @param classz
     */
    protected void doGet(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,final int reqTag,String progressTipInfo){
        doGet (url, params, classz, reqTag, true, progressTipInfo);
    }

    /**
     * doGet请求--重载－7
     * @param url
     * @param params
     * @param classz
     * @param reqTag
     * @param showProgress
     */
    protected void doGet(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,final int reqTag,boolean showProgress){
        doGet (url, params, classz, reqTag, showProgress, null);
    }

    /**
     * doGet请求--重载－8
     * @param url
     * @param params
     * @param classz
     * @param reqTag
     * @param showProgress
     */
    protected void doGet(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,final int reqTag,boolean showProgress,
            String progressTipInfo){
        if (showProgress) {
            DialogUtils.showProgressDialog (progressTipInfo);
        }
        HttpRequestClient.doGet (getRunManApplication (), params, url, classz, reqTag, this);
    }

    /**
     * 请求成功回调
     * @param result
     * @param reqTag
     * @param clazz
     */
    public void httpRespSuccess(BaseRespBean result,int reqTag,Class<?> clazz){
        DialogUtils.closeDialog ();
        handleHttpSuccess (result, reqTag);
    }

    /**
     * 请求失败
     * @param errorcode
     * @param errorMsg
     * @param reqTag
     */
    public void httpRespFail(String errorcode,String errorMsg,int reqTag){
        DialogUtils.closeDialog ();
        handleHttpFail (errorcode, errorMsg, reqTag);
    }

    /**
     * http请求开始
     */
    public void httpReqBegin(int reqTag){

    }

    /**
     * 加载进度
     * @param progress
     * @param rate
     * @param reqTag
     */
    public void httpOnProgress(boolean progress,int rate,int reqTag){

    }

    /***
     * http正在加载
     * @param count
     * @param current
     */
    public void httpOnloading(long count,long current,int reqTag){

    }

    // 等待子类覆盖
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){}

    /**
     * 是否请求正常，但返回码不正常的情况
     * @param errorcode
     * @param errorMsg
     * @param reqTag
     */
    protected void handleHttpFail(String errorcode,String errorMsg,int reqTag){
        
        //转义错误描述
        errorMsg = convertErrorMsg(errorcode,errorMsg,reqTag);
        
        //显示错误提示
        if (httpFailShowAlertView (reqTag)) 
        {
            DialogUtils.showToast (getActivity (), errorMsg + "[" + errorcode + "]");
        }
    }

    /**
     *@Description: 转义错误描述
     *@Author: 李焱生
     *@Since: 2015年4月3日上午10:09:55
     *@param errorcode
     *@param errorMsg
     *@param reqTag
     *@return
     */
    protected String convertErrorMsg(String errorcode,String errorMsg,int reqTag)
    {
        return errorMsg;
    }

    /**
     * 请求错误之后，是否显示alertView
     * @param reqTag
     * @return
     */
    protected boolean httpFailShowAlertView(int reqTag){
        return true;
    }

    /**
     *@Description: Fragment跳转到Activity 
     *@Author: 张聪
     *@Since: 2015年3月26日下午9:23:16
     *@param clazz
     *@param bundle
     */
    public void redirectToTargetActivity(Class<?> clazz,Bundle bundle){
        Intent intent = new Intent ();
        if (bundle == null) {
            bundle = new Bundle ();
        }
        intent.putExtras (bundle);
        intent.setClass (getActivity (), clazz);
        getActivity ().startActivity (intent);
        CommonUtils.inAnim ();
    }
    
    /**
     *@Description: 根据订单状态和支付类型跳转页面 
     *@Author: 李焱生
     *@Since: 2015年4月2日下午3:34:25
     *@param orderStatus
     *@param payType
     */
    protected void redirectPageByOrderStatusAndPaytype(int orderStatus,String payType)
    {
        switch (orderStatus) {
            case AppConstant.ORDER_ARRIVE:
                redirectToTargetActivity (FoundActivity.class, null);
                getActivity ().finish();
                break;
            case AppConstant.ORDER_FOUND:// 已找到
                redirectToTargetActivity (WashCarBeforeActivity.class, null);
                getActivity ().finish();
                break;
            case AppConstant.ORDER_WASH_BEFORE:// 洗车前拍照
                redirectToTargetActivity (StartWaskCarActivity.class, null);
                getActivity ().finish();
                break;
            case AppConstant.ORDER_WASH_CAR:// 开始洗车
                redirectToTargetActivity (StartWaskCarActivity.class, null);
                getActivity ().finish();
                break;
            case AppConstant.ORDER_FINISH_WORK:// 洗车完成
                redirectToTargetActivity (WashCarAfterActivity.class, null);
                getActivity ().finish();
                break;
            case AppConstant.ORDER_WASH_AFTER:// 洗车后拍照完成
                if (Integer.parseInt (payType) != AppConstant.PAYTYPE_WECHAT_PAYMENT) {
                    redirectToTargetActivity (OfflinePayConfirmActivity.class, null);
                    getActivity ().finish();
                }
                break;
            default:
                break;
        }
    }

}
