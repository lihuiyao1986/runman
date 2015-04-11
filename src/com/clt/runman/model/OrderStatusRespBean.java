package com.clt.runman.model;

import com.clt.runman.db.model.OrderDaoModel;

/**
 * 订单对象响应bean
 * @author yanshengli
 * @since 2015-1-29
 */
public class OrderStatusRespBean extends BaseRespBean {
    
	/** 订单对象 **/
    private OrderDaoModel order;

	public OrderDaoModel getOrder() {
		return order;
	}

	public void setOrder(OrderDaoModel order) {
		this.order = order;
	}

}
