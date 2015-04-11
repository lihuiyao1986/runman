package com.clt.runman.model;

/**
 * 订单收入查询汇总
 * @author yanshengli
 * @since 2015-3-9
 */
public class OrderIncomeQueryTotalBean extends BaseOrderIncomeQueryItemBean
{
	
	/** 月份 **/
	private String orderTotalMonth;
	
	/** 数量 **/
	private String orderTotalNum;
	
	/** 金额 **/
	private String orderTotalSum;
	
	/** 索引 **/
	private int index;

	public String getOrderTotalMonth() {
		return orderTotalMonth;
	}

	public void setOrderTotalMonth(String orderTotalMonth) {
		this.orderTotalMonth = orderTotalMonth;
	}

	public String getOrderTotalNum() {
		return orderTotalNum;
	}

	public void setOrderTotalNum(String orderTotalNum) {
		this.orderTotalNum = orderTotalNum;
	}

	public String getOrderTotalSum() {
		return orderTotalSum;
	}

	public void setOrderTotalSum(String orderTotalSum) {
		this.orderTotalSum = orderTotalSum;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
