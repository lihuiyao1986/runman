package com.clt.runman.model;

import java.util.List;

import com.clt.runman.db.model.OrderDaoModel;

/**
 * 订单查询
 * @author yanshengli
 * @since 2015-3-11
 */
public class OrderListQueryBean extends Page
{
	private static final long serialVersionUID = -838220268333377824L;
	
	private List<OrderDaoModel> items;

	public List<OrderDaoModel> getItems() {
		return items;
	}

	public void setItems(List<OrderDaoModel> items) {
		this.items = items;
	}
}
