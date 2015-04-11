package com.clt.runman.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.model.OrderIncomeQueryDetailBean;
import com.clt.runman.utils.StringUtils;

/**
 * 收入查询对应的适配器类
 * @author yanshengli
 * @since 2015-3-10
 */
public class IncomeQueryAdapter extends RunManBaseAdapter<OrderIncomeQueryDetailBean>
{

	public IncomeQueryAdapter(Context context,List<OrderIncomeQueryDetailBean> data, int resource) 
	{
		super(context, data, resource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		HolderView holderView = null;
		OrderIncomeQueryDetailBean item = listData.get(position);
		if(convertView == null)
		{
			convertView = listContainer.inflate(itemViewResource, null);
			holderView = new HolderView();
			holderView.orderDate = (TextView)convertView.findViewById(R.id.income_query_item_listview_date_tv);
			holderView.orderNum = (TextView)convertView.findViewById(R.id.income_query_item_listview_orderNum_tv);
			holderView.orderSum = (TextView)convertView.findViewById(R.id.income_query_item_listview_orderSum_tv);
			convertView.setTag(holderView);
		}
		else
		{
			holderView = (HolderView) convertView.getTag(); 
		}
		displayView(item,holderView);
		return convertView;
	}
	
	/**
	 * 展示视图
	 * @param item
	 * @param holderView
	 */
	private void displayView(OrderIncomeQueryDetailBean item,HolderView holderView)
	{
		holderView.orderDate.setText(StringUtils.trimNull(item.getOrderDate(), "--"));
		holderView.orderNum.setText(StringUtils.trimNull(item.getOrderNum(), "--"));
		holderView.orderSum.setText(StringUtils.trimNull(item.getOrderSum(), "--"));
	}

	/**
	 * 
	 * @author yanshengli
	 * @since 2015-3-10
	 */
    private class HolderView
    {
    	public TextView orderDate;
    	public TextView orderNum;
    	public TextView orderSum;
    }

}
