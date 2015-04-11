package com.clt.runman.push;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;

import com.clt.runman.R;
import com.clt.runman.activity.index.IndexActivity;
import com.clt.runman.activity.index.OrderReceive2Activity;
import com.clt.runman.activity.login.LoginActivity;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.LoginRespBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.push.model.BaseGexinPayLoadBean;
import com.clt.runman.push.model.PushType;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.SwipeDirect;

/**
 * 推送服务对应的activity--改activity主要作为中转，没有界面
 * 
 * @author yanshengli
 * @since 2015-4-2
 */
public class PushActivity extends BaseActivity {

    /** 自动登录的请求tag **/
    private static final int     auto_login_req_tag = 200;

    /** 推送的对象 **/
    private BaseGexinPayLoadBean payLoadBean;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        Bundle bundle = getIntent ().getExtras ();
        if (bundle != null) {
            payLoadBean = (BaseGexinPayLoadBean) bundle.getSerializable ("data");
            if (payLoadBean != null) {
                String type = StringUtils.trimNull (payLoadBean.getType ());
                if (PushType.ORDER_PUSH_CODE.equals (type)) {
                    doOrderPush (payLoadBean);
                }
            } else {
                finish ();
            }
        } else {
            finish ();
        }

    }

    /**
     * 订单推送
     * 
     * @param payLoadBean
     */
    private void doOrderPush(BaseGexinPayLoadBean payLoadBean){
        RunMan runman = CommonUtils.getLoginedRunManInfo (this);
        if (runman != null) {
            doAutologin ();
        } else {
            redirectToTargetActivity (this, LoginActivity.class, null, SwipeDirect.DIRECT_LEFT);
            finish ();
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
        doPost (url, params, LoginRespBean.class, auto_login_req_tag, false);
    }

    @Override
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        switch (reqTag) {
            case auto_login_req_tag:
                handleAutologinSucc (data);
                break;
            default:
                break;
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

        // 5.跑男状态
        int runmanStatus = runman.getRunManStatus ();
        if (runmanStatus != AppConstant.RUNMAN_STATUS_OFFDUTY) {
            Bundle bundle = new Bundle ();
            bundle.putSerializable ("orderItem", payLoadBean);
            redirectToTargetActivity (this, OrderReceive2Activity.class, bundle, SwipeDirect.DIRECT_LEFT);
        } else {
            redirectToTargetActivity (this, IndexActivity.class, null, SwipeDirect.DIRECT_LEFT);
        }
        finish ();
    }

    @Override
    protected void handleHttpFail(String errorcode,String errorMsg,int reqTag){
        super.handleHttpFail (errorcode, errorMsg, reqTag);
        finish ();
    }

    @Override
    protected boolean httpFailShowAlertView(int reqTag){
        return false;
    }

}
