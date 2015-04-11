package com.clt.runman.activity.order;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.adapter.OrderQueryAdapter;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.db.model.OrderDaoModel;
import com.clt.runman.model.BaseOrderQueryItemBean;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.OrderListQueryRespBean;
import com.clt.runman.model.OrderQueryDetailBean;
import com.clt.runman.model.OrderQueryTotalBean;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DialogUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.SwipeDirect;
import com.clt.runman.widget.pinnedsectionlistview.PinnedSectionListView;
import com.clt.runman.widget.pullrefresh.PullToRefreshBase;
import com.clt.runman.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.clt.runman.widget.pullrefresh.PullToRefreshPinnedSectionListView;

/**
 * 订单列表查询页面
 * @author yanshengli
 * @since 2015-3-5
 */
public class OrderListQueryActivity extends BaseActivity
{

    /** 订单列表 **/
    private PullToRefreshPinnedSectionListView orderListview;

    /** pinnedSectionListView **/
    private PinnedSectionListView              pinnedSectionListView;

    /** 适配器 **/
    private OrderQueryAdapter                  adapter;

    /** 数据 **/
    private List<BaseOrderQueryItemBean>       dataList;

    /** 汇总信息 **/
    private Map<String, OrderQueryTotalBean>   totalInfoMap;

    /** 列表数据为空时显示的视图 **/
    private TextView                           emptyListTv;

    /** 返回按钮 **/
    private Button                             order_query_nav_bar_left_btn;

    /** 订单日期 **/
    @SuppressLint("SimpleDateFormat")
    private DateFormat                         orderDateFormat             = new SimpleDateFormat ("yyyy年MM月dd日");
    
    /** 服务端返回的订单日期格式 **/
    @SuppressLint("SimpleDateFormat")
    private DateFormat                         orderOriginalDateFormat     = new SimpleDateFormat ("yyyyMMddHHmmss");

    /** 当前的页码 **/
    private int                                currentPage                 = 1;

    /** 每页显示的条目数 **/
    private int                                size                        = 10;

    /** 总页数 **/
    private int                                totalPage                   = 0;

    /** 获取订单列表的http请求tag **/
    private final static int                   get_order_list_http_req_tag = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);

        // 2.初始化uiview
        initUIView ();

        // 4.初始化数据
        initData ();
    }

    /**
     * 初始化数据
     */
    private void initData()
    {
        // 汇总信息
        totalInfoMap = new HashMap<String, OrderQueryTotalBean> ();

        // 获取数据集
        dataList = new ArrayList<BaseOrderQueryItemBean> ();

        // 查询订单列表
        queryOrderList (true);
    }

    /**
     * 查询订单信息
     */
    private void queryOrderList(boolean showProgress)
    {
        String url = StringUtils.trimNull (getResources ().getString (R.string.orderListQueryUrl));
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        NameValuePair pageNo = new BasicNameValuePair ("pageNo",String.valueOf (currentPage));
        NameValuePair pageSize = new BasicNameValuePair ("pageSize",String.valueOf (size));
        params.add (pageNo);
        params.add (pageSize);
        doPost (url, params, OrderListQueryRespBean.class, get_order_list_http_req_tag, showProgress);
    }

    /**
     * 处理http失败的请求
     */
    @Override
    protected void handleHttpFail(String errorcode,String errorMsg,int reqTag)
    {
        super.handleHttpFail (errorcode, errorMsg, reqTag);
    }

    /**
     * 处理http成功的请求
     */
    @Override
    protected void handleHttpSuccess(BaseRespBean data,int reqTag)
    {
        super.handleHttpSuccess (data, reqTag);
        if (reqTag == get_order_list_http_req_tag) 
        {
            handleOrderListQuerySucc (data);
        }
    }

    /**
     * 订单查询成功
     * @param data
     */
    private void handleOrderListQuerySucc(BaseRespBean data)
    {
        OrderListQueryRespBean bean = (OrderListQueryRespBean) data;
        List<OrderDaoModel> orders = bean.getOrderPagination ().getItems ();
        totalPage = bean.getOrderPagination ().getTotalCount ();
        if (orders != null && !orders.isEmpty ()) 
        {
            for ( OrderDaoModel orderDaoModel : orders ) 
            {
                OrderQueryDetailBean detailBean = new OrderQueryDetailBean ();
                detailBean.setConfirmTime(orderDaoModel.getFinishTime());
                //detailBean.setOriginalOrderDate(StringUtils.trimNull (orderDaoModel.getCreateTime()));
                detailBean.setCarNum (StringUtils.trimNull (orderDaoModel.getCarPlateNum (), "--"));
                detailBean.setCarPosition (StringUtils.trimNull (orderDaoModel.getAddress (), "--"));
                detailBean.setOrderSum (StringUtils.trimNull (orderDaoModel.getOrderAmt (), "0"));
                detailBean.setOrderTime (StringUtils.trimNull (orderDaoModel.getOrderHopeTime()));
                detailBean.setPayway (StringUtils.trimNull (orderDaoModel.getPayType ()));
                detailBean.setPhone(StringUtils.trimNull(orderDaoModel.getPhone()));
                detailBean.setPaywayStr (CommonUtils.getPaywayStr (orderDaoModel.getPayType ()));
                detailBean.setOrderStatus (StringUtils.trimNull (orderDaoModel.getStatus ()));
                detailBean.setOrderStatusStr (CommonUtils.getOrderStatusStr (orderDaoModel.getStatus ()));
                detailBean.setType (BaseOrderQueryItemBean.DETAIL);
                detailBean.setOrderId(StringUtils.trimNull (orderDaoModel.getOrderId()));
                fillData (detailBean);
            }
            if (totalPage > currentPage) 
            {
                currentPage = bean.getOrderPagination ().getPageNo () + 1;
            } 
            else 
            {
                orderListview.setHasMoreData (false);
            }
        } else
        {
            if (dataList != null && !dataList.isEmpty ()) 
            {
                orderListview.setHasMoreData (false);
                DialogUtils.showToast (this, "没有更多数据了");
            }
        }
        // 更新结果集
        updateResultList ();
    }

    /**
     * 填充数据
     * @param resultList
     */
    private void fillData(OrderQueryDetailBean bean){
        String orderDate = convertOrderDate(bean.getOrderTime());
        OrderQueryTotalBean totalBean = totalInfoMap.get (orderDate);
        if (totalBean == null) {
            totalBean = new OrderQueryTotalBean ();
            totalBean.setDate (orderDate);
            totalBean.setTotalNum ("1");
            totalBean.setTotalIncome (bean.getOrderSum ());
            totalBean.setType (BaseOrderQueryItemBean.TOTAL);
            totalBean.setIndex (dataList.size ());
            totalInfoMap.put (orderDate, totalBean);
            dataList.add (totalBean);
        } else {
            BigDecimal totalSum = new BigDecimal (StringUtils.trimNull (totalBean.getTotalIncome (), "0.00"));
            BigDecimal orderSum = new BigDecimal (StringUtils.trimNull (bean.getOrderSum (), "0.00"));
            totalSum = totalSum.add (orderSum);
            totalBean.setTotalIncome (totalSum.toString ());
            int totalNum = Integer.parseInt (totalBean.getTotalNum ());
            totalNum += 1;
            totalBean.setTotalNum (String.valueOf (totalNum));
            int index = totalBean.getIndex ();
            dataList.set (index, totalBean);
        }
        dataList.add (bean);
    }

    /**
     * 获取订单日期
     * @param originalOrderTime
     * @return
     */
    private String convertOrderDate(String originalOrderTime){
        try {
            return orderDateFormat.format (orderOriginalDateFormat.parse (originalOrderTime));
        } catch (Exception e) {}
        return "";
    }

    /**
     * 更新结果集
     */
    private void updateResultList(){
        adapter.refresh (dataList);
        orderListview.onPullUpRefreshComplete ();
    }

    /**
     * 初始化视图
     */
    private void initUIView(){
        // 1.布局文件
        setContentView (R.layout.order_list_query_activity);

        // 2.带下拉的订单列表视图
        orderListview = (PullToRefreshPinnedSectionListView) findViewById (R.id.order_query_list_view);
        orderListview.setPullLoadEnabled (true);
        orderListview.setPullRefreshEnabled (false);
        orderListview.setOnRefreshListener (new OnRefreshListener<PinnedSectionListView> () {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<PinnedSectionListView> refreshView){

            }

            // 上拉刷新
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<PinnedSectionListView> refreshView){
                // 查询订单列表
                queryOrderList (false);
            }
        });

        // 3.pinnedListview
        adapter = new OrderQueryAdapter (this,dataList,R.layout.order_query_total_list_item,R.layout.order_query_detail_list_item);
        pinnedSectionListView = orderListview.getRefreshableView ();
        emptyListTv = new TextView(this);
        emptyListTv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        emptyListTv.setTextColor(getResources().getColor(R.color.color_36));
        emptyListTv.setTextSize(getResources().getDimension(R.dimen.tSize20));
        emptyListTv.setGravity(Gravity.CENTER);
        emptyListTv.setVisibility(View.GONE);
        emptyListTv.setText("暂无订单");
        ((ViewGroup)pinnedSectionListView.getParent()).addView(emptyListTv);
        pinnedSectionListView.setEmptyView(emptyListTv);
        pinnedSectionListView.setAdapter (adapter);
        pinnedSectionListView.setVerticalFadingEdgeEnabled(false);
        pinnedSectionListView.setHorizontalFadingEdgeEnabled(false);
        pinnedSectionListView.setDivider (getResources ().getDrawable (R.color.color_32));
        pinnedSectionListView.setDividerHeight (CommonUtils.dip2px (this, 0.5f));
        pinnedSectionListView.setOnItemClickListener (new OnItemClickListener () 
        {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id)
            {
             BaseOrderQueryItemBean itemBean = dataList.get(position);
             int type = itemBean.getType();
             if(type == BaseOrderQueryItemBean.DETAIL)
             {
              OrderQueryDetailBean bean = (OrderQueryDetailBean)itemBean;
              Bundle bundle = new Bundle();
              bundle.putSerializable("orderItem", bean);
              redirectToTargetActivity(OrderListQueryActivity.this,OrderDetailActivity.class, bundle,SwipeDirect.DIRECT_LEFT);
             }
            }
        });

        // 5.返回按钮
        order_query_nav_bar_left_btn = (Button) findViewById (R.id.order_query_nav_bar_left_btn);
        order_query_nav_bar_left_btn.setOnClickListener (new View.OnClickListener () 
        {
            @Override
            public void onClick(View v){
                finish ();
                CommonUtils.outAnim();
            }
        });
    }
}
