package com.clt.runman.activity.orderflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clt.runman.R;
import com.clt.runman.activity.index.OrderReceiveActivity;
import com.clt.runman.core.WashCarFlowActivity;
import com.clt.runman.db.dao.OrderDao;
import com.clt.runman.interfaces.AlertDialogueCallBack;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.ExperienceListQueryRespBean;
import com.clt.runman.model.Voucher;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DialogUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.widget.toast.CustomToastDialog;

/**
 * 线下支付确认页面
 * @author yanshengli
 * @since 2015-3-5
 */
public class OfflinePayConfirmActivity extends WashCarFlowActivity {

    /** 现金支付按钮 **/
    private Button           cash_pay_btn;

    /** 体验券支付按钮 **/
    private Button           voucher_pay_btn;

    /** 使用体验券按钮 **/
    private Button           use_voucher_btn;

    /** 线下支付确认按钮 **/
    private Button           cash_pay_commit_btn;

    /** 现金支付表单区域 **/
    private LinearLayout     cash_pay_form_zone_ll;

    /** 体验券支付表单区域 **/
    private LinearLayout     voucher_form_zone_ll;

    /** 当前的支付方式 **/
    private int              currentPayway;

    /** 现金支付 **/
    private final static int CASH_PAY_WAY                     = 0;

    /** 体验券支付 **/
    private final static int VOUCHER_PAY_WAY                  = 1;

    /** 线下支付确认请求tag **/
    private final static int offline_pay_confirm_http_req_tag = 100;

    /** 体验券查询请求tag **/
    private final static int voucher_query_req_tag            = 200;

    /** 体验券编号 **/
    private Voucher          voucher;

    /** 体验券码tv **/
    private TextView         voucher_number_tv;

    /** offline_pay_confirm_voucher_bg_ll **/
    private LinearLayout     offline_pay_confirm_voucher_bg_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类
        super.onCreate (savedInstanceState);

        // 2.初始化视图
        initUIView ();

        // 3.注册监听事件
        registerListener ();

        // 4.初始化数据
        initData ();

        // 5.查询跑男拥有的体验券
        queryVoucherList ();
    }

    /**
     * 查询用户的体验券支付
     */
    private void queryVoucherList(){
        String url = StringUtils.trimNull (getResources ().getString (R.string.experienceListQueryUrl));
        doPost (url, null, ExperienceListQueryRespBean.class, voucher_query_req_tag);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        // 1.当前的支付方式
        currentPayway = CASH_PAY_WAY;

        // 2.转换支付方式
        convertPayway ();
    }

    /**
     * 转换支付方式
     */
    private void convertPayway(){
        if (currentPayway == CASH_PAY_WAY) {
            cash_pay_form_zone_ll.setVisibility (View.VISIBLE);
            voucher_form_zone_ll.setVisibility (View.GONE);
            cash_pay_btn.setTextColor (getResources ().getColor (R.color.blue_bg));
            voucher_pay_btn.setTextColor (getResources ().getColor (R.color.color_29));
        } else if (currentPayway == VOUCHER_PAY_WAY) {
            cash_pay_form_zone_ll.setVisibility (View.GONE);
            voucher_form_zone_ll.setVisibility (View.VISIBLE);
            cash_pay_btn.setTextColor (getResources ().getColor (R.color.color_29));
            voucher_pay_btn.setTextColor (getResources ().getColor (R.color.blue_bg));
            if (voucher != null) {
                use_voucher_btn.setEnabled (true);
                offline_pay_confirm_voucher_bg_ll.setEnabled (true);
                use_voucher_btn.setBackgroundResource (R.drawable.shape_blue_btn);
            } else {
                use_voucher_btn.setEnabled (false);
                offline_pay_confirm_voucher_bg_ll.setEnabled (false);
                use_voucher_btn.setBackgroundResource (R.drawable.shape_gray_btn);
            }

        }
    }

    /**
     * 注册监听事件
     */
    private void registerListener(){
        // 选择现金支付按钮
        cash_pay_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                // 1.当前的支付方式
                currentPayway = CASH_PAY_WAY;

                // 2.转换支付方式
                convertPayway ();
            }
        });

        // 选择体验券支付按钮
        voucher_pay_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                // 1.当前的支付方式
                currentPayway = VOUCHER_PAY_WAY;

                // 2.转换支付方式
                convertPayway ();
            }
        });

        // 使用体验券支付按钮
        use_voucher_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                doOfflinePayConfirm ();
            }
        });

        // 立即现金支付按钮
        cash_pay_commit_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                DialogUtils.showAlertDialog ("确认拿到洗车费用了？", OfflinePayConfirmActivity.this, "取消", "已拿到", new AlertDialogueCallBack () {

                    @Override
                    public void doCallBack(){
                        doOfflinePayConfirm ();
                    }
                });
            }
        });

    }

    /**
     * 线下支付确认
     */
    private void doOfflinePayConfirm(){
        String url = StringUtils.trimNull (getResources ().getString (R.string.offlinePayConfirmUrl));
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        NameValuePair orderId = new BasicNameValuePair ("orderId",CommonUtils.queryCurrentReceivedOrderId (this));
        if (currentPayway == VOUCHER_PAY_WAY) {
            NameValuePair couponId = new BasicNameValuePair ("couponId",String.valueOf (voucher.getId ()));
            params.add (couponId);
        }
        NameValuePair type = new BasicNameValuePair ("payType",String.valueOf (currentPayway));
        params.add (orderId);
        params.add (type);
        doPost (url, params, null, offline_pay_confirm_http_req_tag);
    }

    /**
     * 处理http请求成功
     */
    @Override
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        if (reqTag == offline_pay_confirm_http_req_tag) {
            handleOfflinePayConfirmSucc (data);
        } else if (reqTag == voucher_query_req_tag) {
            handleVoucherListQuerySucc (data);
        }
    }

    /**
     * 处理http请求失败
     */
    @Override
    protected void handleHttpFail(String errorcode,String errorMsg,int reqTag){
        super.handleHttpFail (errorcode, errorMsg, reqTag);
        if (reqTag == voucher_query_req_tag) {
            handleVoucherListQueryFail ();
        }
    }

    /**
     * 处理查询体验券列表失败
     */
    private void handleVoucherListQueryFail(){
        voucher = null;
        voucher_number_tv.setText ("查询体验券失败");
    }

    /**
     * 处理体验券列表查询成功
     * @param data
     */
    private void handleVoucherListQuerySucc(BaseRespBean data){
        ExperienceListQueryRespBean bean = (ExperienceListQueryRespBean) data;
        List<Voucher> voucheres = bean.getListCoupon ();
        if (voucheres != null && !voucheres.isEmpty ()) {
            voucher = voucheres.get (0);
            voucher_number_tv.setText (StringUtils.trimNull (voucher.getName ()));
        } else {
            voucher = null;
            voucher_number_tv.setText ("无可用体验券");
        }
    }

    /**
     * 处理线下支付确认成功
     * @param data
     */
    private void handleOfflinePayConfirmSucc(BaseRespBean data){
        // 1.删除订单
        OrderDao.newInstance (this).deleteAllOrder ();
        CommonUtils.saveCurrentReceivedOrderId ("", this);

        // 2.页面跳转到接单页面
        CustomToastDialog.makeText (this, "完成订单", "恭喜你，又有一笔新收入了，继续加油", Toast.LENGTH_SHORT, true).show ();
        cash_pay_commit_btn.postDelayed (new Runnable () {

            @Override
            public void run(){
                redirectToTargetActivity (OrderReceiveActivity.class, null);
                finish ();
            }
        }, 2000);

    }

    /**
     * 初始化ui视图
     */
    private void initUIView(){
        // 1.初始化布局文件
        setContentView (R.layout.offline_pay_confirm_activity);
        super.initTopTitle (R.drawable.top_service, "现金支付确认", R.drawable.top_customer);
        // 2.现金支付按钮
        cash_pay_btn = (Button) findViewById (R.id.cash_pay_btn);
        // 3.体验券按钮
        voucher_pay_btn = (Button) findViewById (R.id.voucher_pay_btn);
        // 4.使用体验券按钮
        use_voucher_btn = (Button) findViewById (R.id.use_voucher_btn);
        // 5.线下支付确认按钮
        cash_pay_commit_btn = (Button) findViewById (R.id.cash_pay_commit_btn);
        // 6.现金支付表单区域
        cash_pay_form_zone_ll = (LinearLayout) findViewById (R.id.cash_pay_form_zone_ll);
        // 7.体验券支付表单区域
        voucher_form_zone_ll = (LinearLayout) findViewById (R.id.voucher_form_zone_ll);
        // 8.体验券码tv
        voucher_number_tv = (TextView) findViewById (R.id.voucher_number_tv);
        // 9.offline_pay_confirm_voucher_bg_ll
        offline_pay_confirm_voucher_bg_ll = (LinearLayout) findViewById (R.id.offline_pay_confirm_voucher_bg_ll);

    }

}
