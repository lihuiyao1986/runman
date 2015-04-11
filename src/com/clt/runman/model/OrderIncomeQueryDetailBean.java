package com.clt.runman.model;

/**
 * 收入统计查询
 * @author yanshengli
 *
 */
public class OrderIncomeQueryDetailBean extends BaseOrderIncomeQueryItemBean
{
	/** 订单数量 **/
	private String orderNum;
	
	/** 订单收入 **/
	private String orderSum;
	
	/** 订单日期 **/
	private String orderDate;

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOrderSum() {
		return orderSum;
	}

	public void setOrderSum(String orderSum) {
		this.orderSum = orderSum;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	
}
