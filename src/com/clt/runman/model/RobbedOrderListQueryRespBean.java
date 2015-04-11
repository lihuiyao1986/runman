package com.clt.runman.model;

import java.util.List;

/**
 * 已抢订单列表查询
 * @author yanshengli
 * @since 2015-3-31
 */
public class RobbedOrderListQueryRespBean extends BaseRespBean 
{
	private List<RobOrderDetailBean> orders;

	public List<RobOrderDetailBean> getOrders() {
		return orders;
	}

	public void setOrders(List<RobOrderDetailBean> orders) {
		this.orders = orders;
	}

}
