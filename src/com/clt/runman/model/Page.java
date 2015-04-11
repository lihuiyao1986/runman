package com.clt.runman.model;

import java.io.Serializable;

public class Page implements Serializable
{
	private static final long serialVersionUID = -5467331218774228122L;

	/**
	 * 页码，第几页的数据，从1开始。（大于0的整数。默认为1）<br />
	 * 支持最大值为：2147483647<br />
	 * 支持最小值为：-21474836478
	 */
	private int pageNo = 1;

	/**
	 * 每页条数。（每页条数不超过50条）<br />
	 * 支持最大值为：2147483647<br />
	 * 支持最小值为：-21474836478
	 */
	private int pageSize = 10;

	/**
	 * 总记录数。<br />
	 * 支持最大值为：2147483647<br />
	 * 支持最小值为：-21474836478
	 */
	private int totalCount;

	/**
	 * 总页数。
	 */
	private int pageCount;

	/**
	 * 是否分页
	 */
	private boolean isPaging = false;

	/**
	 * 要排序的字段名（需是数据库表中的属性名称）
	 */
	private String sortName;

	/**
	 * 排序，为空不排序，desc倒序，asc顺序
	 */
	private String sortOrder;
	
	public int getPageNo() 
	{
		return pageNo;
	}

	public void setPageNo(int pageNo)
	{
		this.pageNo = pageNo;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize) 
	{
		this.pageSize = pageSize;
	}

	public int getTotalCount() 
	{
		return totalCount;
	}

	public void setTotalCount(int totalCount) 
	{
		this.totalCount = totalCount;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(int pageCount) 
	{
		this.pageCount = pageCount;
	}

	public boolean isPaging()
	{
		return isPaging;
	}

	public void setPaging(boolean isPaging)
	{
		this.isPaging = isPaging;
	}

	public String getSortName()
	{
		return sortName;
	}

	public void setSortName(String sortName) 
	{
		this.sortName = sortName;
	}

	public String getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) 
	{
		this.sortOrder = sortOrder;
	}
}
