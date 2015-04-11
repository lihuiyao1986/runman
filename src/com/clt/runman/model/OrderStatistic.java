package com.clt.runman.model;

import java.util.List;

/**
 * 订单统计
 * @author yanshengli
 * @since 2015-3-10
 */
public class OrderStatistic 
{
	 /** 统计日期刻度*/ 
    private String date;
    
    /** 统计日期刻度订单数*/ 
    private String dateOrderNum;
    
    /** 统计日期刻度订单金额*/ 
    private String dateOrderAmt;
    
    /** 明晰列表 **/
    private List<OrderStatistic> lowLevelOrderStatisticsList;
    
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDateOrderNum() {
		return dateOrderNum;
	}
	public void setDateOrderNum(String dateOrderNum) {
		this.dateOrderNum = dateOrderNum;
	}
	public String getDateOrderAmt() {
		return dateOrderAmt;
	}
	public void setDateOrderAmt(String dateOrderAmt) {
		this.dateOrderAmt = dateOrderAmt;
	}
	public List<OrderStatistic> getLowLevelOrderStatisticsList() {
		return lowLevelOrderStatisticsList;
	}
	public void setLowLevelOrderStatisticsList(
			List<OrderStatistic> lowLevelOrderStatisticsList) {
		this.lowLevelOrderStatisticsList = lowLevelOrderStatisticsList;
	}
    
    
}
