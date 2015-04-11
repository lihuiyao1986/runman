package com.clt.runman.model;



/**
 * 体验券信息
 * @author yanshengli
 * @since 2015-3-12
 */
public class Voucher 
{
	/** 体验券发放记录编号*/
    private String experiencevoucherId;
    /** 编号*/
    private String id;
    /** 类型 0体验券1抵价券*/
    private String type;
    /** 名称*/
    private String name;
    /** 状态 0:正常 1：失效 2已使用*/
    private String status;
    /** 所有者类型 0跑男1用户*/
    private String ownerType;
    /** 所有者*/
    private String owner;
    
	public String getExperiencevoucherId() {
		return experiencevoucherId;
	}
	public void setExperiencevoucherId(String experiencevoucherId) {
		this.experiencevoucherId = experiencevoucherId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
