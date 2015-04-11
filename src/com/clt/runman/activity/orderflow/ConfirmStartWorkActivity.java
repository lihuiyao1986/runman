package com.clt.runman.activity.orderflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.db.dao.OrderDao;
import com.clt.runman.interfaces.AlertDialogueCallBack;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.OrderReceiveRespBean;
import com.clt.runman.model.RobOrderDetailBean;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DateUtils;
import com.clt.runman.utils.DialogUtils;

/**
 * 确定赶往页面
 * @author yanshengli
 * @since 2015-3-25
 */
public class ConfirmStartWorkActivity extends BaseActivity {

    /**确认赶往请求tag**/
    private static final int   arrive_parking_http_tag = 100;
    /** 左导航按钮 **/
    private Button             confirm_start_work_nav_bar_left_btn;

    /** 右导航按钮 **/
    private Button             confirm_start_work_nav_bar_right_btn;

    /** 车牌号 **/
    private TextView           confirm_start_work_car_number_tv;

    /** 停车位置 **/
    private TextView           confirm_start_work_car_position_tv;

    /** 预约时间 **/
    private TextView           confirm_start_work_order_time_tv;

    /** 确认赶往按钮 **/
    private Button             confirm_start_work_btn;

    /** 订单 **/
    private RobOrderDetailBean orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);

        // 2.初始化视图
        initUIView ();

        // 3.初始化数据
        initData ();

        // 4.注册监听器
        registerListener ();
    }

    /***
     * 注册监听器
     */
    private void registerListener(){
        // 1.确认赶往
        confirm_start_work_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                DialogUtils.showAlertDialog ("确认赶往停车位置吗？", ConfirmStartWorkActivity.this, "取消", "赶往", new AlertDialogueCallBack () {

                    @Override
                    public void doCallBack(){
                        doConfirm ();
                    }
                });
            }
        });

        // 2.左按钮被点击
        confirm_start_work_nav_bar_left_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                finish ();
                CommonUtils.outAnim ();
            }
        });

        // 3.打电话按钮被点击
        confirm_start_work_nav_bar_right_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                String callCustomerPhoneTip = getResources ().getString (R.string.callCustomerPhoneTip);
                CommonUtils.callPhone (callCustomerPhoneTip, orderInfo.getPhone (), ConfirmStartWorkActivity.this);
            }
        });
    }

    /**
     * 确认赶往
     */
    private void doConfirm(){
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        params.add (new BasicNameValuePair ("orderId",orderInfo.getOrderId ()));
        String arriveParking = getString (R.string.arriveParkingUrl);
        doPost (arriveParking, params, OrderReceiveRespBean.class, arrive_parking_http_tag, true);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        orderInfo = (RobOrderDetailBean) getIntent ().getSerializableExtra ("orderInfo");
        confirm_start_work_car_number_tv.setText (orderInfo.getCarPlateNum ());
        confirm_start_work_car_position_tv.setText (orderInfo.getAddress ());
        confirm_start_work_order_time_tv.setText (DateUtils.convertHopeTime (orderInfo.getServiceBeginTime (), orderInfo.getServiceEndTime ()));
    }

    /**
     * 初始化ui视图
     */
    private void initUIView(){
        // 1.布局文件
        setContentView (R.layout.confirm_start_work_activity);

        // 2.左导航按钮
        confirm_start_work_nav_bar_left_btn = (Button) findViewById (R.id.confirm_start_work_nav_bar_left_btn);

        // 3.右导航按钮
        confirm_start_work_nav_bar_right_btn = (Button) findViewById (R.id.confirm_start_work_nav_bar_right_btn);

        // 4.车牌号
        confirm_start_work_car_number_tv = (TextView) findViewById (R.id.confirm_start_work_car_number_tv);

        // 5.车辆位置
        confirm_start_work_car_position_tv = (TextView) findViewById (R.id.confirm_start_work_car_position_tv);

        // 6.预约时间
        confirm_start_work_order_time_tv = (TextView) findViewById (R.id.confirm_start_work_order_time_tv);

        // 7.确认赶往按钮
        confirm_start_work_btn = (Button) findViewById (R.id.confirm_start_work_btn);
    }

    /**
     * 处理确认赶往操作
     */
    private void processArriveParking(OrderReceiveRespBean data){

        // 2.保存当前已接的订单
        CommonUtils.saveCurrentReceivedOrderId (data.getOrder ().getOrderId (), this);

        // 3.先删除所有的订单
        OrderDao.newInstance (this).deleteAllOrder ();

        // 4.保存订单
        OrderDao.newInstance (this).saveOrder (data.getOrder ());

        // 5.跳转页面
        redirectToTargetActivity (FoundActivity.class, null);

        // 6.关闭当前页面
        finish ();
    }

    /**
     *@Description: TODO(这里用一句话描述这个方法的作用) 
     *@Author: 李焱生
     *@Since: 2015年4月3日上午10:38:57
     *@param errorcode
     *@param errorMsg
     *@param reqTag
     *@return
     */
    @Override
    protected String convertErrorMsg(String errorcode,String errorMsg,int reqTag)
    {
        if(arrive_parking_http_tag == reqTag && "9002001".equals (errorcode))
        {
            return "请先完成进行中订单";
        }
        return super.convertErrorMsg (errorcode, errorMsg, reqTag);
    }

    @Override
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        switch (reqTag) {
            case arrive_parking_http_tag:
                if (data instanceof OrderReceiveRespBean) processArriveParking ((OrderReceiveRespBean) data);
                break;

            default:
                break;
        }
    }
}
