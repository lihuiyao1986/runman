package com.clt.runman.model;

/**
 * 收入统计查询响应的bean
 * @author yanshengli
 * @since 2015-3-11
 */
public class IncomeListQueryRespBean extends BaseRespBean
{
	/** 订单统计 **/
	private OrderStatistic orderStatistics;

	public OrderStatistic getOrderStatistics() {
		return orderStatistics;
	}

	public void setOrderStatistics(OrderStatistic orderStatistics) {
		this.orderStatistics = orderStatistics;
	}
}
