package com.clt.runman.activity.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.db.dao.WashcarPicInfoDao;
import com.clt.runman.db.model.WashcarPicInfoDaoModel;
import com.clt.runman.model.OrderQueryDetailBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.SwipeDirect;

/**
 * 订单详情页面
 * @author yanshengli
 * @since 2015-3-20
 */
public class OrderDetailActivity extends BaseActivity {

    // 到账状态
    private TextView             order_detail_order_status_tv;

    // 确认时间
    private TextView             order_detail_order_confirm_time_tv;

    // 车主号码
    private TextView             order_detail_car_owner_phone_tv;

    // 下单时间
    private TextView             order_detail_order_time_tv;

    // 车牌号
    private TextView             order_detail_carnum_tv;

    // 停车位置
    private TextView             order_detail_car_posi_tv;

    // 支付方式
    private TextView             order_detail_payway_tv;

    // 订单金额
    private TextView             order_detail_order_sum_tv;

    // 洗车前照片按钮
    private Button               order_detail_washcar_pre_pic_btn;

    // 洗车后照片按钮
    private Button               order_detail_washcar_after_pic_btn;

    // 返回按钮
    private Button               order_detail_nav_bar_left_btn;

    // 当前订单
    private OrderQueryDetailBean orderItem;

    // order_detail_washcar_pre_pic_ll
    private LinearLayout         order_detail_washcar_pre_pic_ll;

    @SuppressLint("SimpleDateFormat")
    private DateFormat           format1 = new SimpleDateFormat ("yyyyMMddHHmmss");

    @SuppressLint("SimpleDateFormat")
    private DateFormat           format2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm");

    @SuppressLint("SimpleDateFormat")
    private DateFormat           format3 = new SimpleDateFormat ("yyyyMMdd");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);

        // 2.初始化视图
        initUIView ();

        // 3.注册监听事件
        initListeners ();

        // 4.查询订单
        initData ();
    }

    /**
     * 查询订单 
     */
    private void initData(){
        Bundle bundle = getIntent ().getExtras ();
        orderItem = (OrderQueryDetailBean) bundle.get ("orderItem");
        if (orderItem != null) {
            order_detail_order_status_tv.setText (StringUtils.trimNull (orderItem.getOrderStatusStr (), "--"));
            order_detail_order_confirm_time_tv.setText (StringUtils.trimNull (convertOrderTime (orderItem.getConfirmTime ())));
            order_detail_order_time_tv.setText (StringUtils.trimNull (convertOrderTime (orderItem.getOrderTime ()), "--"));
            order_detail_carnum_tv.setText (StringUtils.trimNull (orderItem.getCarNum (), "--"));
            order_detail_car_posi_tv.setText (StringUtils.trimNull (orderItem.getCarPosition (), "--"));
            order_detail_payway_tv.setText (StringUtils.trimNull (orderItem.getPaywayStr (), "--"));
            String phone = StringUtils.trimNull (orderItem.getPhone ());
            if (!StringUtils.isEmpty (phone) && phone.length () == 11) {
                phone = phone.substring (0, 3) + "********";
            }
            order_detail_car_owner_phone_tv.setText (phone);
            String payway = StringUtils.trimNull (orderItem.getPayway ());
            if ("1".equals (payway)) {
                order_detail_order_status_tv.setText ("已使用");
                order_detail_order_sum_tv.setText ("0.00元");
            } else {
                order_detail_order_sum_tv.setText (orderItem.getOrderSum () + "元");
            }
        }

        boolean hasPrePicList = hasPrePicList ();
        boolean hasAfterPicList = hasAfterPicList ();

        if (hasPrePicList && hasAfterPicList) {
            order_detail_washcar_pre_pic_ll.setVisibility (View.VISIBLE);
            order_detail_washcar_pre_pic_btn.setVisibility (View.VISIBLE);
            order_detail_washcar_after_pic_btn.setVisibility (View.VISIBLE);
        } else if (hasPrePicList && !hasAfterPicList) {
            order_detail_washcar_pre_pic_ll.setVisibility (View.VISIBLE);
            order_detail_washcar_pre_pic_btn.setVisibility (View.VISIBLE);
            order_detail_washcar_after_pic_btn.setVisibility (View.GONE);
        } else if (!hasPrePicList && hasAfterPicList) {
            order_detail_washcar_pre_pic_ll.setVisibility (View.VISIBLE);
            order_detail_washcar_pre_pic_btn.setVisibility (View.GONE);
            order_detail_washcar_after_pic_btn.setVisibility (View.VISIBLE);
        } else {
            order_detail_washcar_pre_pic_ll.setVisibility (View.GONE);
            order_detail_washcar_pre_pic_btn.setVisibility (View.GONE);
            order_detail_washcar_after_pic_btn.setVisibility (View.GONE);
        }
        // order_detail_washcar_pre_pic_ll.setVisibility(View.VISIBLE);
        // order_detail_washcar_pre_pic_btn.setVisibility(View.VISIBLE);
        // order_detail_washcar_after_pic_btn.setVisibility(View.VISIBLE);
    }

    /**
     * 是否有洗车前拍照
     * @return
     */
    private boolean hasPrePicList(){
        List<WashcarPicInfoDaoModel> picPreList = queryPicInfoList (AppConstant.washcar_before_type);
        return (picPreList != null && !picPreList.isEmpty ());
    }

    /**
     * 是否有洗车后拍照
     * @return
     */
    private boolean hasAfterPicList(){
        List<WashcarPicInfoDaoModel> picAfterList = queryPicInfoList (AppConstant.washcar_after_type);
        return (picAfterList != null && !picAfterList.isEmpty ());
    }

    /**
     * 转换下单时间
     * @param orderTime
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private String convertOrderTime(String orderTime){
        String result = "";
        if (!StringUtils.isEmpty (orderTime)) {
            try {
                return format2.format (format1.parse (orderTime));
            } catch (ParseException e) {
                e.printStackTrace ();
            }
        }
        return result;
    }

    /**
     * 注册监听事件
     */
    private void initListeners(){
        // 1.洗车前照片
        order_detail_washcar_pre_pic_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                Bundle bundle = new Bundle ();
                bundle.putSerializable ("orderItem", orderItem);
                redirectToTargetActivity (OrderDetailActivity.this, CheckBeforePicActivity.class, bundle, SwipeDirect.DIRECT_LEFT);
            }
        });

        // 2.洗车后照片
        order_detail_washcar_after_pic_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                Bundle bundle = new Bundle ();
                bundle.putSerializable ("orderItem", orderItem);
                redirectToTargetActivity (OrderDetailActivity.this, CheckAfterPicActivity.class, bundle, SwipeDirect.DIRECT_LEFT);
            }
        });

        // 3.返回按钮
        order_detail_nav_bar_left_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                finish ();
                CommonUtils.outAnim ();
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initUIView(){
        setContentView (R.layout.order_detail_activity);
        order_detail_order_status_tv = (TextView) findViewById (R.id.order_detail_order_status_tv);
        order_detail_order_status_tv.setText ("--");
        order_detail_order_confirm_time_tv = (TextView) findViewById (R.id.order_detail_order_confirm_time_tv);
        order_detail_order_confirm_time_tv.setText ("--");
        order_detail_car_owner_phone_tv = (TextView) findViewById (R.id.order_detail_car_owner_phone_tv);
        order_detail_car_owner_phone_tv.setText ("--");
        order_detail_order_time_tv = (TextView) findViewById (R.id.order_detail_order_time_tv);
        order_detail_order_time_tv.setText ("--");
        order_detail_carnum_tv = (TextView) findViewById (R.id.order_detail_carnum_tv);
        order_detail_carnum_tv.setText ("--");
        order_detail_car_posi_tv = (TextView) findViewById (R.id.order_detail_car_posi_tv);
        order_detail_car_posi_tv.setText ("--");
        order_detail_payway_tv = (TextView) findViewById (R.id.order_detail_payway_tv);
        order_detail_payway_tv.setText ("--");
        order_detail_order_sum_tv = (TextView) findViewById (R.id.order_detail_order_sum_tv);
        order_detail_order_sum_tv.setText ("--");
        order_detail_washcar_pre_pic_btn = (Button) findViewById (R.id.order_detail_washcar_pre_pic_btn);
        order_detail_washcar_after_pic_btn = (Button) findViewById (R.id.order_detail_washcar_after_pic_btn);
        order_detail_nav_bar_left_btn = (Button) findViewById (R.id.order_detail_nav_bar_left_btn);
        order_detail_washcar_pre_pic_ll = (LinearLayout) findViewById (R.id.order_detail_washcar_pre_pic_ll);
    }

    /**
     * 查询之前拍照过的照片信息列表
     */
    private List<WashcarPicInfoDaoModel> queryPicInfoList(int picType){
        WashcarPicInfoDaoModel picInfo = new WashcarPicInfoDaoModel ();
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        String orderTime = orderItem.getOrderTime ();
        picInfo.setPicDate (orderTime.substring (0, 8));// 日期
        picInfo.setRunmanid (runMan.getRunManId ());// 跑男ID
        picInfo.setOrderid (orderItem.getOrderId ());// 订单号
        picInfo.setPicType (picType);
        return WashcarPicInfoDao.newInstance (this).queryPicInfoList (picInfo);
    }

}
