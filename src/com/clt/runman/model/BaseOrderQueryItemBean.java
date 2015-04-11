package com.clt.runman.model;

import java.io.Serializable;

/**
 * 订单查询信息
 * @author yanshengli
 * @since 2015-3-6
 */
public class BaseOrderQueryItemBean implements Serializable
{
	private static final long serialVersionUID = 2527487612948084100L;

	//汇总
	public static final int TOTAL = 0;
	
	//详情
	public static final int DETAIL = 1;
	
	/** 类型 **/
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
