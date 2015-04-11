package com.clt.runman.activity.index;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.activity.login.LoginActivity;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.db.dao.VersionDao;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.VersionInfomation;
import com.clt.runman.utils.AppManager;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.UpdateChecker;

/**
 * 
* @ClassName: MoreActivity 
* @Description: TODO(更多activity) 
* @author fuxianwei 
* @date 2015-4-8 下午6:21:14 
*
 */
public class MoreActivity extends BaseActivity implements OnClickListener {

    private static final int  logout_req_tag = 100;

    // 是否是最新版本
    private TextView          more_check_version_isnew_tv;
    // 版本号
    private TextView          more_check_version_code_tv;
    // 有新版本提示
    private ImageView         more_check_version_found_iv;

    private VersionInfomation mVersionInfomation;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate (savedInstanceState);

        initUIView ();
        initListener ();
        initData ();
    }

    /**
     * 初始化视图
     */
    private void initUIView(){
        setContentView (R.layout.more_activity);
        more_check_version_isnew_tv = (TextView) findViewById (R.id.more_check_version_isnew_tv);
        more_check_version_code_tv = (TextView) findViewById (R.id.more_check_version_code_tv);
        more_check_version_found_iv = (ImageView) findViewById (R.id.more_check_version_found_iv);
    }

    /**
     * 初始化事件监听
     */
    private void initListener(){
        findViewById (R.id.more_nav_bar_left_btn).setOnClickListener (this);
        findViewById (R.id.more_exit_tv).setOnClickListener (this);
        findViewById (R.id.more_check_version_ll).setOnClickListener (this);
        findViewById (R.id.more_service_phone_tv).setOnClickListener (this);
    }

    /*
     * 初始化数据
     */
    private void initData(){
        mVersionInfomation = VersionDao.newInstance (getApplicationContext ()).getVersion ();
        // 检查版本信息
        if (mVersionInfomation != null) {
            checkVersionCode ();
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId ()) {
            case R.id.more_check_version_ll:
                checkVersionUpdate ();
                break;
            case R.id.more_exit_tv:
                logout ();
                break;
            case R.id.more_nav_bar_left_btn:
                finish ();
                CommonUtils.outAnim ();
                break;
            case R.id.more_service_phone_tv:
                callServicePhone ();
                break;

            default:
                break;
        }
    }

    /*
     * 检查版本号
     */
    private void checkVersionCode(){
        // 对比版本信息
        try {
            int versionCode = getPackageManager ().getPackageInfo (getPackageName (), PackageManager.GET_META_DATA).versionCode;
            if (versionCode >= mVersionInfomation.getVersionCode ()) {
                isLastestVersion ();
            } else {
                foundNewVersion ();
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace ();
        }
    }

    /*
     * 检查版本更新
     */
    private void checkVersionUpdate(){
        // 判断是否保存了最新版本信息,如果没保存,重新获取
        if (mVersionInfomation == null) {
            UpdateChecker.getInstance (getApplicationContext ()).checkForUpdate ();
        } else {
            UpdateChecker.getInstance (getApplicationContext ()).check (mVersionInfomation, true);
        }
    }

    /*
     * 已是最新版本
     */
    private void isLastestVersion(){
        String versionName = "";
        try {
            versionName = getPackageManager ().getPackageInfo (getPackageName (), PackageManager.GET_META_DATA).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace ();
            versionName = "";
        }
        more_check_version_code_tv.setVisibility (View.VISIBLE);
        more_check_version_isnew_tv.setVisibility (View.VISIBLE);
        more_check_version_code_tv.setText (versionName);
    }

    /*
     * 发现新版本
     */
    private void foundNewVersion(){
        more_check_version_code_tv.setVisibility (View.GONE);
        more_check_version_isnew_tv.setVisibility (View.GONE);
        more_check_version_found_iv.setVisibility (View.VISIBLE);
    }

    /*
     * 拨打服务电话
     */
    private void callServicePhone(){
        String callServicePhoneTip = getResources ().getString (R.string.callServicePhoneTip);
        CommonUtils.callPhone (callServicePhoneTip, "", MoreActivity.this);
    }

    /**
     * 退出当前帐号
     */
    private void logout(){
        String logoutUrl = AppUtils.getActivity ().getResources ().getString (R.string.logoutUrl);
        doPost (logoutUrl, null, BaseRespBean.class, logout_req_tag);
    }

    @Override
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        if (reqTag == logout_req_tag) {
            AppManager.getAppManager ().finishAllActivity ();
            // 退出成功后,停止上传服务
            appContext.stopUploadService ();
            Intent intent = new Intent (this,LoginActivity.class);
            intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity (intent);
            CommonUtils.inAnim ();
        }
    }

}
