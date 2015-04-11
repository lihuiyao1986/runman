package com.clt.runman.model;

import java.util.List;

/**
 * 已抢单列表查询
 * @author yanshengli
 * @since 2015-3-31
 */
public class RobbedOrderListQueryBean
{
	private List<RobOrderDetailBean> items;

	public List<RobOrderDetailBean> getItems()
	{
		return items;
	}

	public void setItems(List<RobOrderDetailBean> items) 
	{
		this.items = items;
	}
}
