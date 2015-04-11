package com.clt.runman.model;

/**
 * 收入统计查询的bean
 * @author yanshengli
 * @since 2015-3-9
 */
public class BaseOrderIncomeQueryItemBean 
{
	// 汇总
	public static final int TOTAL = 0;

	// 详情
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
