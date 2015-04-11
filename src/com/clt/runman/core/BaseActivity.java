package com.clt.runman.core;

import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.activity.login.LoginActivity;
import com.clt.runman.activity.orderflow.FoundActivity;
import com.clt.runman.activity.orderflow.OfflinePayConfirmActivity;
import com.clt.runman.activity.orderflow.StartWaskCarActivity;
import com.clt.runman.activity.orderflow.WashCarAfterActivity;
import com.clt.runman.activity.orderflow.WashCarBeforeActivity;
import com.clt.runman.http.HttpRequestClient;
import com.clt.runman.http.HttpRequestClientCallback;
import com.clt.runman.interfaces.AlertDialogueCallBack;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppContext;
import com.clt.runman.utils.AppManager;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DialogUtils;
import com.clt.runman.utils.SwipeDirect;

/**
 * 所有activity的父类
 * @author yanshengli
 * @since 2015-1-30
 */
@SuppressLint("Registered")
public class BaseActivity extends Activity implements HttpRequestClientCallback {

    /** 导航栏左，中，右按钮 **/
    protected Button            navLeftBtn;
    protected Button            navCenterBtn;
    protected Button            navRightBtn;

    /** 会话过期的错误码 **/
    private final static String session_expire_http_code = "2001003";

    /**是否是单例，true:是，false:否**/
    protected boolean           isSingleton              = true;

    /**上下文对象**/
    protected AppContext        appContext               = null;

    /**
     *@Description: activity被创建的时候调用
     *@Author: 张聪
     *@Since: 2015年1月26日下午2:13:45
     *@param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);
        // 2.设置activity
        AppUtils.setActivity (this);
        // 3.禁止横屏
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 4.是否将Activity设为单例，并放入栈中
        creatSingleton ();
        // 5.初始化上下文对象
        appContext = getAppContext ();
    }

    @Override
    protected void onRestart(){
        super.onRestart ();
        AppUtils.setActivity (this);
    }

    /**
     *@Description: 界面获得焦点的时候调用
     *@Author: 张聪
     *@Since: 2015年1月26日下午2:13:33
     */
    @Override
    protected void onResume(){
        super.onResume ();
        AppUtils.setActivity (this);
    }

    @Override
    public void onBackPressed(){
        DialogUtils.closeDialog ();
        super.onBackPressed ();
    }

    @Override
    public void finish(){
        DialogUtils.closeDialog ();
        super.finish ();
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
        doPost (url, null, classz, 0, true,progressTipInfo);
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
        doPost (url, params, classz, 0, true,progressTipInfo);
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
    	 doPost(url, params, classz, reqTag, true, progressTipInfo);
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
        doPost(url, params, classz, reqTag, showProgress, null);
    }
    
    /**
     * doPost请求--重载－8
     * @param url
     * @param params
     * @param classz
     * @param reqTag
     * @param showProgress
     */
    protected void doPost(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,final int reqTag,boolean showProgress,String progressTipInfo){
        if (showProgress) {
            DialogUtils.showProgressDialog (progressTipInfo);
        }
        HttpRequestClient.doPost (getAppContext (), params, url, classz, reqTag, this);
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
    	uploadFile (url, null, classz, 0, true,progressTipInfo);
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
    	uploadFile (url, params, classz, 0, true,progressTipInfo);
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
    	uploadFile (url, params, classz, reqTag, true,progressTipInfo);
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
        uploadFile(url, params, classz, reqTag, showProgress, null);
    }
    
    /**
     * 上传文件请求--重载－8
     * @param url
     * @param params
     * @param classz
     * @param reqTag
     * @param showProgress
     */
    protected void uploadFile(String url,AjaxParams params,final Class<? extends BaseRespBean> classz,final int reqTag,boolean showProgress,String progressTipInfo){
        if (showProgress) 
        {
            DialogUtils.showProgressDialog (progressTipInfo);
        }
        HttpRequestClient.uploadFiles(getAppContext (), params, url, classz, reqTag, this);
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
        doGet (url, null, classz, 0, true,progressTipInfo);
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
        doGet (url, params, classz, 0, true,progressTipInfo);
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
        doGet (url, params, classz, reqTag, true,progressTipInfo);
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
        doGet(url, params, classz, reqTag, showProgress, null);
    }
    
    /**
     * doGet请求--重载－8
     * @param url
     * @param params
     * @param classz
     * @param reqTag
     * @param showProgress
     */
    protected void doGet(String url,List<NameValuePair> params,final Class<? extends BaseRespBean> classz,final int reqTag,boolean showProgress,String progressTipInfo){
        if (showProgress) {
            DialogUtils.showProgressDialog (progressTipInfo);
        }
        HttpRequestClient.doGet (getAppContext (), params, url, classz, reqTag, this);
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
        if (session_expire_http_code.equalsIgnoreCase (errorcode)) {
            // 会话失效
            DialogUtils.showAlertDialog ("会话已失效，是否重新登录", this, new AlertDialogueCallBack () {

                @Override
                public void doCallBack(){
                    redirectToTargetActivity (LoginActivity.class, null);
                }
            });
        } else {
            handleHttpFail (errorcode, errorMsg, reqTag);
        }
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
        
        if (httpFailShowAlertView (reqTag)) {
            DialogUtils.showToast (this, errorMsg + "[" + errorcode + "]");
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
     *@Description: 初始化顶部标题栏 
     *@Author: 张聪
     *@Since: 2015年1月10日下午5:25:17
     *@param lTitle 左标题
     *@param cTitle 中间标题
     *@param rTitle 右标题
     */
    protected void initTopTitle(Object lTitle,Object cTitle,Object rTitle){

        // 1.左标题
        navLeftBtn = (Button) findViewById (R.id.top_title_left);
        if (lTitle instanceof String) {
            navLeftBtn.setText (lTitle.toString ());
        } else if (lTitle instanceof Integer) {
            navLeftBtn.setBackgroundResource ((Integer) lTitle);
        }
        navLeftBtn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                navLeftBtnClicked ();
            }
        });

        // 2.中间标题
        navCenterBtn = (Button) findViewById (R.id.top_title_center);
        if (cTitle instanceof String) {
            navCenterBtn.setText (cTitle.toString ());
        } else if (cTitle instanceof Integer) {
            navCenterBtn.setBackgroundResource ((Integer) cTitle);
        }
        navCenterBtn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                navCenterBtnClicked ();
            }
        });

        // 3.右标题
        Button rightTitle = (Button) findViewById (R.id.top_title_right);
        if (rTitle instanceof String) {
            rightTitle.setText (rTitle.toString ());
        } else if (rTitle instanceof Integer) {
            rightTitle.setBackgroundResource ((Integer) rTitle);
        }
        rightTitle.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                navRightBtnClicked ();
            }
        });

        // 4.设置导航的样式
        configNavBarStyle (navLeftBtn, navCenterBtn, navRightBtn);
    }

    /**
     * 配置导航的样式--子类覆盖
     * @param leftTitle
     * @param centerTitle
     * @param rightTitle
     */
    protected void configNavBarStyle(Button leftTitle,TextView centerTitle,Button rightTitle){

    }

    /**
     * 导航左按钮被点击--待子类覆盖
     */
    protected void navLeftBtnClicked(){

    }

    /**
     * 导航右按钮被点击--待子类覆盖
     */
    protected void navRightBtnClicked(){

    }

    /**
     * 导航中间按钮被点击--待子类覆盖
     */
    protected void navCenterBtnClicked(){

    }

    /**
     * 跳转到指定的activity
     * @param clazz
     * @param bundle
     */
    public void redirectToTargetActivity(Class<? extends Activity> clazz,Bundle bundle){
        Intent intent = new Intent (this,clazz);
        if (bundle == null) {
            bundle = new Bundle ();
        }
        intent.putExtras (bundle);
        this.startActivity (intent);
        CommonUtils.inAnim ();
    }

    /**
     *@Description: 重载页面跳转方法 
     *@Author: 张聪
     *@Since: 2015-1-30下午2:16:16
     *@param context
     *@param clazz
     *@param bundle
     *@param isUp
     */
    public void redirectToTargetActivity(Context context,Class<? extends Activity> clazz,Bundle bundle,boolean isUp){
        Intent intent = new Intent ();
        intent.setClass (context, clazz);
        if (bundle == null) {
            bundle = new Bundle ();
        }
        intent.putExtras (bundle);
        this.startActivity (intent);
        if (isUp) {// 上进
            CommonUtils.upInTranslateAnim ();
        } else {// 下进
            CommonUtils.bottomInTranslateAnim ();
        }
    }

    /**
     * 跳转到指定的activity
     * @param context
     * @param clazz
     * @param bundle
     * @param direct
     */
    public void redirectToTargetActivity(Context context,Class<? extends Activity> clazz,Bundle bundle,SwipeDirect direct){
        Intent intent = new Intent ();
        intent.setClass (context, clazz);
        if (bundle == null) {
            bundle = new Bundle ();
        }
        intent.putExtras (bundle);
        this.startActivity (intent);
        switch (direct) {
            case DIRECT_UP:
                CommonUtils.bottomInTranslateAnim ();
                break;
            case DIRECT_DOWN:
                CommonUtils.upInTranslateAnim ();
                break;
            case DIRECT_LEFT:
                CommonUtils.inAnim ();
                break;
            case DIRECT_RIGHT:
                CommonUtils.outAnim ();
                break;
            default:
                break;
        }
    }

    /**
     *@Description: 禁止返回键
     *@Author: 张聪
     *@Since: 2015年1月27日下午5:31:42
     *@param keyCode
     *@param event
     *@return
     */
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) { return true; }
        return super.onKeyDown (keyCode, event);
    }

    /**
     * 获取应用程序的上下文对象
     * @return
     */
    protected AppContext getAppContext(){
        return (AppContext) getApplicationContext ();
    }

    /**
     * 获取日志的tag
     * @return
     */
    protected String getLogTag(){
        return this.getClass ().getName ();
    }

    /**
     *@Description: Activity是否为单例
     *@Author: 张聪
     *@Since: 2015-2-6上午9:48:49
     */
    protected void creatSingleton(){
        if (isSingleton) {
            AppManager.getAppManager ().addActivity (this);
        }
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
            case AppConstant.ORDER_ARRIVE:// 已接单
                redirectToTargetActivity (FoundActivity.class, null);
                finish ();
                break;
            case AppConstant.ORDER_FOUND:// 已找到
                redirectToTargetActivity (WashCarBeforeActivity.class, null);
                finish ();
                break;
            case AppConstant.ORDER_WASH_BEFORE:// 洗车前拍照
                redirectToTargetActivity (StartWaskCarActivity.class, null);
                finish ();
                break;
            case AppConstant.ORDER_WASH_CAR:// 开始洗车
                redirectToTargetActivity (StartWaskCarActivity.class, null);
                finish ();
                break;
            case AppConstant.ORDER_FINISH_WORK:// 洗车完成
                redirectToTargetActivity (WashCarAfterActivity.class, null);
                finish ();
                break;
            case AppConstant.ORDER_WASH_AFTER:// 洗车后拍照完成
                if (Integer.parseInt (payType) != AppConstant.PAYTYPE_WECHAT_PAYMENT) {
                    redirectToTargetActivity (OfflinePayConfirmActivity.class, null);
                    finish ();
                }
                break;
            default:
                break;
        }
    }

}
