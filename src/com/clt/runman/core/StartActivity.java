package com.clt.runman.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;

import com.clt.runman.R;
import com.clt.runman.activity.index.IndexActivity;
import com.clt.runman.activity.login.LoginActivity;
import com.clt.runman.activity.welcome.WelcomeActivity;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.LoginRespBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.UpdateChecker;

/**
 *@Description:应用程序启动
 *@Author:李彦生
 *@Since:2015年1月21日上午9:42:43
 */
public class StartActivity extends BaseActivity {

    private static final int auto_login_req_tag = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父亲的方法
        super.onCreate (savedInstanceState);
        // 2.启动删除洗车照片信息的服务
        getAppContext ().startDeleteWashcarPicService ();
        // 3.启动实时定位的功能
        getAppContext ().startRealtimeLocService ();
        // 4.检查版本更新
        checkVersion ();
        // 5.第一次登录跳转到登录页面,否则跳转到登录页面
        if (getAppContext ().isFristStart ()) {
            goWelcomePage ();// 跳转到欢迎页
        } else {
            RunMan runman = CommonUtils.getLoginedRunManInfo (this);
            if (runman != null) {
                doAutologin ();// 自动登录
            } else {
                goLoginPage ();// 跳转到登录页
            }
        }
    }

    /**
     * 自动登录
     */
    private void doAutologin(){
        RunMan runman = CommonUtils.getLoginedRunManInfo (this);
        String runManUuid = runman.getRunManUuid ();
        String runManEncrypt = runman.getRunManEncrypt ();
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        params.add (new BasicNameValuePair ("runManUuid",StringUtils.trimNull (runManUuid)));
        params.add (new BasicNameValuePair ("runManEncrypt",StringUtils.trimNull (runManEncrypt)));
        String url = StringUtils.trimNull (getResources ().getString (R.string.autologinUrl));
        doPost (url, params, LoginRespBean.class, auto_login_req_tag);
    }

    /**
     * 请求成功
     */
    @Override
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        if (reqTag == auto_login_req_tag) {
            handleAutologinSucc (data);
        }
    }

    /**
     * 处理自动登录成功
     */
    private void handleAutologinSucc(BaseRespBean data){
        // 1.转型
        LoginRespBean resultBean = (LoginRespBean) data;

        // 2.跑男信息
        RunMan runman = resultBean.getRunMan ();

        // 3.保存token信息
        CommonUtils.saveRunmanUUID (resultBean.getRunMan ().getRunManUuid (), this);

        // 4.保存跑男登录后的信息
        CommonUtils.saveLoginedRunmanInfo (runman, this);

        // 5.页面跳转--跳转到首页
        redirectToIndex ();
    }

    /**
     * 页面跳转--跳转到首页
     */
    private void redirectToIndex(){
        redirectToTargetActivity (IndexActivity.class, null);
        finish ();
    }

    /**
     * 请求失败
     */
    @Override
    protected void handleHttpFail(String errorcode,String errorMsg,int reqTag){
        super.handleHttpFail (errorcode, errorMsg, reqTag);
        if (reqTag == auto_login_req_tag) {
            handleAutologinFail ();
        }
    }

    /**
     * 处理自动登录失败--直接跳转到登录页面
     */
    private void handleAutologinFail(){
        goLoginPage ();
    }

    /**
     * 自动登录失败不展示错误信息
     */
    @Override
    protected boolean httpFailShowAlertView(int reqTag){
        if (reqTag == auto_login_req_tag) { return false; }
        return super.httpFailShowAlertView (reqTag);
    }

    /**
     * 跳转到登录页面
     */
    private void goLoginPage(){
        redirectToTargetActivity (LoginActivity.class, null);
        finish ();
    }

    /**
     * 跳转到欢迎页面
     */
    private void goWelcomePage(){
        redirectToTargetActivity (WelcomeActivity.class, null);
        finish ();
    }

    /*
     * 检查版本更新
     */
    private void checkVersion(){
        UpdateChecker.getInstance (getAppContext ()).checkForUpdate (false);
    }
}
