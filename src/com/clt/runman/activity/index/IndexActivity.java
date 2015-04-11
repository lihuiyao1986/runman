package com.clt.runman.activity.index;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.activity.login.LoginActivity;
import com.clt.runman.activity.order.IncomeListQueryActivity;
import com.clt.runman.activity.order.OrderListQueryActivity;
import com.clt.runman.adapter.SlideMenuItemAdapter;
import com.clt.runman.adapter.WeatherInfoAdapter;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.db.dao.OrderDao;
import com.clt.runman.db.model.OrderDaoModel;
import com.clt.runman.http.HttpRequestClient;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.IncomeListQueryRespBean;
import com.clt.runman.model.OrderStatusRespBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.model.RunmanLocationBean;
import com.clt.runman.model.SlideMenuItemBean;
import com.clt.runman.model.TodayFinishedOrderQueryRespBean;
import com.clt.runman.model.UserLoginRespBean;
import com.clt.runman.model.WeatherIndexInfo;
import com.clt.runman.model.WeatherInfoBean;
import com.clt.runman.model.WeatherInfoResultBean;
import com.clt.runman.model.WeatherQueryRespBean;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.GestureUtils;
import com.clt.runman.utils.JsonUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.SwipeDirect;
import com.clt.runman.widget.slidemenu.SlidingMenu;

/**
 * 首页
 * @author yanshengli
 * @since 2015-1-29
 */
public class IndexActivity extends BaseActivity {

    /** 姓名 **/
    private TextView                rm_name;
    /** 头像 **/
    private ImageView               header_image;
    /** 手机号 **/
    private TextView                rm_phone;
    /** 跑男本月的收入 **/
    private TextView                index_page_current_month_ordersum_tv;
    /** 跑男本月的订单数 **/
    private TextView                index_page_current_month_ordernum_tv;
    /** 展示天气的视图 **/
    private GridView                weather_items_view;
    /** 今日完成订单数tv **/
    private TextView                index_page_income_number_tv;
    /** 退出登录http请求tag **/
    private final static int        logout_req_tag                     = 100;
    /** 上班http请求tag **/
    private final static int        onduty_req_tag                     = 200;
    /** 获取订单状态的http请求tag **/
    private final static int        get_order_status_req_tag           = 300;
    /** 上传个信clientid的http请求tag **/
    private final static int        upload_gexin_clientid_req_tag      = 400;
    /** 获取跑男状态的http请求tag **/
    private final static int        get_runman_status_req_tag          = 500;
    /** 今日完成订单数查询http请求tag **/
    private final static int        today_finished_order_query_req_tag = 600;
    /** 本月订单统计信息查询http请求tag **/
    private final static int        current_month_order_query_req_tag  = 700;
    /** 天气信息列表 **/
    private List<WeatherInfoBean>   weatherInfoList;
    /** 天气适配器 **/
    private WeatherInfoAdapter      weatherAdapter;
    /** index_page_weather_zone_ll **/
    private LinearLayout            index_page_weather_zone_ll;
    /** 导航栏左边按钮 **/
    private Button                  index_page_nav_left_btn;
    /** 右边按钮 **/
    private Button                  index_page_query_order_btn;
    /** 左边抽屉菜单 **/
    private SlidingMenu             slideLeftMenu;
    /** 菜单选项列表 **/
    private ListView                index_page_slidemenu_listview;
    /** 是否适宜洗车的提示 **/
    private TextView                index_page_washcar_tip_tv;
    /** 城市 **/
    private TextView                weather_city_tv;
    /** 查询的日期格式 **/
    @SuppressLint("SimpleDateFormat")
    private DateFormat              format1                            = new SimpleDateFormat ("yyyyMM");
    /** SlideMenuItemAdapter **/
    private SlideMenuItemAdapter    menuItemAdapter;
    /** menuItemList **/
    private List<SlideMenuItemBean> menuItemList;
    /** 消息列表 **/
    private Button                  index_page_message_list_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);
        // 2.初始化uiview
        initUIView ();
        // 3.初始化跑男信息
        initRunManInfo ();
        // 4.上传个推clientID
        uploadClientId ();
        // 5.获取跑男“工作”状态
        updateRunManStatus ();
        // 6.查询天气
        queryWeather ();
        // 7.查询今日完成订单
        queryTodayInfoOrderInfo ();
        // 8.查询本月的统计信息
        queryCurrentMonthOrderInfo ();
    }

    /**
     * 查询本月的订单统计信息
     */
    private void queryCurrentMonthOrderInfo(){
        String url = StringUtils.trimNull (getResources ().getString (R.string.incomeListQueryUrl));
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        NameValuePair date = new BasicNameValuePair ("date",format1.format (new Date ()));
        params.add (date);
        doPost (url, params, IncomeListQueryRespBean.class, current_month_order_query_req_tag);
    }

    /**
     * 获取今日完成订单数量
     */
    private void queryTodayInfoOrderInfo(){
        String url = StringUtils.trimNull (getResources ().getString (R.string.todayFinishOrderUrl));
        doPost (url, null, TodayFinishedOrderQueryRespBean.class, today_finished_order_query_req_tag);
    }

    /**
     * 查询天气
     */
    public void queryWeather(){
        String url = StringUtils.trimNull (getResources ().getString (R.string.QUERY_WEATHER_INFO_URL));
        String ak = StringUtils.trimNull (getResources ().getString (R.string.QUERY_WEATHER_BAIDU_KEY));
        RunmanLocationBean locInfo = CommonUtils.getRunmanLocInfo (this);
        if (locInfo == null) { return; }
        String targetLoc = StringUtils.trimNull (locInfo.getCity ());
        if (StringUtils.isEmpty (targetLoc)) {
            targetLoc = StringUtils.trimNull (locInfo.getLongitude ()) + "," + StringUtils.trimNull (locInfo.getLatitude ());
        }
        NameValuePair location = new BasicNameValuePair ("location",targetLoc);
        NameValuePair type = new BasicNameValuePair ("output","json");
        NameValuePair baiduKey = new BasicNameValuePair ("ak",ak);
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        params.add (location);
        params.add (type);
        params.add (baiduKey);
        HttpRequestClient.doHttpRequest (getAppContext (), params, url, null, 3000, new AjaxCallBack<String> () {

            @Override
            public void onSuccess(String result){
                super.onSuccess (result);
                WeatherQueryRespBean resultBean = JsonUtils.parseObject (result, WeatherQueryRespBean.class);
                if (resultBean != null) {
                    String error = StringUtils.trimNull (resultBean.getError ());
                    if ("0".equals (error)) {
                        List<WeatherInfoResultBean> results = resultBean.getResults ();
                        if (results != null && !results.isEmpty ()) {
                            WeatherInfoResultBean weatherInfoBean = results.get (0);
                            weatherInfoBean.getWeather_data ().get (0).setDate ("今天");
                            weatherInfoList = new ArrayList<WeatherInfoBean> ();
                            weatherInfoList.add (weatherInfoBean.getWeather_data ().get (0));
                            weatherInfoList.add (weatherInfoBean.getWeather_data ().get (1));
                            weatherInfoList.add (weatherInfoBean.getWeather_data ().get (2));
                            weatherAdapter = new WeatherInfoAdapter (IndexActivity.this,weatherInfoList,R.layout.weather_info_list_item);
                            weather_items_view.setAdapter (weatherAdapter);
                            weather_items_view.setSelector (new ColorDrawable (Color.TRANSPARENT));
                            index_page_weather_zone_ll.setVisibility (View.VISIBLE);
                            List<WeatherIndexInfo> weatherIndexList = weatherInfoBean.getIndex ();
                            for ( WeatherIndexInfo weatherIndexItem : weatherIndexList ) {
                                String tipt = StringUtils.trimNull (weatherIndexItem.getTipt ());
                                if ("洗车指数".equalsIgnoreCase (tipt)) {
                                    String desc = StringUtils.trimNull (weatherIndexItem.getZs ());
                                    index_page_washcar_tip_tv.setText (desc);
                                    index_page_washcar_tip_tv.setVisibility (View.VISIBLE);
                                    break;
                                }
                            }
                            weather_city_tv.setText (weatherInfoBean.getCurrentCity ());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t,int errorNo,String strMsg){
                super.onFailure (t, errorNo, strMsg);
            }
        }, HttpRequestClient.HTTP_GET_REQ);
    }

    /**
     * 初始化视图
     */
    private void initUIView(){
        // 1.布局和导航
        setContentView (R.layout.index_activity);
        // super.initTopTitle ("", "跑男", "退出");
        // 2.头像
        header_image = (ImageView) findViewById (R.id.header_image);
        // 3.姓名
        rm_name = (TextView) findViewById (R.id.rm_name);
        // 4.手机号
        rm_phone = (TextView) findViewById (R.id.rm_phone);
        // 5.跑男的收入
        index_page_current_month_ordersum_tv = (TextView) findViewById (R.id.index_page_current_month_ordersum_tv);
        index_page_current_month_ordernum_tv = (TextView) findViewById (R.id.index_page_current_month_ordernum_tv);
        // 6.天气区域
        weather_items_view = (GridView) findViewById (R.id.index_page_weather_items);
        // 7.index_page_weather_zone_ll
        index_page_weather_zone_ll = (LinearLayout) findViewById (R.id.index_page_weather_zone_ll);
        index_page_weather_zone_ll.setVisibility (View.GONE);
        // 8.导航栏左边按钮
        index_page_nav_left_btn = (Button) findViewById (R.id.index_page_nav_left_btn);
        index_page_nav_left_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                slideLeftMenu.showMenu (true);
            }
        });
        // 9.右边导航按钮
        index_page_query_order_btn = (Button) findViewById (R.id.index_page_query_order_btn);
        index_page_query_order_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                redirectToTargetActivity (getAppContext (), OrderListQueryActivity.class, null, SwipeDirect.DIRECT_LEFT);
            }
        });
        // 10.导航菜单
        slideLeftMenu = initLeftSlideMenu ();
        // 11.是否适宜洗车的提示
        index_page_washcar_tip_tv = (TextView) findViewById (R.id.index_page_washcar_tip_tv);
        index_page_washcar_tip_tv.setVisibility (View.GONE);
        // 12.城市
        weather_city_tv = (TextView) findViewById (R.id.weather_city_tv);
        // 13.今日完成订单数
        index_page_income_number_tv = (TextView) findViewById (R.id.index_page_income_number_tv);
        // 14.消息列表按钮
        index_page_message_list_btn = (Button)findViewById (R.id.index_page_message_list_btn);
        index_page_message_list_btn.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v){
                redirectToTargetActivity (getAppContext (), MessageListActivity.class, null, SwipeDirect.DIRECT_LEFT);
            }
        });
    }

    /**
     * 初始化左边导航菜单
     * @return
     */
    private SlidingMenu initLeftSlideMenu(){
        SlidingMenu menu = new SlidingMenu (this);
        // 屏幕的宽度
        int screenWidth = GestureUtils.getScreenPix (this).widthPixels;
        String leftoffset = StringUtils.trimNull (getResources ().getString (R.string.index_page_left_slide_menu_offset), "112");
        // 设置方向
        menu.setMode (SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove (SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes (R.dimen.width5);
        menu.setShadowDrawable (R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffset (screenWidth - CommonUtils.dip2px (this, Integer.parseInt (leftoffset)));
        // 设置渐入渐出效果的值
        menu.setFadeDegree (0.35f);
        // 把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
        menu.attachToActivity (this, SlidingMenu.SLIDING_WINDOW);
        // 为侧滑菜单设置布局
        menu.setMenu (R.layout.index_page_left_slide_menu);
        // 初始化slider菜单
        initSlideUIView (menu);
        // 返回菜单
        return menu;
    }

    /**
     * 初始化slider菜单
     * @param menu
     */
    private void initSlideUIView(SlidingMenu menu){
        menuItemList = initMenuItemList ();
        menuItemAdapter = new SlideMenuItemAdapter (this,menuItemList,R.layout.slidemenu_list_item);
        index_page_slidemenu_listview = (ListView) menu.getMenu ().findViewById (R.id.index_page_slidemenu_listview);
        index_page_slidemenu_listview.setAdapter (menuItemAdapter);
        index_page_slidemenu_listview.setOnItemClickListener (new OnItemClickListener () {

            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                if (position == 1) {
                    redirectToTargetActivity (getApplicationContext (), IncomeListQueryActivity.class, null, SwipeDirect.DIRECT_LEFT);
                } else if (position == 2) {
                    redirectToTargetActivity (getApplicationContext (), MoreActivity.class, null, SwipeDirect.DIRECT_LEFT);
                }
            }

        });
    }

    /**
     *@Description: initMenuItemList method
     *@Author: 李焱生
     *@Since: 2015年4月8日下午4:11:27
     *@return
     */
    private List<SlideMenuItemBean> initMenuItemList(){

        List<SlideMenuItemBean> resultList = new ArrayList<SlideMenuItemBean> ();

        SlideMenuItemBean accountMenuItem = new SlideMenuItemBean ();
        accountMenuItem.setIcon (R.drawable.slide_menu_acct_icon);
        accountMenuItem.setMenuName ("洗车账户");
        accountMenuItem.setExtraInfo ("300");
        resultList.add (accountMenuItem);

        SlideMenuItemBean incomeMenuItem = new SlideMenuItemBean ();
        incomeMenuItem.setIcon (R.drawable.slide_menu_income_icon);
        incomeMenuItem.setMenuName ("统计收入");
        resultList.add (incomeMenuItem);

        SlideMenuItemBean moreMenuItem = new SlideMenuItemBean ();
        moreMenuItem.setIcon (R.drawable.slide_menu_more_icon);
        moreMenuItem.setMenuName ("更多");
        resultList.add (moreMenuItem);

        return resultList;
    }

    /**
     * @Description: 初始化跑男信息
     * @Author: 张聪
     * @Since: 2015年1月10日下午12:35:54
     */
    public void initRunManInfo(){
        // 1.获取跑男信息
        RunMan runMan = CommonUtils.getLoginedRunManInfo (AppUtils.getContext ());
        if (!StringUtils.isEmpty (runMan.getRunManAvatar ())) {
            FinalBitmap.create (this).display (header_image, runMan.getRunManAvatar ());
        } else {
            header_image.setBackgroundResource (R.drawable.runaman_default);
        }
        // 2.姓名
        rm_name.setText (runMan.getRunManName ());
        // 3.手机号
        rm_phone.setText (runMan.getRunManPhone ());
    }

    /**
     * @Description:上班
     * @Author: 张聪
     * @Since: 2015年1月19日下午9:03:08
     */
    public void workClick(View view){
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        String onDutyUrl = AppUtils.getContext ().getResources ().getString (R.string.onDuty);
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        params.add (new BasicNameValuePair ("runManId",runMan.getRunManId ()));
        doPost (onDutyUrl, params, UserLoginRespBean.class, onduty_req_tag, "正在提交申请...");
    }

    /**
     * @Description:上传个推clientID
     * @Author: 张聪
     * @Since: 2015年1月26日上午11:29:45
     */
    private void uploadClientId(){
        String clientId = CommonUtils.getGeXinClientId (this);
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        params.add (new BasicNameValuePair ("clientId",clientId));
        params.add (new BasicNameValuePair ("runManId",runMan.getRunManId ()));
        String uploadClientIdUrl = AppUtils.getContext ().getResources ().getString (R.string.uploadClientId);
        doPost (uploadClientIdUrl, params, UserLoginRespBean.class, upload_gexin_clientid_req_tag);
    }

    /**
     * @Description:获取跑男订单状态
     * @Author: 张聪
     * @Since: 2015年1月26日下午12:29:56
     */
    private void getRunManOrderStatus(){
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        if (null != runMan) {
            int runaManStatus = runMan.getRunManStatus ();
            if (runaManStatus == AppConstant.RUNMAN_STATUS_BUSY) {
                List<NameValuePair> params = new ArrayList<NameValuePair> ();
                params.add (new BasicNameValuePair ("runManId",runMan.getRunManId ()));
                String orderSatusUrl = AppUtils.getContext ().getResources ().getString (R.string.ingOrder);
                doPost (orderSatusUrl, params, OrderStatusRespBean.class, get_order_status_req_tag);
            } else if (runaManStatus == AppConstant.RUNMAN_STATUS_FREE) {
                redirectToTargetActivity (OrderReceive2Activity.class, null);
                finish ();
            }
        }
    }

    /**
     * @Description: 请求之后回调
     * @Author: 张聪
     * @Since: 2015年1月26日下午12:42:52
     * @param data
     * @param reqTag
     */
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        // 调用父类的方法
        super.handleHttpSuccess (data, reqTag);
        // 处理请求成功的逻辑
        switch (reqTag) {
            case onduty_req_tag:// 上班
                // 上班之后需要实时上传跑男的地理位置
                getAppContext ().resumeUploadRunmanLoc ();
                redirectToTargetActivity (OrderReceive2Activity.class, null);
                finish ();
                break;
            case get_order_status_req_tag:// 获取订单的请求
                requestOrderStatus (data);
                break;
            case logout_req_tag:// 退出登录
                redirectToTargetActivity (LoginActivity.class, null);
                finish ();
                break;
            case get_runman_status_req_tag:// 获取跑男状态
                queryRunmanStatusSucc (data);
                break;
            case today_finished_order_query_req_tag:// 查询今日完成订单
                queryTodayFinisedOrderSucc (data);
                break;
            case current_month_order_query_req_tag:
                queryCurrentMonthOrderInfoSucc (data);// 查询本月订单统计信息
                break;
            default:
                break;
        }
    }

    /**
     * 查询本月订单统计信息
     * @param data
     */
    private void queryCurrentMonthOrderInfoSucc(BaseRespBean data){
        IncomeListQueryRespBean bean = (IncomeListQueryRespBean) data;
        index_page_current_month_ordernum_tv.setText (StringUtils.trimNull (bean.getOrderStatistics ().getDateOrderNum (), "0"));
        index_page_current_month_ordersum_tv.setText (StringUtils.trimNull (bean.getOrderStatistics ().getDateOrderAmt (), "0"));
    }

    /**
     * 查询今日完成订单
     * @param data
     */
    private void queryTodayFinisedOrderSucc(BaseRespBean data){
        TodayFinishedOrderQueryRespBean bean = (TodayFinishedOrderQueryRespBean) data;
        index_page_income_number_tv.setText (StringUtils.trimNull (bean.getOrderStatistics ().getDateOrderNum (), "--"));
    }

    /**
     * 查询跑男状态成功的回调
     * @param data
     */
    private void queryRunmanStatusSucc(BaseRespBean data){
        // 1.更新跑男的状态
        UserLoginRespBean userLoginRespBean = (UserLoginRespBean) data;
        RunMan runman = CommonUtils.getLoginedRunManInfo (this);
        runman.setRunManStatus (userLoginRespBean.getStatus ());
        CommonUtils.saveLoginedRunmanInfo (runman, this);

        // 2.查询订单状态
        getRunManOrderStatus ();
    }

    /**
     * 处理请求失败的情况
     */
    public void handleHttpFail(String errorcode,String errorMsg,int reqTag){
        super.handleHttpFail (errorcode, errorMsg, reqTag);
        /**
         * 跑男状态查询失败，需要查询一下订单的状态
         */
        if (reqTag == get_runman_status_req_tag) {
            // 查询订单状态
            getRunManOrderStatus ();
        } else if (reqTag == get_order_status_req_tag) {
            // 查询订单状态失败后，如果跑男已上班，跳转到接单页面
            RunMan runman = CommonUtils.getLoginedRunManInfo (this);
            int runmanStatus = runman.getRunManStatus ();
            if (runmanStatus != AppConstant.RUNMAN_STATUS_OFFDUTY) {
                redirectToTargetActivity (OrderReceive2Activity.class, null);
                finish ();
            }
        }
    }

    /**
     * @Description: 获取订单请求
     * @Author: 张聪
     * @Since: 2015年1月26日下午10:52:13
     * @param orderBean
     * @param code
     */
    private void requestOrderStatus(BaseRespBean orderBean){
        OrderStatusRespBean resultBean = (OrderStatusRespBean) orderBean;
        OrderDaoModel order = resultBean.getOrder ();
        int orderStatus = -1;
        if (order != null) {
            // 1.保存当前已接单的订单号
            String orderId = order.getOrderId ();
            CommonUtils.saveCurrentReceivedOrderId (orderId, this);

            // 2.保存订单
            OrderDao.newInstance (this).deleteAllOrder ();
            OrderDao.newInstance (this).saveOrder (order);

            // 3.根据对应的订单状态,跳转到相应的页面
            if (org.apache.commons.lang3.StringUtils.equals (order.getStatus (), String.valueOf (AppConstant.ORDER_WAIT_LIST))) {
                RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
                orderStatus = runMan.getRunManStatus ();
            } else {
                orderStatus = Integer.parseInt (order.getStatus ());
            }
            // 根据订单状态和支付类型跳转页面
            redirectPageByOrderStatusAndPaytype (orderStatus, order.getPayType ());
        }
    }

    /**
     *@Description: 获取跑男状态 
     *@Author: 张聪
     *@Since: 2015-1-31下午4:55:20
     */
    private void updateRunManStatus(){
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        if (!StringUtils.isEmpty (runMan.getRunManId ())) {
            List<NameValuePair> params = new ArrayList<NameValuePair> ();
            params.add (new BasicNameValuePair ("runManId",runMan.getRunManId ()));
            String runManStatusUrl = AppUtils.getContext ().getResources ().getString (R.string.getRunManStatus);
            doPost (runManStatusUrl, params, UserLoginRespBean.class, get_runman_status_req_tag);
        }
    }

    /**
     * 配置导航栏的样式
     */
    @Override
    protected void configNavBarStyle(Button leftTitle,TextView centerTitle,Button rightTitle){
        // 1.父类方法调用
        super.configNavBarStyle (leftTitle, centerTitle, rightTitle);

        // 2.设置导航标题的样式
        centerTitle.setTextSize (TypedValue.COMPLEX_UNIT_SP, 20);
        centerTitle.setTextColor (getResources ().getColor (R.color.color_14));
        rightTitle.setTextSize (TypedValue.COMPLEX_UNIT_SP, 15);
        rightTitle.setTextColor (getResources ().getColor (R.color.color_5));
    }
}
