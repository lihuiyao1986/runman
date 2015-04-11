package com.clt.runman.activity.index;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.activity.order.OrderListQueryActivity;
import com.clt.runman.db.dao.OrderDao;
import com.clt.runman.fragment.BaseFragmentActivity;
import com.clt.runman.fragment.RobOrderFragment;
import com.clt.runman.machineAccess.Machine;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.model.TodayFinishedOrderQueryRespBean;
import com.clt.runman.model.UserLoginRespBean;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.StringUtils;

/**
 *@Description:接单页面
 *@Author:张聪
 *@Since:2015年3月23日下午2:05:16
 */
@SuppressLint("NewApi")
public class OrderReceiveActivity extends BaseFragmentActivity{

    /** 抢单Fragment **/
    public final static String    unrobbed_type                      = "0";

    /**已抢单Fragment**/
    public final static String    robbed_type                        = "1";

    /** 来源 **/
    public final static String    source_from_notify_value           = "1";
    public final static String    source_from_notify_key             = "source_from_notify_key";

    /** 下班请求Tag **/
    private final static int      off_duty_http_tag                  = 100;

    /** 今日完成订单数查询http请求tag **/
    private final static int      today_finished_order_query_req_tag = 300;

    /** 抢单按钮 **/
    private Button                tab_rob_order_btn;
    private View                  rob_order_view;

    /** 已抢单按钮 **/
    private Button                tab_robbed_order_btn;
    private View                  robbed_order_view;
    private FragmentManager       manager;
    private FragmentTransaction   transaction;

    /** 查询订单 **/
    private Button                rob_page_query_order_btn;
    /** 下班按钮 **/
    private Button                offwork_btn;

    /** 今日完成的订单数 **/
    private TextView              order_receive_ordernum_tv;

    /** 接单的广播 **/
    private OrderDispatchReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState){

        // 1.调用父类方法
        super.onCreate (savedInstanceState);

        // 2.初始化视图
        initView ();

        // 3.默认选项卡
        changeTabSelect (unrobbed_type);

        // 4.默认列表
        changeFragmentByType (unrobbed_type);

        // 5.查询今日完成订单
        queryTodayInfoOrderInfo ();

        // 6.注册广播
        registerOrderDispatchReceiver ();
    }

    /**
     * 注册receiver
     */
    private void registerOrderDispatchReceiver(){
        receiver = new OrderDispatchReceiver ();
        IntentFilter filter = new IntentFilter ();
        filter.addAction (AppConstant.Order_Dispatch_Action);
        registerReceiver (receiver, filter);
    }

    /**
     * 初始化视图
     */
    private void initView(){
        setContentView (R.layout.order_receive_activity);
        tab_rob_order_btn = (Button) findViewById (R.id.tab_rob_order_btn);
        rob_order_view = findViewById (R.id.rob_order_view);

        tab_robbed_order_btn = (Button) findViewById (R.id.tab_robbed_order_btn);
        robbed_order_view = findViewById (R.id.robbed_order_view);
        // 1.抢单
        tab_rob_order_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                changeFragmentByType (unrobbed_type);
                changeTabSelect (unrobbed_type);
            }
        });

        // 2.已抢单
        tab_robbed_order_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                changeFragmentByType (robbed_type);
                changeTabSelect (robbed_type);
            }
        });

        // 3.订单
        rob_page_query_order_btn = (Button) findViewById (R.id.rob_page_query_order_btn);
        rob_page_query_order_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                redirectToTargetActivity (OrderListQueryActivity.class, null);
            }
        });

        // 4.下班
        offwork_btn = (Button) findViewById (R.id.order_receive_offwork_btn);
        offwork_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                offWorkClick ();
            }
        });

        // 5.今日完成的订单数
        order_receive_ordernum_tv = (TextView) findViewById (R.id.order_receive_ordernum_tv);
    }

    /**
     * 获取今日完成订单数量
     */
    private void queryTodayInfoOrderInfo(){
        String url = StringUtils.trimNull (getResources ().getString (R.string.todayFinishOrderUrl));
        doPost (url, null, TodayFinishedOrderQueryRespBean.class, today_finished_order_query_req_tag);
    }

    /**
     * 更改fragment
     * @param type
     */
    private void changeFragmentByType(String type){
        manager = getSupportFragmentManager ();
        transaction = manager.beginTransaction ();
        transaction.replace (R.id.order_fragment_layout, RobOrderFragment.newInstance (type));
        transaction.commit ();
    }

    /**
     *@Description: 修改选项卡状态
     *@Author: 张聪
     *@Since: 2015年3月26日上午10:06:09
     *@param type
     */
    private void changeTabSelect(String type){
        if (type.equals (unrobbed_type)) {
            tab_rob_order_btn.setTextColor (getResources ().getColor (R.color.color_18));
            rob_order_view.setBackgroundColor (getResources ().getColor (R.color.color_18));

            tab_robbed_order_btn.setTextColor (getResources ().getColor (R.color.gray));
            robbed_order_view.setBackgroundColor (getResources ().getColor (R.color.gray));
        } else {
            tab_rob_order_btn.setTextColor (getResources ().getColor (R.color.gray));
            rob_order_view.setBackgroundColor (getResources ().getColor (R.color.gray));

            tab_robbed_order_btn.setTextColor (getResources ().getColor (R.color.color_18));
            robbed_order_view.setBackgroundColor (getResources ().getColor (R.color.color_18));
        }
    }

    /**
     *@Description: 下班
     *@Author: 张聪
     *@Since: 2015年1月19日下午9:26:49
     *@param view 
     */
    private void offWorkClick(){
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        String offDutyUrl = AppUtils.getContext ().getResources ().getString (R.string.offDuty);
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        params.add (new BasicNameValuePair ("runManId",runMan.getRunManId ()));
        doPost (offDutyUrl, params, UserLoginRespBean.class, off_duty_http_tag, "正在提交申请...");
    }

    /**
     * http请求成功
     */
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        switch (reqTag) {
            case off_duty_http_tag:
                powerOffMachine ();
                processOffWorkSuccess (data);
                break;
            case today_finished_order_query_req_tag:
                handleTodayFinishedOrderQuerySucc (data);
                break;
            default:
                break;
        }
    }

    /**
     *@Description: 转义错误消息
     *@Author: 李焱生
     *@Since: 2015年4月3日上午10:17:01
     *@param errorcode
     *@param errorMsg
     *@param reqTag
     *@return
     */
    @Override
    protected String convertErrorMsg(String errorcode,String errorMsg,int reqTag){
        if (off_duty_http_tag == reqTag && "2001006".equals (errorcode)) { return "完成洗车中订单才能下班"; }
        return super.convertErrorMsg (errorcode, errorMsg, reqTag);
    }

    /**
     * 处理今日完成的订单数查询
     * @param data
     */
    private void handleTodayFinishedOrderQuerySucc(BaseRespBean data){
        TodayFinishedOrderQueryRespBean bean = (TodayFinishedOrderQueryRespBean) data;
        order_receive_ordernum_tv.setText (StringUtils.trimNull (bean.getOrderStatistics ().getDateOrderNum (), "--"));
    }

    /**
     * 处理下班成功
     * @param obj
     */
    private void processOffWorkSuccess(Object obj){
        // 1.删除所有的订单
        OrderDao.newInstance (this).deleteAllOrder ();
        CommonUtils.saveCurrentReceivedOrderId ("", this);

        // 2.下班之后，不再上传跑男的地理位置
        getRunManApplication ().stopUploadRunmanLoc ();

        // 3.跳转页面
        redirectToTargetActivity (IndexActivity.class, null);

        // 4.关闭当前页面
        finish ();
    }

    /**
     *@Description:下班并关闭设备
     *@Author: 张聪
     *@Since: 2015-3-10下午4:15:24
     */
    private void powerOffMachine(){
        RunMan runMan = CommonUtils.getLoginedRunManInfo (AppUtils.getContext ());
        if (null != runMan && !StringUtils.isEmpty (runMan.getEquipmentMac ()) && !StringUtils.isEmpty (runMan.getEquipmentKey ())) {
            Machine machine = Machine.getMachine (AppUtils.getContext (), runMan.getEquipmentMac (), runMan.getEquipmentKey (), new Handler ());
            machine.powerOff ();
        }
    }

    /**
     * 接受订单派送的广播器
     * @author yanshengli
     */
    public class OrderDispatchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context,Intent intent){
            // 1.切换的待抢单页面
            changeTabSelect (unrobbed_type);
            // 2.切换的待抢单页面
            changeFragmentByType (unrobbed_type);
        }
    }

    /**
     * 销毁activity
     */
    @Override
    public void onDestroy(){
        super.onDestroy ();
        unregisterReceiver (receiver);
    };

}
