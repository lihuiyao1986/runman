/**
 * @Title: MessageListActivity.java
 * @Package com.clt.runman.activity.index
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2015年4月9日 上午10:00:58
 * @Copyright:Copyright (c)
 * @Company:whty李焱生
 * @version V1.0
 */
package com.clt.runman.activity.index;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.adapter.MessageListAdapter;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.db.dao.MessageDao;
import com.clt.runman.db.model.MessageDaoModel;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DateUtils;
import com.clt.runman.utils.DialogUtils;
import com.clt.runman.widget.pullrefresh.PullToRefreshBase;
import com.clt.runman.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.clt.runman.widget.pullrefresh.PullToRefreshListView;

/**
 *@Description:消息列表
 *@Author:李焱生
 *@Since:2015年4月9日上午10:00:58  
 */
@SuppressLint("SimpleDateFormat")
public class MessageListActivity extends BaseActivity {

    /** 左导航按钮 **/
    private Button                msg_list_nav_bar_left_btn;

    /**下拉刷新列表**/
    private PullToRefreshListView pull_list_view;

    /** listView **/
    private ListView              listView;

    /** 空视图 **/
    private TextView              emptyView;

    /** adapter **/
    private MessageListAdapter    adapter;

    /** 集合 **/
    private List<MessageDaoModel> dataList;

    /** 当前页码 **/
    private int                   pageNo = 1;

    /** 每页显示的记录数 **/
    private int                   pageSize    = 10;

    /**
     *@Author: 李焱生
     *@Since: 2015年4月9日上午10:01:23
     *@param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);

        // 2.初始化uiview
        initUIView ();

        // 3.注册事件监听器
        registerListener ();

        // 4.初始化数据
        initData ();
    }

    /**
     *@Description: 初始化数据 
     *@Author: 李焱生
     *@Since: 2015年4月9日下午1:06:45
     */
    private void initData()
    {
        pull_list_view.startRefreshing ();
    }

    /**
     *@Description: 注册事件监听器
     *@Author: 李焱生
     *@Since: 2015年4月9日上午10:20:34
     */
    private void registerListener(){
        msg_list_nav_bar_left_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                finish ();
                CommonUtils.outAnim ();
            }
        });

        pull_list_view.setOnRefreshListener (new OnRefreshListener<ListView> () {

            /**
             *@Description:下拉刷新 
             *@Author: 李焱生
             *@Since: 2015年4月9日上午10:42:36
             *@param refreshView
             */
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                loadDataFromServer();
            }

            /**
             *@Description: 上拉刷新
             *@Author: 李焱生
             *@Since: 2015年4月9日上午10:42:38
             *@param refreshView
             */
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView){
                loadDataFromDB();
            }
        });
    }

    /**
     *@Description: 从数据库中加载消息
     *@Author: 李焱生
     *@Since: 2015年4月9日下午11:00:54
     */
    protected void loadDataFromDB()
    {
        List<MessageDaoModel> list = MessageDao.newInstance (this).queryMsgByPage (null, pageNo, pageSize);
        if(pageNo == 1)
        {
            dataList.clear ();
        }
        pull_list_view.onPullUpRefreshComplete();
        if(list!=null && !list.isEmpty ())
        {
            pageNo++;
            pull_list_view.setHasMoreData (true);
            dataList.addAll (list);
        }
        else
        {
            DialogUtils.showToast (this, "没有更多数据了");
            pull_list_view.setHasMoreData (false);
        }
        adapter.notifyDataSetChanged ();
    }

    /**
     *@Description: 从服务器加载数据
     *@Author: 李焱生
     *@Since: 2015年4月9日下午10:12:05
     */
    protected void loadDataFromServer()
    {
        pageNo = 1;
        List<MessageDaoModel> list = MessageDao.newInstance (this).queryMsgByPage (null, pageNo, pageSize);
        String queryStratTime = new SimpleDateFormat ("yyyyMMddHHmmss").format (new Date ());
        if(list!=null && !list.isEmpty ())
        {
            MessageDaoModel msg = list.get (0);
            queryStratTime = msg.getTime ();
        }
        pull_list_view.onPullDownRefreshComplete ();
        pull_list_view.setLastUpdatedLabel (DateUtils.getLastUpdateTime ());
        MessageDaoModel bean1 = new MessageDaoModel ();
        bean1.setType (AppConstant.remind_type_book_due);
        bean1.setTime (new SimpleDateFormat ("yyyyMMddHHmmss").format (new Date ()));
        bean1.setDescribe ("订单马上到预约时间，请尽快确认赶往。已抢单列表中，订单车牌号浙A725E4,预约时间14:00-15:00");

        MessageDaoModel bean3 = new MessageDaoModel ();
        bean3.setType (AppConstant.remind_type_money_in);
        bean3.setTime (new SimpleDateFormat ("yyyyMMddHHmmss").format (new Date ()));
        bean3.setDescribe ("订单车牌号浙A72742，车主已确认完成洗车，费用已到账");

        MessageDaoModel bean2 = new MessageDaoModel ();
        bean2.setType (AppConstant.remind_type_book_due);
        bean2.setTime (new SimpleDateFormat ("yyyyMMddHHmmss").format (new Date ()));
        bean2.setDescribe ("订单马上到预约时间，请尽快确认赶往。已抢单列表中，订单车牌号浙A725E4,预约时间14:00-15:00");

        MessageDaoModel bean4 = new MessageDaoModel ();
        bean4.setType (AppConstant.remind_type_money_in);
        bean4.setTime (new SimpleDateFormat ("yyyyMMddHHmmss").format (new Date ()));
        bean4.setDescribe ("订单车牌号浙A72742，车主已确认完成洗车，费用已到账");
        list = new ArrayList<MessageDaoModel>();
        list.add (bean1);
        list.add (bean2);
        list.add (bean3);
        list.add (bean4);
        for ( MessageDaoModel messageDaoModel : list )
        {
            MessageDao.newInstance (this).saveMessage (messageDaoModel);
        }
        loadDataFromDB();
    }

    /**
     *@Description: 初始化uiview 
     *@Author: 李焱生
     *@Since: 2015年4月9日上午10:02:20
     */
    private void initUIView(){
        // 1.设置布局文件
        setContentView (R.layout.message_list_activity);

        // 2.左边导航按钮
        msg_list_nav_bar_left_btn = (Button) findViewById (R.id.msg_list_nav_bar_left_btn);

        // 3.下拉刷新列表
        pull_list_view = (PullToRefreshListView) findViewById (R.id.msg_list_view);

        // 4.空试图
        emptyView = new TextView (this);
        emptyView.setLayoutParams (new LayoutParams (LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        emptyView.setTextColor (getResources ().getColor (R.color.color_36));
        emptyView.setTextSize (getResources ().getDimension (R.dimen.tSize20));
        emptyView.setGravity (Gravity.CENTER);
        emptyView.setText ("暂无消息");
        emptyView.setVisibility (View.GONE);

        // 5.listview
        listView = pull_list_view.getRefreshableView ();
        ((ViewGroup) listView.getParent ()).addView (emptyView);
        listView.setEmptyView (emptyView);
        listView.setDivider (this.getResources ().getDrawable (R.drawable.seperator_line_selector));
        listView.setDividerHeight (CommonUtils.dip2px (this, 1.f));

        // 6.adapter
        dataList = new ArrayList<MessageDaoModel> ();
        adapter = new MessageListAdapter (this,dataList,R.layout.msg_list_item_activity);
        listView.setAdapter (adapter);
        pull_list_view.setPullLoadEnabled (true);
        pull_list_view.setPullRefreshEnabled (true);
    }

}
