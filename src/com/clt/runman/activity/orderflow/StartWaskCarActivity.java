package com.clt.runman.activity.orderflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.clt.runman.R;
import com.clt.runman.core.WashCarFlowActivity;
import com.clt.runman.interfaces.AlertDialogueCallBack;
import com.clt.runman.machineAccess.MachineStatus;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.OrderStatusRespBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.model.UserLoginRespBean;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DialogUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.SwipeDirect;
import com.clt.runman.widget.toast.CustomToastDialog;

/**
 * @Description:步骤3/4：开始洗车
 * @Author:张聪
 * @Since:2015年1月20日下午2:37:54
 */
@SuppressLint("HandlerLeak")
public class StartWaskCarActivity extends WashCarFlowActivity {

    // 订单长度
    private static final int ORDER_LENGH                      = 8;
    // 开始洗车http请求tag
    private static final int start_wash_http_tag              = 100;
    // 完成洗车http请求tag
    private static final int finish_wash_http_tag             = 200;
    // 开始洗车当前订单状态
    private static final int start_current_order_status_tag   = 300;
    // 完成洗车当前订单状态
    private static final int finsish_current_order_status_tag = 400;

    private Button           btnWork;
    private Button           btnPowerOff;
    // 订单ID
    private String           orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        // 1.初始化视图
        initView ();
    }

    /**
     *@Description: 初始化视图 
     *@Author: 张聪
     *@Since: 2015-1-29下午1:46:57
     */
    private void initView(){
        setContentView (R.layout.orderflow_startwaskcar_activity);
        super.initTopTitle (R.drawable.top_service, appContext.getMachineStatusTitle (), R.drawable.top_customer);
        btnWork = (Button) this.findViewById (R.id.btnWork);
        btnPowerOff = (Button) this.findViewById (R.id.btnPowerOff);
    }

    /**
     * @Description: 开始工作
     * @Author: 张聪
     * @Since: 2015年1月19日下午7:54:24
     */
    public void btnWorkClick(View view){
        orderId = CommonUtils.queryCurrentReceivedOrderId (this);
        if (null != orderId) {
            if (null == machine) {
                initMachine ();
            }
            machine.startWork (formatOrderId (orderId));
            String error = machine.getError ();
            if (!StringUtils.isEmpty (error)) {
                CustomToastDialog.makeText (AppUtils.getContext (), "警告", error, Toast.LENGTH_SHORT, true).show ();
            } else {
                CustomToastDialog.makeText (AppUtils.getContext (), "设备开机", "设备已开机，可以开始洗车..", Toast.LENGTH_SHORT, true).show ();
            }
            if (machine.getStatus () >= MachineStatus.READY) {
                postCurrentOrderStatus (start_current_order_status_tag);
            }
        } else {
            DialogUtils.showToast (AppUtils.getContext (), "订单ID为空");
        }
    }

    /**
     *@Description:洗车前获取下订单状态 
     *@Author: 张聪
     *@Since: 2015年3月16日下午7:29:36
     *@param currentOrderStatus
     */
    private void startWashCar(int currentOrderStatus){
        if (currentOrderStatus == AppConstant.ORDER_WASH_BEFORE) {
            String startWashUrl = AppUtils.getContext ().getResources ().getString (R.string.startWash);
            List<NameValuePair> params = new ArrayList<NameValuePair> ();
            params.add (new BasicNameValuePair ("orderId",orderId));
            // 发送http请求
            doPost (startWashUrl, params, UserLoginRespBean.class, start_wash_http_tag);
        } else {
            hideWorkBtn ();
        }
    }

    /**
     *@Description: 格式化订单长度
     *@Author: 张聪
     *@Since: 2015-1-31下午12:50:24
     *@param orderId
     *@return
     */
    private String formatOrderId(String orderId){
        StringBuffer buffer = new StringBuffer (orderId);
        int orderLength = buffer.toString ().trim ().length ();
        int minus = ORDER_LENGH - orderLength;
        if (minus > 0) {
            for ( int i = 0 ; i < minus ; i++ ) {
                buffer.append ("0");
            }
        }
        return buffer.toString ();
    }

    /**
     *@Description: 获取当前订单状态 
     *@Author: 张聪
     *@Since: 2015年3月16日下午7:26:32
     */
    private void postCurrentOrderStatus(int tag){
        String orderUrl = AppUtils.getContext ().getResources ().getString (R.string.ingOrder);
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        if (null != runMan && !StringUtils.isEmpty (runMan.getRunManId ())) {
            List<NameValuePair> params = new ArrayList<NameValuePair> ();
            params.add (new BasicNameValuePair ("runManId",runMan.getRunManId ()));
            // 发送http请求
            doPost (orderUrl, params, OrderStatusRespBean.class, tag);
        }
    }

    /**
     *@Description: 完成洗车
     *@Author: 张聪
     *@Since: 2015-1-29下午3:42:24
     *@param view
     */
    public void btnPowerOffClick(View view){
        postCurrentOrderStatus (finsish_current_order_status_tag);
    }

    /**
     *@Description: 根据当前订单状态 判断是否可以关闭设备
     *@Author: 张聪
     *@Since: 2015年3月16日下午7:04:20
     *@param currentOrderStatus
     */
    private void finishWashCar(int currentOrderStatus){
        // if (currentOrderStatus >= AppConstant.ORDER_WASH_CAR) {
        DialogUtils.showAlertDialog ("确认洗车完成，并关机？", StartWaskCarActivity.this, new AlertDialogueCallBack () {

            RunMan runMan  = CommonUtils.getLoginedRunManInfo (StartWaskCarActivity.this);
            String orderId = CommonUtils.queryCurrentReceivedOrderId (StartWaskCarActivity.this);

            @Override
            public void doCallBack(){
                if (null != machine) {
                    machine.stopWork ();
                }
                if (machine.getStatus () >= MachineStatus.TURN_WORK) {
                    if (Integer.parseInt (order.getStatus ()) == AppConstant.ORDER_WASH_CAR) {
                        String finishWashUrl = AppUtils.getContext ().getResources ().getString (R.string.finishWash);
                        List<NameValuePair> params = new ArrayList<NameValuePair> ();
                        params.add (new BasicNameValuePair ("runManId",runMan.getRunManId ()));
                        params.add (new BasicNameValuePair ("orderId",orderId));
                        // 发送http请求
                        doPost (finishWashUrl, params, UserLoginRespBean.class, finish_wash_http_tag);
                    } else {
                        showWorkBtn ();
                        redirect ();
                    }
                } else {
                    String errorInfo = machine.getError ();
                    if (!StringUtils.isEmpty (errorInfo)) {
                        CustomToastDialog.makeText (AppUtils.getContext (), "警告", errorInfo, Toast.LENGTH_SHORT, true).show ();
                        return;
                    }
                }
            }
        });
        // } else {
        // DialogUtils.showToast (AppUtils.getContext (), "您还没开机洗车，不能关机");
        // }
    }

    /**
     *@Description: 覆盖 
     *@Author: 张聪
     *@Since: 2015-1-31下午2:14:31
     *@param data
     *@param reqTag
     */
    @Override
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        // 调用父类的方法
        super.handleHttpSuccess (data, reqTag);
        switch (reqTag) {
            case start_wash_http_tag:
                hideWorkBtn ();
                break;
            case finish_wash_http_tag:
                showWorkBtn ();
                redirect ();
                break;
            case start_current_order_status_tag:
                OrderStatusRespBean startOrderStatusRespBean = (OrderStatusRespBean) data;
                order = startOrderStatusRespBean.getOrder ();
                if (null != order && !StringUtils.isEmpty (order.getStatus ())) {
                    startWashCar (Integer.parseInt (order.getStatus ()));
                }
                break;
            case finsish_current_order_status_tag:
                OrderStatusRespBean finishOrderStatusRespBean = (OrderStatusRespBean) data;
                order = finishOrderStatusRespBean.getOrder ();
                if (null != order && !StringUtils.isEmpty (order.getStatus ())) {
                    finishWashCar (Integer.parseInt (order.getStatus ()));
                }
                break;
            default:
                break;
        }
    }

    /**
     *@Description: 页面跳转
     *@Author: 张聪
     *@Since: 2015年3月14日下午6:05:10
     */
    private void redirect(){
        redirectToTargetActivity (StartWaskCarActivity.this, WashCarAfterActivity.class, new Bundle (), false);
        finish ();
    }

    /**
     * @Description: 监听屏幕滑动事件
     * @Author: 张聪
     * @Since: 2015年1月20日下午2:26:51
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return gestureDetector.onTouchEvent (event);
    }

    /**
     * 下划
     */
    @Override
    protected void swipeDown(){
        super.swipeDown ();
        redirectToTargetActivity (StartWaskCarActivity.this, WashCarBeforeActivity.class, new Bundle (), SwipeDirect.DIRECT_DOWN);
        finish ();
    }

    /**
     * 上划
     */
    @Override
    protected void swipeUp(){
        super.swipeUp ();
        if (obtainCurrOrderStatus () >= AppConstant.ORDER_FINISH_WORK) {
            // redirectToTargetActivity (StartWaskCarActivity.this, WashCarAfterActivity.class, setParam (), SwipeDirect.DIRECT_UP);
            redirectToTargetActivity (StartWaskCarActivity.this, WashCarAfterActivity.class, null, SwipeDirect.DIRECT_UP);
            finish ();
        }
    }

    /**
     *@Description: 更新按钮状态 
     *@Author: 张聪
     *@Since: 2015年3月21日下午2:11:53
     */
    @Override
    protected void updateButtonStatus(){
        super.updateButtonStatus ();
        if (obtainCurrOrderStatus () == AppConstant.ORDER_WASH_CAR) {
            hideWorkBtn ();
        } else {
            showWorkBtn ();
        }
    }

    /**
     *@Description: 开启洗车按钮,禁用关闭按钮
     *@Author: 张聪
     *@Since: 2015年4月1日下午2:50:51
     */
    private void showWorkBtn(){
        btnWork.setEnabled (true);
        btnWork.setBackgroundResource (R.drawable.shape_blue_btn);
        btnPowerOff.setEnabled (false);
        btnPowerOff.setBackgroundResource (R.drawable.shape_gray_btn);
    }

    /**
     *@Description: 禁用洗车按钮,启用关闭按钮 
     *@Author: 张聪
     *@Since: 2015年4月1日下午2:51:42
     */
    private void hideWorkBtn(){
        btnWork.setEnabled (false);
        btnWork.setBackgroundResource (R.drawable.shape_gray_btn);
        btnPowerOff.setEnabled (true);
        btnPowerOff.setBackgroundResource (R.drawable.shape_blue_btn);
    }
}