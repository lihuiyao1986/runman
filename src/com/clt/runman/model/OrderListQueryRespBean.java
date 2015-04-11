package com.clt.runman.model;



/**
 * 订单查询响应bean
 * @author yanshengli
 * @since 2015-3-11
 */
public class OrderListQueryRespBean extends BaseRespBean 
{
	private OrderListQueryBean orderPagination;

	public OrderListQueryBean getOrderPagination() 
	{
		return orderPagination;
	}
	
	public void setOrderPagination(OrderListQueryBean orderPagination) 
	{
		this.orderPagination = orderPagination;
	}
}
