package com.clt.runman.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

import com.clt.runman.R;
import com.clt.runman.model.BaseOrderQueryItemBean;
import com.clt.runman.model.OrderQueryDetailBean;
import com.clt.runman.model.OrderQueryTotalBean;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.widget.pinnedsectionlistview.PinnedSectionListView.PinnedSectionListAdapter;

/**
 * 订单查询的adapter
 * 
 * @author yanshengli
 * @since 2015-3-6
 */
public class OrderQueryAdapter extends BaseAdapter implements PinnedSectionListAdapter
{

	// 上下文对象
	private Context context;

	// 订单item对象
	private List<BaseOrderQueryItemBean> dataList;

	// 视图容器
	private LayoutInflater listContainer;

	// 标题布局文件
	private int headerLayout;

	// 详情布局文件
	private int detailLayout;
	
	@SuppressLint("SimpleDateFormat")
	private DateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@SuppressLint("SimpleDateFormat")
	private DateFormat format2 = new SimpleDateFormat("HH:mm");

	@Override
	public int getCount() 
	{
		return dataList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return dataList.get(position);
	}

	/**
	 * 构造函数
	 * 
	 * @param ctx
	 * @param dataList
	 * @param headerLayout
	 * @param detailLayout
	 */
	public OrderQueryAdapter(Context ctx,List<BaseOrderQueryItemBean> dataList, int headerLayout,int detailLayout)
	{
		this.context = ctx;
		setDataList(dataList);
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.headerLayout = headerLayout;
		this.detailLayout = detailLayout;
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		BaseOrderQueryItemBean item = dataList.get(position);
		if (item.getType() == BaseOrderQueryItemBean.TOTAL) 
		{
			return handleTotal(item, convertView, parent);
		} 
		else if (item.getType() == BaseOrderQueryItemBean.DETAIL)
		{
			return handleDetail(item, convertView, parent);
		}
		else
		{
			return null;
		}
	}

	/**
	 * 单个
	 * @param item
	 * @param convertView
	 * @param parent
	 */
	private View handleDetail(BaseOrderQueryItemBean item, View convertView,ViewGroup parent)
	{
		DetailViewHolder holder = null;
		if (convertView == null) 
		{
			holder = new DetailViewHolder();
			convertView = listContainer.inflate(detailLayout, null);
			holder.carNumTv = (TextView) convertView.findViewById(R.id.order_query_detail_carNum_tv);
			holder.carPositionTv = (TextView) convertView.findViewById(R.id.order_query_detail_carPosition_tv);
			holder.orderStatusTv = (TextView) convertView.findViewById(R.id.order_query_detail_orderstatus_tv);
			holder.orderSumTv = (TextView) convertView.findViewById(R.id.order_query_detail_sum_tv);
			holder.orderTimeTv = (TextView) convertView.findViewById(R.id.order_query_detail_order_time_tv);
			holder.paywayTv = (TextView) convertView.findViewById(R.id.order_query_detail_payway_tv);
			holder.orderTimeDescTv = (TextView) convertView.findViewById(R.id.order_query_detail_ordertime_desc_tv);
			convertView.setTag(holder);
		} 
		else
		{
			holder = (DetailViewHolder) convertView.getTag();
		}

		// 展示详情
		displayDetail(item, holder);
		
		return convertView;
	}
	
	/**
	 * 转换时间
	 * @param confirmTime
	 * @return
	 */
	private String convertConfirmTime(String confirmTime)
	{
		String result = "";
		if(!StringUtils.isEmpty(confirmTime))
		{
			try 
			{
				return format2.format(format1.parse(confirmTime));
			} 
			catch (ParseException e) 
			{
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 展示单个
	 * 
	 * @param item
	 * @param holder
	 */
	private void displayDetail(BaseOrderQueryItemBean item,DetailViewHolder holder) 
	{
		OrderQueryDetailBean bean = (OrderQueryDetailBean) item;
		holder.carNumTv.setText(StringUtils.trimNull(bean.getCarNum(), "--"));
		holder.carPositionTv.setText(StringUtils.trimNull(bean.getCarPosition(), "--"));
		holder.orderStatusTv.setText(StringUtils.trimNull(bean.getOrderStatusStr(), "--"));
		holder.orderSumTv.setText(StringUtils.trimNull(bean.getOrderSum(), "--") + "元");
		holder.orderTimeTv.setText(StringUtils.trimNull(convertConfirmTime(bean.getConfirmTime()), "--"));
		holder.paywayTv.setText(StringUtils.trimNull(bean.getPaywayStr(), "--"));
		holder.orderStatusTv.setVisibility(View.VISIBLE);
		String payway = StringUtils.trimNull(bean.getPayway());
		//订单状态
		String status = StringUtils.trimNull(bean.getOrderStatus());
		if("5000".equals(status))
		{
			holder.orderTimeDescTv.setText("撤单时间");
		}
		else if("3000".equals(status))
		{
			holder.orderTimeDescTv.setText("完成时间");
		}
		else if("4000".equals(status))
		{
			holder.orderTimeDescTv.setText("到账时间");
			//如果是体验券支付，则不显示订单状态
			if("1".equals(payway))
			{
				holder.orderStatusTv.setVisibility(View.GONE);
				holder.orderSumTv.setText("0.00元");
				holder.orderTimeDescTv.setText("使用时间");
			}
		}
	}
	

	/**
	 * 展示汇总
	 * @param item
	 * @param convertView
	 * @param parent
	 */
	private View handleTotal(BaseOrderQueryItemBean item, View convertView,ViewGroup parent) {
		HeaderViewHolder holder = null;
		if (convertView == null) 
		{
			convertView = listContainer.inflate(headerLayout, null);
			holder = new HeaderViewHolder();
			holder.dateTv = (TextView) convertView.findViewById(R.id.order_query_total_date_tv);
			//holder.orderNumTv = (TextView) convertView.findViewById(R.id.order_query_total_num_tv);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (HeaderViewHolder) convertView.getTag();
		}

		// 展示统计
		displayTotal(item, holder);
		
		//返回
		convertView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, CommonUtils.dip2px(context, 40)));
		return convertView;
	}

	/**
	 * 展示
	 * 
	 * @param item
	 * @param holder
	 */
	private void displayTotal(BaseOrderQueryItemBean item,HeaderViewHolder holder) 
	{
		OrderQueryTotalBean bean = (OrderQueryTotalBean) item;
		holder.dateTv.setText(StringUtils.trimNull(bean.getDate(), "--"));
		//holder.orderNumTv.setText(StringUtils.trimNull(bean.getTotalNum(), "--") + "单");
	}

	/**
	 * 头部视图
	 * 
	 * @author yanshengli
	 * @since 2015-3-6
	 */
	static class HeaderViewHolder
	{
		TextView dateTv;
		//TextView orderNumTv;
		//TextView orderTotalIncomeTv;
	}

	/**
	 * DetailViewHolder
	 * 
	 * @author yanshengli
	 * @since 2015-3-6
	 */
	static class DetailViewHolder
	{
		TextView carNumTv;
		TextView orderTimeTv;
		TextView orderTimeDescTv;
		TextView orderStatusTv;
		TextView carPositionTv;
		TextView paywayTv;
		TextView orderSumTv;
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == BaseOrderQueryItemBean.TOTAL;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return ((BaseOrderQueryItemBean) getItem(position)).getType();
	}

	/**
	 * 设置datalist
	 * @param dataList
	 */
	public void setDataList(List<BaseOrderQueryItemBean> dataList) {
		this.dataList = dataList;
		if (dataList != null) {
			this.dataList = dataList;
		} else {
			this.dataList = new ArrayList<BaseOrderQueryItemBean>();
		}
	}

	/**
	 * 刷新
	 * @param dataList2
	 */
	public void refresh(List<BaseOrderQueryItemBean> data) {
		setDataList(data);
		notifyDataSetChanged();
	}

}
