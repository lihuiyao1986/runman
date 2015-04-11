package com.clt.runman.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.activity.index.OrderReceiveActivity;
import com.clt.runman.activity.orderflow.ConfirmStartWorkActivity;
import com.clt.runman.adapter.RobOrderAdapter;
import com.clt.runman.adapter.RobOrderAdapter.RobOrderAdpaterCallBack;
import com.clt.runman.db.dao.OrderDao;
import com.clt.runman.db.model.OrderDaoModel;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.OrderReceiveRespBean;
import com.clt.runman.model.OrderStatusRespBean;
import com.clt.runman.model.RobOrderDetailBean;
import com.clt.runman.model.RobbedOrderListQueryRespBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DateUtils;
import com.clt.runman.utils.DialogUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.widget.pullrefresh.PullToRefreshBase;
import com.clt.runman.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.clt.runman.widget.pullrefresh.PullToRefreshListView;

/**
 *@Description:抢单/已抢单Fragment
 *@Author:张聪
 *@Since:2015年3月23日下午2:58:49
 */
@SuppressLint("NewApi")
public class RobOrderFragment extends BaseFragment implements RobOrderAdpaterCallBack {

    private final static int         unRobbed_order_list_http_req_tag = 100;
    private String                   title;
    private final static int         robbed_order_list_http_req_tag   = 200;
    private final static int         rob_order_http_req_tag           = 300;
    private final static int         get_order_status_req_tag         = 400;
    private ListView                 listView;
    private PullToRefreshListView    pullListView;
    private TextView                 emptyView;
    private View                     rootView;
    private String                   type;
    private List<RobOrderDetailBean> orderList                        = new ArrayList<RobOrderDetailBean> ();
    private RobOrderAdapter          robOrderAdapter;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate (R.layout.rob_order_activity, null);
    }

    private RobOrderFragment() {

    }

    /**
     * 获取实体类
     * @param connectBean
     * @return
     */
    public static RobOrderFragment newInstance(String type){
        RobOrderFragment fragment = new RobOrderFragment ();
        Bundle bundle = new Bundle ();
        bundle.putString ("type", type);
        fragment.setArguments (bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        Bundle bundle = getArguments ();
        type = StringUtils.trimNull (bundle.getString ("type"), "0");
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated (view, savedInstanceState);

        // 1.根视图
        rootView = view;

        // 2.初始化视图
        initUIView ();

        // 3.初始化数据
        initData ();
    }

    /**
     * 初始化uiview视图
     */
    private void initUIView(){
        pullListView = (PullToRefreshListView) rootView.findViewById (R.id.rob_order_listview);
        pullListView.setPullRefreshEnabled (true);
        pullListView.setOnRefreshListener (new OnRefreshListener<ListView> () {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView){
                loadData (false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView){}
        });
        listView = pullListView.getRefreshableView ();
        emptyView = new TextView (getActivity ());
        emptyView.setLayoutParams (new LayoutParams (LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        emptyView.setTextColor (getResources ().getColor (R.color.color_36));
        emptyView.setTextSize (getResources ().getDimension (R.dimen.tSize20));
        emptyView.setGravity (Gravity.CENTER);
        emptyView.setVisibility (View.GONE);
        if (type == OrderReceiveActivity.robbed_type) {
            emptyView.setText ("暂没抢单");
        } else if (type == OrderReceiveActivity.unrobbed_type) {
            emptyView.setText ("等待派单");
        }
        ((ViewGroup) listView.getParent ()).addView (emptyView);
        listView.setEmptyView (emptyView);
        listView.setDivider (getActivity ().getResources ().getDrawable (R.drawable.seperator_line_selector));
        listView.setDividerHeight (CommonUtils.dip2px (getActivity (), 1.f));
        listView.setOnItemClickListener (new OnItemClickListener () {

            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                if (type == OrderReceiveActivity.robbed_type) {
                    RobOrderDetailBean orderInfo = orderList.get (position);
                    /****
                     * 如果是已确认赶往的订单，则跳转到对应的页面
                     * 否则跳转到确认赶往页面
                     */
                    if (orderInfo.getStatus () > AppConstant.ORDER_RECEIVED) {
                        redirectPageByOrderStatusAndPaytype (orderInfo.getStatus (), orderInfo.getPayType ());
                    } else {
                        Bundle bundle = new Bundle ();
                        bundle.putSerializable ("orderInfo", orderInfo);
                        redirectToTargetActivity (ConfirmStartWorkActivity.class, bundle);
                    }
                }
            }
        });
        orderList = new ArrayList<RobOrderDetailBean> ();
        int resourceId = "0".equals (type) ? R.layout.rob_order_item_activity : R.layout.robbed_order_item_activity;
        robOrderAdapter = new RobOrderAdapter (getActivity (),orderList,resourceId,type,this);
        listView.setAdapter (robOrderAdapter);
    }

    /**
     * 加载数据
     */
    private void loadData(boolean showProgress){
        String url = "";
        int reqTag = 0;
        if (type == OrderReceiveActivity.robbed_type) {
            url = getActivity ().getResources ().getString (R.string.robbedOrderListQueryUrl);
            reqTag = robbed_order_list_http_req_tag;
        } else if (type == OrderReceiveActivity.unrobbed_type) {
            url = getActivity ().getResources ().getString (R.string.unRobbedOrderListQueryUrl);
            reqTag = unRobbed_order_list_http_req_tag;
        }
        doPost (url, null, RobbedOrderListQueryRespBean.class, reqTag, showProgress);
    }

    /**
     * 处理请求成功
     */
    @Override
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        if (reqTag == rob_order_http_req_tag) {
            handleRobOrderSucc (data);
        } else if (reqTag == robbed_order_list_http_req_tag || reqTag == unRobbed_order_list_http_req_tag) {
            handleQueryOrderListSucc (data);
        } else if (reqTag == get_order_status_req_tag) {
            handleOrderStatusQuerySucc (data);
        }
    }

    /**
     *@Description: TODO(这里用一句话描述这个方法的作用) 
     *@Author: 李焱生
     *@Since: 2015年4月3日上午10:13:27
     *@param errorcode
     *@param errorMsg
     *@param reqTag
     *@return
     */
    @Override
    protected String convertErrorMsg(String errorcode,String errorMsg,int reqTag){
        if (reqTag == rob_order_http_req_tag && "9002005".equals (errorcode)) { return "手太慢，被别人抢了"; }
        return super.convertErrorMsg (errorcode, errorMsg, reqTag);
    }

    /**
     *@Description: 处理失败 
     *@Author: 李焱生
     *@Since: 2015年4月3日上午10:04:59
     *@param errorcode
     *@param errorMsg
     *@param reqTag
     */
    @Override
    protected void handleHttpFail(String errorcode,String errorMsg,int reqTag){
        // 转义错误描述
        errorMsg = convertErrorMsg (errorcode, errorMsg, reqTag);
        if (reqTag == rob_order_http_req_tag) {
            if ("9002005".equals (errorcode)) {
                DialogUtils.showToast (getActivity (), errorMsg);
            } else {
                super.handleHttpFail (errorcode, errorMsg, reqTag);
            }
            new Handler ().postDelayed (new Runnable () {

                @Override
                public void run(){
                    pullListView.startRefreshing ();
                }
            }, 200);
        } else {
            super.handleHttpFail (errorcode, errorMsg, reqTag);
        }
    }

    /**
     *@Description: 处理查询跑男订单状态成功的情况
     *@Author: 李焱生
     *@Since: 2015年4月2日下午5:30:32
     *@param data
     */
    private void handleOrderStatusQuerySucc(BaseRespBean data){
        OrderStatusRespBean resultBean = (OrderStatusRespBean) data;
        OrderDaoModel order = resultBean.getOrder ();
        if (order != null) {
            // 1.保存当前已接单的订单号
            String orderId = order.getOrderId ();
            CommonUtils.saveCurrentReceivedOrderId (orderId, getActivity ());

            // 2.保存订单
            OrderDao.newInstance (getActivity ()).deleteAllOrder ();
            OrderDao.newInstance (getActivity ()).saveOrder (order);
        }
    }

    /**
     * 处理抢单成功的http请求
     * @param data
     */
    private void handleRobOrderSucc(BaseRespBean data){
        DialogUtils.showToast (getActivity (), "抢单成功");
        new Handler ().postDelayed (new Runnable () {

            @Override
            public void run(){
                pullListView.startRefreshing ();
            }
        }, 200);
    }

    /**
     * 查询待抢单列表成功
     * @param data
     */
    private void handleQueryOrderListSucc(BaseRespBean data){
        RobbedOrderListQueryRespBean resultBean = (RobbedOrderListQueryRespBean) data;
        orderList.clear ();
        orderList.addAll (resultBean.getOrders ());
        pullListView.onPullDownRefreshComplete ();
        pullListView.setLastUpdatedLabel (DateUtils.getLastUpdateTime ());
        robOrderAdapter.notifyDataSetChanged ();
    }

    /**
     * 初始化数据
     */
    public void initData(){
        loadData (true);
        if (type == OrderReceiveActivity.robbed_type) {
            getRunManOrderStatus ();
        }
    }

    @Override
    public void robBtnClicked(Button btn,RobOrderDetailBean orderInfo){
        doOrderRob (orderInfo);
    }

    /**
     * 抢单
     * @param orderInfo
     */
    private void doOrderRob(RobOrderDetailBean orderInfo){
        String url = StringUtils.trimNull (getActivity ().getResources ().getString (R.string.receiveOrder));
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        NameValuePair orderIdParam = new BasicNameValuePair ("orderId",StringUtils.trimNull (orderInfo.getOrderId ()));
        params.add (orderIdParam);
        doPost (url, params, OrderReceiveRespBean.class, rob_order_http_req_tag, true, "正在抢单，请稍后...");
    }

    /**
     * @Description:获取跑男订单状态
     * @Author: 张聪
     * @Since: 2015年1月26日下午12:29:56
     */
    private void getRunManOrderStatus(){
        RunMan runMan = CommonUtils.getLoginedRunManInfo (getActivity ());
        if (null != runMan) {
            List<NameValuePair> params = new ArrayList<NameValuePair> ();
            params.add (new BasicNameValuePair ("runManId",runMan.getRunManId ()));
            String orderSatusUrl = AppUtils.getContext ().getResources ().getString (R.string.ingOrder);
            doPost (orderSatusUrl, params, OrderStatusRespBean.class, get_order_status_req_tag);
        }
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
