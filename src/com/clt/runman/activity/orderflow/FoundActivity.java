package com.clt.runman.activity.orderflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.core.WashCarFlowActivity;
import com.clt.runman.db.dao.OrderDao;
import com.clt.runman.db.model.OrderDaoModel;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.SwipeDirect;

/**
 *@Description:步骤1/4：已找到
 *@Author:张聪
 *@Since:2015年1月20日下午2:40:38
 */
public class FoundActivity extends WashCarFlowActivity {

    /**  找到车辆按钮 **/
    private Button           found_car_btn;
    // 停车地点
    private TextView         carPosition;
    // 备注
    private TextView         comment;
    // 车牌号码
    private TextView         carNum;
    // 车型
    private TextView         carModel;
    // 车颜色
    private TextView         carColor;

    /** 确认找到车辆的请求 **/
    private static final int confirm_found_car_http_tag = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类方法
        super.onCreate (savedInstanceState);
        // 2.初始化uiview
        initUIView ();
        // 3.初始化数据
        initData ();
    }

    /**
     *@Description: 初始化UIView 
     *@Author: 张聪
     *@Since: 2015-1-30上午10:21:32
     */
    private void initUIView(){
        // 1.布局和导航
        setContentView (R.layout.orderflow_found_activity);
        super.initTopTitle (R.drawable.top_service, appContext.getMachineStatusTitle (), R.drawable.top_customer);

        // 2.按钮
        found_car_btn = (Button) findViewById (R.id.found_act_found_btn);
        found_car_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                foundClick ();
            }
        });

        // 3.停车地点
        carPosition = (TextView) findViewById (R.id.found_act_car_position_tv);
        // 4.备注
        comment = (TextView) findViewById (R.id.found_act_car_comment_tv);
        // 5.车牌号码
        carNum = (TextView) findViewById (R.id.found_act_car_number_tv);
        // 6.车型
        carModel = (TextView) findViewById (R.id.found_act_car_type_tv);
        // 7.车颜色
        carColor = (TextView) findViewById (R.id.found_act_car_color_tv);
    }

    /**
     *@Description: 
     *@Author: 张聪
     *@Since: 2015-1-30上午10:21:39
     */
    private void initData(){
        String orderid = CommonUtils.queryCurrentReceivedOrderId (this);
        OrderDaoModel order = OrderDao.newInstance (this).queryOrderByOrderId (orderid);
        if (order != null) {
            // 车辆位置
            carPosition.setText (StringUtils.trimNull (order.getAddress (), "暂无"));

            // 备注
            if (StringUtils.isEmpty (order.getComment ())) {
                comment.setTextColor (getResources ().getColor (R.color.color_20));
            }
            comment.setText (StringUtils.trimNull (order.getComment (), "暂无"));

            // 车牌号
            carNum.setText (StringUtils.trimNull (order.getCarPlateNum ()));

            // 车型
            if (StringUtils.isEmpty (order.getCarBrand ())) {
                carModel.setTextColor (getResources ().getColor (R.color.color_20));
            }
            String carBrand = StringUtils.trimNull (order.getCarBrand (), "暂无");
            carModel.setText (carBrand);

            // 车辆颜色
            if (StringUtils.isEmpty (order.getCarColor ())) {
                carColor.setTextColor (getResources ().getColor (R.color.color_20));
            }
            String car_color = StringUtils.trimNull (order.getCarColor (), "暂无");
            carColor.setText (car_color);
        }
    }

    /**
     *@Description: 已找到
     *@Author: 张聪
     *@Since: 2015年1月19日下午7:43:28
     */
    public void foundClick(){
        String foundUrl = AppUtils.getContext ().getResources ().getString (R.string.found);
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        String orderid = CommonUtils.queryCurrentReceivedOrderId (this);
        params.add (new BasicNameValuePair ("orderId",orderid));
        doPost (foundUrl, params, BaseRespBean.class, confirm_found_car_http_tag);
    }

    /**
     * http请求成功响应回调
     */
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        switch (reqTag) {
            case confirm_found_car_http_tag:
                processConfirmFoundCarCallBack (data);
                break;
            default:
                break;
        }
    }

    /**
     * 处理确认找到车辆的http请求成功的回调
     * @param obj
     */
    private void processConfirmFoundCarCallBack(BaseRespBean obj){
        redirectToTargetActivity (FoundActivity.this, WashCarBeforeActivity.class, new Bundle (), false);
        finish ();
    }

    /**
     *@Description: 监听屏幕滑动事件
     *@Author: 张聪
     *@Since: 2015年1月20日下午2:26:51
     *@param event
     *@return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return gestureDetector.onTouchEvent (event);
    }

    /**
     * 上划
     */
    @Override
    protected void swipeUp(){
        super.swipeUp ();
        if (obtainCurrOrderStatus () >= AppConstant.ORDER_FOUND) {
            // redirectToTargetActivity (FoundActivity.this, WashCarBeforeActivity.class, setParam (), SwipeDirect.DIRECT_UP);
            redirectToTargetActivity (FoundActivity.this, WashCarBeforeActivity.class, null, SwipeDirect.DIRECT_UP);
            finish ();
        }
    }

    /**
     * 更新按钮状态
     */
    @Override
    protected void updateButtonStatus(){
        super.updateButtonStatus ();
        if (obtainCurrOrderStatus () < AppConstant.ORDER_FOUND) {
            found_car_btn.setEnabled (true);
            found_car_btn.setBackgroundResource (R.drawable.shape_blue_btn);
        } else {
            found_car_btn.setEnabled (false);
            found_car_btn.setBackgroundResource (R.drawable.shape_gray_btn);
        }
    }
}
