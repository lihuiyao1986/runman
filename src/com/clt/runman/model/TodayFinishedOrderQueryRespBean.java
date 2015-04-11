package com.clt.runman.model;

/**
 * 今日完成订单查询响应类
 * @author yanshengli
 * @since 2015-3-10
 */
public class TodayFinishedOrderQueryRespBean extends BaseRespBean 
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
