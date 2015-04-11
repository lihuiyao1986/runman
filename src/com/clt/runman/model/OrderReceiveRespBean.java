package com.clt.runman.model;

import com.clt.runman.db.model.OrderDaoModel;


/**
 * 接单接口响应的bean
 * @author yanshengli
 *
 */
public class OrderReceiveRespBean extends BaseRespBean 
{
	
	/** 订单对象 **/
	private OrderDaoModel order;

	public OrderDaoModel getOrder() {
		return order;
	}

	public void setOrder(OrderDaoModel order) {
		this.order = order;
	}

}
