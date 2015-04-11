package com.clt.runman.activity.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.activity.index.IndexActivity;
import com.clt.runman.adapter.IncomeQueryAdapter;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.IncomeListQueryRespBean;
import com.clt.runman.model.OrderIncomeQueryDetailBean;
import com.clt.runman.model.OrderStatistic;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.SwipeDirect;

/**
 * 收入明细列表查询页面
 * @author yanshengli
 * @since 2015-3-5
 */
public class IncomeListQueryActivity extends BaseActivity {

    /** 查询的日期格式 **/
    @SuppressLint("SimpleDateFormat")
    private DateFormat                       format1                   = new SimpleDateFormat ("yyyyMM");

    /** 订单日期输出格式 **/
    @SuppressLint("SimpleDateFormat")
    private DateFormat                       format2                   = new SimpleDateFormat ("yyyy年MM月");

    /** 服务端返回的日期格式 **/
    @SuppressLint("SimpleDateFormat")
    private DateFormat                       format3                   = new SimpleDateFormat ("yyyyMMddHHmmss");

    /** 列表中需要展示的日期格式 **/
    @SuppressLint("SimpleDateFormat")
    private DateFormat                       format4                   = new SimpleDateFormat ("MM月dd日");

    /** 列表 **/
    private ListView                         income_query_list_view;

    /** 适配器对象 **/
    private IncomeQueryAdapter               adapter;

    /** 集合对象 **/
    private List<OrderIncomeQueryDetailBean> dataList                  = new ArrayList<OrderIncomeQueryDetailBean> ();

    /** emptyView **/
    private View                             emptyView;

    /** 头部 **/
    private LinearLayout                     income_query_header_view_ll;

    /** 总订单数 **/
    private TextView                         income_query_total_ordernum_tv;

    /** 总收入 **/
    private TextView                         income_query_total_ordersum_tv;

    /** 左侧按钮 **/
    private Button                           income_query_left_btn;

    /** 右侧按钮 **/
    private Button                           income_query_right_btn;

    /** 查询日期 **/
    private Button                           income_query_date;

    /**当前的月份**/
    private String                           currentMonth;

    /** 收入统计查询http请求的tag **/
    private final static int                 income_list_query_req_tag = 100;

    /** 返回按钮 **/
    private Button                           order_income_query_nav_bar_left_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);

        // 2.初始化UIView
        initUIView ();

        // 3.初始化数据
        initData ();
    }

    /**
     * 初始化数据
     */
    private void initData(){
        // 默认设置为当前月份
        currentMonth = format1.format (new Date ());

        // 设置当前月份
        setCurrentMonth ();

        // 查询数据
        queryIncomeList ();
    }

    /**
     * 设置当前月份
     */
    private void setCurrentMonth(){
        String currentMonthStr = "";
        try {
            currentMonthStr = format2.format (format1.parse (currentMonth));
        } catch (ParseException e) {}
        income_query_date.setText (currentMonthStr);
    }

    /**
     * 获取下一个月
     * @param date
     * @return
     */
    private String getNextMonth(String date){
        Calendar calendar = Calendar.getInstance ();
        try {
            calendar.setTime (format1.parse (date));
            calendar.add (Calendar.MONTH, 1);
            return format1.format (calendar.getTime ());
        } catch (ParseException e) {}
        return "";
    }

    /**
     * 获取上一个月
     * @param date
     * @return
     */
    private String getPreMonth(String date){
        Calendar calendar = Calendar.getInstance ();
        try {
            calendar.setTime (format1.parse (date));
            calendar.add (Calendar.MONTH, -1);
            return format1.format (calendar.getTime ());
        } catch (ParseException e) {}
        return "";
    }

    /**
     * 更新数据
     */
    private void updateData(){
        if (dataList == null || dataList.size () == 0) {
            income_query_header_view_ll.setVisibility (View.GONE);
        } else {
            income_query_header_view_ll.setVisibility (View.VISIBLE);
        }
        adapter.notifyDataSetChanged ();
    }

    /**
     * 初始化uiview
     */
    private void initUIView(){
        // 1.布局文件
        setContentView (R.layout.order_income_query_activity);

        // 2.头部
        income_query_header_view_ll = (LinearLayout) findViewById (R.id.income_query_header_view_ll);

        // 3.订单数
        income_query_total_ordernum_tv = (TextView) findViewById (R.id.income_query_total_ordernum_tv);

        // 4.订单收入
        income_query_total_ordersum_tv = (TextView) findViewById (R.id.income_query_total_ordersum_tv);

        // 5.左侧按钮
        income_query_left_btn = (Button) findViewById (R.id.income_query_left_btn);
        income_query_left_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                loadPreMonthOrders ();
            }
        });

        // 6.右侧按钮
        income_query_right_btn = (Button) findViewById (R.id.income_query_right_btn);
        income_query_right_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                loadNextMonthOrders ();
            }
        });

        // 7.查询日期
        income_query_date = (Button) findViewById (R.id.income_query_date);

        // 8.集合列表
        income_query_list_view = (ListView) findViewById (R.id.income_query_list_view);
        adapter = new IncomeQueryAdapter (this,dataList,R.layout.income_query_list_item);
        income_query_list_view.setAdapter (adapter);
        income_query_list_view.setSelector (new ColorDrawable (Color.TRANSPARENT));
        emptyView = findViewById (R.id.income_query_list_empty_view);
        income_query_list_view.setEmptyView (emptyView);

        // 9.返回按钮
        order_income_query_nav_bar_left_btn = (Button) findViewById (R.id.order_income_query_nav_bar_left_btn);
        order_income_query_nav_bar_left_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                redirectToTargetActivity (IncomeListQueryActivity.this, IndexActivity.class, null, SwipeDirect.DIRECT_RIGHT);
            }
        });
    }

    /**
     * 加载上一个月的订单数据
     */
    private void loadPreMonthOrders(){
        currentMonth = getPreMonth (currentMonth);
        setCurrentMonth ();
        queryIncomeList ();
    }

    /**
     * 加载上一个月的订单数据
     */
    private void loadNextMonthOrders(){
        currentMonth = getNextMonth (currentMonth);
        setCurrentMonth ();
        queryIncomeList ();
    }

    /**
     * 查询收入统计列表
     */
    private void queryIncomeList(){
        String url = StringUtils.trimNull (getResources ().getString (R.string.incomeListQueryUrl));
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        NameValuePair date = new BasicNameValuePair ("date",currentMonth);
        params.add (date);
        doPost (url, params, IncomeListQueryRespBean.class, income_list_query_req_tag);
    }

    /**
     * 处理http请求成功的情况
     */
    @Override
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        if (reqTag == income_list_query_req_tag) {
            handleIncomeListQuerySucc (data);
        }
    }

    /**
     * 处理http请求成功的情况
     */
    @Override
    protected void handleHttpFail(String errorcode,String errorMsg,int reqTag){
        super.handleHttpFail (errorcode, errorMsg, reqTag);
        if (reqTag == income_list_query_req_tag) {
            handleIncomeListQueryFail ();
        }
    }

    /**
     * 查询失败
     */
    private void handleIncomeListQueryFail(){
        dataList.clear ();
        updateData ();
    }

    /**
     * 查询成功
     * @param data
     */
    private void handleIncomeListQuerySucc(BaseRespBean data){
        IncomeListQueryRespBean bean = (IncomeListQueryRespBean) data;
        dataList.clear ();
        income_query_total_ordersum_tv.setText (StringUtils.trimNull (bean.getOrderStatistics ().getDateOrderAmt (), "--"));
        income_query_total_ordernum_tv.setText (StringUtils.trimNull (bean.getOrderStatistics ().getDateOrderNum (), "--"));
        List<OrderStatistic> resultList = bean.getOrderStatistics ().getLowLevelOrderStatisticsList ();
        if (resultList != null && !resultList.isEmpty ()) {
            for ( OrderStatistic orderStatistic : resultList ) {
                OrderIncomeQueryDetailBean item = new OrderIncomeQueryDetailBean ();
                String orderDate = getItemOrderDate (orderStatistic.getDate ());
                if (orderDate.startsWith ("0")) {
                    orderDate = orderDate.substring (1);
                }
                item.setOrderDate (orderDate);
                item.setOrderNum (StringUtils.trimNull (orderStatistic.getDateOrderNum ()));
                item.setOrderSum (StringUtils.trimNull (orderStatistic.getDateOrderAmt ()));
                dataList.add (item);
            }
        }
        updateData ();
    }

    /**
     * 获取列表中显示的订单日期
     * @param orderDate
     * @return
     */
    private String getItemOrderDate(String orderDate){
        String resultStr = "--";

        if (!StringUtils.isEmpty (orderDate)) {
            try {
                resultStr = format4.format (format3.parse (orderDate));
            } catch (Exception e) {}
        }
        return resultStr;
    }
}
