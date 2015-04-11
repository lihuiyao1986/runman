package com.clt.runman.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.clt.runman.R;
import com.clt.runman.db.dao.OrderDao;
import com.clt.runman.db.model.OrderDaoModel;
import com.clt.runman.machineAccess.Machine;
import com.clt.runman.machineAccess.MachineErrors;
import com.clt.runman.machineAccess.MachineMessage;
import com.clt.runman.machineAccess.MachineStatus;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.OrderStatusRespBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DialogUtils;
import com.clt.runman.utils.GestureUtils;
import com.clt.runman.utils.GestureUtils.Screen;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.widget.toast.CustomToastDialog;

/**
 *@Description:洗车流程需要继承的Activity
 *@Author:张聪
 *@Since:2015-1-29下午4:51:14
 */
@SuppressLint({ "HandlerLeak", "Registered" })
public class WashCarFlowActivity extends BaseActivity {

    /**获取订单请求tag**/
    protected static final int order_status_http_tag = 10000;

    /** 手势 **/
    protected GestureDetector  gestureDetector;

    /** 屏幕对象 **/
    protected Screen           screen;

    /** 订单状态 **/
    protected Integer          orderStatus           = 0;

    /** 当前订单 **/
    protected OrderDaoModel    order;

    /**设备对象**/
    protected Machine          machine;

    /**设备MAC地址**/
    protected String           equipmentMac          = null;

    /**设备配对密码**/
    protected String           equipmentKey          = null;

    /**配对次数**/
    protected int              pairCount             = 0;

    /**连接丢失允许尝试的次数**/
    protected int              attemptNum            = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);
        // 2.获取订单状态
        getOrderStatus ();
        // 3.获取手势
        gestureDetector = new GestureDetector (this,onGestureListener);
        // 4.获取屏幕对象
        screen = GestureUtils.getScreenPix (this);
        // 5.初始化话AppContext
        appContext = getAppContext ();
    }

    /**
     * 更新按钮的状态
     */
    protected void updateButtonStatus(){

    }

    /** 手势监听器对象 **/
    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener () {

                                                            @Override
                                                            public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY){
                                                                float x = e2.getX () - e1.getX ();
                                                                float y = e2.getY () - e1.getY ();
                                                                // 限制必须得划过屏幕的1/3才能算划过
                                                                float x_limit = screen.widthPixels / 3;
                                                                float y_limit = screen.heightPixels / 3;
                                                                float x_abs = Math.abs (x);
                                                                float y_abs = Math.abs (y);
                                                                if (x_abs >= y_abs) {
                                                                    if (x > x_limit || x < -x_limit) {
                                                                        if (x > 0) {
                                                                            swipeRight ();
                                                                        } else {
                                                                            swipeLeft ();
                                                                        }
                                                                    }
                                                                } else {
                                                                    if (y > y_limit || y < -y_limit) {
                                                                        if (y > 0) {
                                                                            swipeDown ();
                                                                        } else {
                                                                            swipeUp ();
                                                                        }
                                                                    }
                                                                }
                                                                return true;
                                                            }
                                                        };

    /**
     * 向右边划--子类覆盖
     */
    protected void swipeRight(){

    }

    /**
     * 向左边划--子类覆盖
     */
    protected void swipeLeft(){

    }

    /**
     * 向上划--子类覆盖
     */
    protected void swipeUp(){

    }

    /**
     * 向下划--子类覆盖
     */
    protected void swipeDown(){

    }

    /**
     *@Description: 获取订单状态
     *@Author: 张聪
     *@Since: 2015-1-29下午4:52:43
     *@return
     */
    protected void getOrderStatus(){
        String orderUrl = AppUtils.getContext ().getResources ().getString (R.string.ingOrder);
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        if (null != runMan && !StringUtils.isEmpty (runMan.getRunManId ())) {
            List<NameValuePair> params = new ArrayList<NameValuePair> ();
            params.add (new BasicNameValuePair ("runManId",runMan.getRunManId ()));
            // 发送http请求
            doPost (orderUrl, params, OrderStatusRespBean.class, order_status_http_tag);
        }
    }

    /**
     *@Description:请求成功回调
     *@Author: 张聪
     *@Since: 2015-1-31下午3:52:44
     *@param data
     *@param reqTag
     */
    @Override
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        switch (reqTag) {
            case order_status_http_tag:
                // 1.获取订单对象
                OrderStatusRespBean orderStatusRespBean = (OrderStatusRespBean) data;
                order = orderStatusRespBean.getOrder ();

                // 2.保存当前已接单的订单号
                String orderId = order.getOrderId ();
                CommonUtils.saveCurrentReceivedOrderId (orderId, this);

                // 3.保存订单
                OrderDao.newInstance (this).deleteAllOrder ();
                OrderDao.newInstance (this).saveOrder (order);

                // 4.获取订单状态
                if (null != orderStatusRespBean.getOrder ()) {
                    orderStatus = Integer.valueOf (orderStatusRespBean.getOrder ().getStatus ());
                }
                // 5.更新按钮状态
                updateButtonStatus ();

                // 如果订单状态大于等于3000,不配对
                if (Integer.parseInt (order.getStatus ()) < AppConstant.ORDER_WASH_AFTER) {
                    // 6.初始化话设备
                    initMachine ();

                    // 7.开始配对
                    pairMachine ();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 处理请求失败的情况
     */
    @Override
    protected void handleHttpFail(String errorcode,String errorMsg,int reqTag){
        super.handleHttpFail (errorcode, errorMsg, reqTag);

        if (order_status_http_tag == reqTag) {
            // 1.从数据库中查询当前订单
            order = queryCurrentOrderFromDB ();

            // 2.更新订单状态
            updateButtonStatus ();

        }
    }

    /**
     * 从数据库中查询当前订单
     * @return
     */
    private OrderDaoModel queryCurrentOrderFromDB(){
        String orderid = CommonUtils.queryCurrentReceivedOrderId (this);
        return OrderDao.newInstance (this).queryOrderByOrderId (orderid);
    }

    /**
     * 导航左按钮被点击
     */
    @Override
    protected void navLeftBtnClicked(){
        super.navLeftBtnClicked ();
        callServicePhone ();
    }

    /**
     * 导航右按钮被点击
     */
    @Override
    protected void navRightBtnClicked(){
        super.navRightBtnClicked ();
        callCustomerPhone ();
    }

    /**
     * 导航中间按钮被点击
     */
    protected void navCenterBtnClicked(){
        super.navCenterBtnClicked ();
        initMachine ();
        pairMachine ();
    }

    /**
     * 拨打客服电话
     */
    protected void callServicePhone(){
        String callServicePhoneTip = getResources ().getString (R.string.callServicePhoneTip);
        final String servicePhoneNumber = getResources ().getString (R.string.servicePhoneNumber);
        CommonUtils.callPhone (callServicePhoneTip, servicePhoneNumber, this);
    }

    /**
     * 拨打客服电话
     */
    protected void callCustomerPhone(){
        String callCustomerPhoneTip = getResources ().getString (R.string.callCustomerPhoneTip);
        String orderid = CommonUtils.queryCurrentReceivedOrderId (this);
        final OrderDaoModel order = OrderDao.newInstance (this).queryOrderByOrderId (orderid);
        final String phone = order.getPhone ();
        CommonUtils.callPhone (callCustomerPhoneTip, phone, this);
    }

    /**
     * 获取订单状态
     * @return
     */
    protected int obtainCurrOrderStatus(){
        String statusStr = null != order ? StringUtils.trimNull (order.getStatus (), String.valueOf (AppConstant.ORDER_RECEIVED)) : "0";
        return Integer.parseInt (statusStr);
    }

    /**
     * 获取订单支付方式
     * @return
     */
    protected int obtainCurrOrderPayType(){
        int payType = -1;
        if (!StringUtils.isEmpty (order.getPayType ())) {
            payType = Integer.parseInt (order.getPayType ());
        }
        return payType;
    }

    /**
     *@Description: 获取设备MAC地址和配对密码 
     *@Author: 张聪
     *@Since: 2015-3-13下午5:56:50
     */
    protected void initMachine(){
        RunMan runMan = CommonUtils.getLoginedRunManInfo (AppUtils.getContext ());
        if (null != runMan && !StringUtils.isEmpty (runMan.getEquipmentMac ()) && !StringUtils.isEmpty (runMan.getEquipmentKey ())) {
            machine = Machine.getMachine (AppUtils.getContext (), runMan.getEquipmentMac (), runMan.getEquipmentKey (), pairHandler);
        } else {
            DialogUtils.showToast (AppUtils.getContext (), "MAC地址、配对密码为空");
        }
    }

    /**
     *@Description: 设备配对
     *@Author: 张聪
     *@Since: 2015年3月21日下午2:21:54
     *@param mac
     *@param pwd
     */
    protected void pairMachine(){
        if (null != machine) {
            machine.connect ();
        }
    }

    protected Handler pairHandler = new Handler () {

                                      @Override
                                      public void handleMessage(Message msg){
                                          String stateString = "";
                                          switch (msg.what) {
                                              case MachineMessage.STATE:
                                                  switch (msg.arg1) {
                                                      case MachineStatus.OFF:
                                                          stateString = "未配对";
                                                          break;
                                                      case MachineStatus.MATCHING:
                                                          stateString = "配对中";
                                                          CustomToastDialog.makeText (AppUtils.getContext (), "配对提醒", "设备正在配对中..", Toast.LENGTH_SHORT, true)
                                                                  .show ();
                                                          break;
                                                      case MachineStatus.MATCHED:
                                                          stateString = "已配对";
                                                          break;
                                                      default:
                                                          break;
                                                  }
                                                  if (!StringUtils.isEmpty (stateString)) {
                                                      appContext.setMachineStatusTitle (stateString);
                                                  }

                                                  navCenterBtn.setText (appContext.getMachineStatusTitle ());
                                                  break;
                                              case MachineMessage.ERROR:
                                                  break;
                                              case MachineMessage.EXCEPTION:
                                                  switch (msg.arg1) {
                                                      case MachineErrors.BLUETOOTH_CONNECT_FAILED:
                                                          stateString = "未配对";
                                                          break;
                                                      case MachineErrors.BLUETOOTH_CONNECT_LOST:
                                                          stateString = "未配对";
                                                          break;
                                                      default:
                                                          break;
                                                  }
                                                  if (!StringUtils.isEmpty (stateString)) {
                                                      appContext.setMachineStatusTitle (stateString);
                                                  }
                                                  navCenterBtn.setText (appContext.getMachineStatusTitle ());

                                                  // 断开连接，尝试2次重连
                                                  if (pairCount < attemptNum) {
                                                      initMachine ();
                                                      pairMachine ();
                                                      pairCount++;
                                                  }
                                                  break;
                                          }
                                      }
                                  };

}
