package com.clt.runman.push.model;

import java.io.Serializable;

/**
 * 解析个推传消息结果实体类
 * @author yanshengli
 * @since 2015-1-31
 */
public class BaseGexinPayLoadBean implements Serializable
{
	private static final long serialVersionUID = -2300860308456307527L;
	
	/** 时间 **/
	private String createTime;
	/** taskid **/
	private String taskid;
	/** messageid **/
	private String messageid;
	/** 类型 **/
	private String type;
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getMessageid() {
		return messageid;
	}
	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
