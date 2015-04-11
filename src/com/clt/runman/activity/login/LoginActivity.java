package com.clt.runman.activity.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.clt.runman.R;
import com.clt.runman.activity.index.IndexActivity;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.interfaces.BaiMapLocationCallBack;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.LoginRespBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.utils.AppManager;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DialogUtils;
import com.clt.runman.utils.MD5;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.ValidateUtils;

/**
 *@Description:用户登陆
 *@Author:张聪
 *@Since:2014年12月27日下午12:34:37
 */
public class LoginActivity extends BaseActivity {

    /** 账号框 **/
    private EditText         accoutText;
    /** 密码框 **/
    private EditText         pwdText;
    /** 记住密码的checkbox **/
    private CheckBox         rememberPwdBox;
    /** 拨打客户电话的按钮 **/
    private Button           login_page_call_service_btn;
    /** 登录请求 **/
    private static final int login_http_tag = 100;
    /** 跑男位置的经度 **/
    private double           longitude;
    /** 跑男位置的纬度**/
    private double           latitude;
    /** 再按一次退出程序*/
    private long             exitTime       = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);

        // 2.初始化uiview
        initUIView ();

        // 3.自动回显登录信息
        autoShowLoginAccount ();

        // 4.获取经纬度
        beginLocation ();
    }

    /**
     * 开始百度定位
     */
    private void beginLocation(){
        getAppContext ().startLocation (null, new BaiMapLocationCallBack () {
            @Override
            public void onLocationReceived(BDLocation location,LocationClient client){
                longitude = location.getLongitude ();
                latitude = location.getLatitude ();
            }
        });
    }

    /**
     * 回显之前的登录信息
     */
    private void autoShowLoginAccount(){
        // 获取记住密码的标识
        String rememberPwdFlag = CommonUtils.getRememberPwdFlag (this);
        if (!StringUtils.isEmpty (rememberPwdFlag)) {
            rememberPwdBox.setChecked (true);
            String loginAcct = CommonUtils.getLoginAccount (this);
            String loginPwd = CommonUtils.getLoginPwd (this);
            accoutText.setText (loginAcct);
            pwdText.setText (loginPwd);
        }
    }

    /**
     * 初始化视图
     */
    private void initUIView(){
        // 1.布局
        setContentView (R.layout.login_activity);

        // 2.手机号
        accoutText = (EditText) findViewById (R.id.login_accont);

        // 3.密码
        pwdText = (EditText) findViewById (R.id.login_pwd);

        // 4.记住密码
        rememberPwdBox = (CheckBox) findViewById (R.id.remember_pwd_checkbox);

        // 5.呼叫客服按钮
        login_page_call_service_btn = (Button) findViewById (R.id.login_page_call_service_btn);
        login_page_call_service_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v){
                final String phoneNum = StringUtils.trimNull (login_page_call_service_btn.getText ().toString ());
                if (!StringUtils.isEmpty (phoneNum)) {
                    String callServicePhoneTip = getResources ().getString (R.string.callServicePhoneTip);
                    CommonUtils.callPhone(callServicePhoneTip, phoneNum, LoginActivity.this);
                }

            }
        });
    }

    /**
     *@Description: 用户登录
     *@Author: 张聪
     *@Since: 2015年1月16日下午3:08:17
     *@param view
     */
    public void doLogin(View view){
        String phone = StringUtils.trimNull (accoutText.getText ().toString ());
        String pwd = StringUtils.trimNull (pwdText.getText ().toString ());
        if (StringUtils.isEmpty (phone)) {
            DialogUtils.showToast (this, "亲，您输入的登录账号为空哦");
            return;
        }
        if (!ValidateUtils.isMobile (phone)) {
            DialogUtils.showToast (this, "亲，手机号码不合法哦");
            return;
        }
        if (StringUtils.isEmpty (pwd)) {
            DialogUtils.showToast (this, "亲，您输入的登录密码为空哦");
            return;
        }
        String loginUrl = AppUtils.getContext ().getResources ().getString (R.string.login);
        List<NameValuePair> params = packLoginParams ();
        doPost (loginUrl, params, LoginRespBean.class, login_http_tag);
    }

    /**
     * 组装登录请求参数
     * @return
     */
    private List<NameValuePair> packLoginParams(){
        // 1.账号
        String phone = StringUtils.trimNull (accoutText.getText ().toString ());
        // 2.密码
        String pwd = MD5.md5 (StringUtils.trimNull (pwdText.getText ().toString ()));
        // 3.参数
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        params.add (new BasicNameValuePair ("runManPhone",phone));
        params.add (new BasicNameValuePair ("runManPassword",pwd));
        params.add (new BasicNameValuePair ("longitude",String.valueOf (longitude)));
        params.add (new BasicNameValuePair ("latitude",String.valueOf (latitude)));
        return params;
    }

    /**
     *@Description: 覆盖父类方法
     *@Author: 张聪
     *@Since: 2015年1月19日下午3:05:44
     *@param data
     *@param reqTag
     */
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        if (reqTag == login_http_tag) {
            processLoginSuccCallback (data);
        }
    }

    /**
     * 处理登录成功的的回调
     * @param data
     */
    private void processLoginSuccCallback(BaseRespBean data){

        // 1.判断是否需要保存用户的密码
        boolean rememberPwd = rememberPwdBox.isChecked ();
        if (rememberPwd) {
            String phone = StringUtils.trimNull (accoutText.getText ().toString ());
            String pwd = StringUtils.trimNull (pwdText.getText ().toString ());
            CommonUtils.saveRememberPwdFlag ("true", this);
            CommonUtils.saveLoginAccount (phone, this);
            CommonUtils.saveLoginPwd (pwd, this);
        } else {
            CommonUtils.saveRememberPwdFlag ("", this);
            CommonUtils.saveLoginAccount ("", this);
            CommonUtils.saveLoginPwd ("", this);
        }
        // 2.转型
        LoginRespBean resultBean = (LoginRespBean) data;

        // 3.跑男信息
        RunMan runman = resultBean.getRunMan ();

        // 4.保存token信息
        CommonUtils.saveRunmanUUID (resultBean.getRunMan ().getRunManUuid (), this);

        // 5.保存跑男登录后的信息
        CommonUtils.saveLoginedRunmanInfo (runman, this);

        // 6.页面跳转
        redirect ();
    }

    /**
     *@Description: 登录成功后，页面跳转 
     *@Author: 张聪
     *@Since: 2015年1月19日下午3:05:35
     */
    private void redirect(){
        redirectToTargetActivity (IndexActivity.class, new Bundle ());
        finish ();
    }

    /**
      *@Description: 重写返回物理返回键,退出应用
      *@Author: 张聪
      *@Since: 2015-1-31下午1:55:18
      *@param keyCode
      *@param event
      *@return
      */
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction () == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis () - exitTime) > 2000) {
                DialogUtils.showToast (this, "再按一次退出程序");
                exitTime = System.currentTimeMillis ();
            } else {
                AppManager.getAppManager ().AppExit (this);
            }
            return true;
        }
        return super.onKeyDown (keyCode, event);
    }
}
