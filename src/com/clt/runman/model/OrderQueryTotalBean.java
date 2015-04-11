package com.clt.runman.model;

/**
 * 订单查询汇总
 * @author yanshengli
 *
 */
public class OrderQueryTotalBean extends BaseOrderQueryItemBean
{

	private static final long serialVersionUID = 2848232546221350628L;

	/** 日期 **/
	private String date;
	
	/** 总单数 **/
	private String totalNum;
	
	/** 总收入 **/
	private String totalIncome;
	
	/** 索引 **/
	private int index;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(String totalIncome) {
		this.totalIncome = totalIncome;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
