package com.clt.runman.push.model;

/**
 * 订单派送消息推送bean
 * @author yanshengli
 * @since 2015-3-31
 */
public class OrderDispatchPayLoadBean extends BaseGexinPayLoadBean 
{
	private static final long serialVersionUID = -3771971003323125767L;
    
	/** 派单时间 **/
	private String dispatchTime;
	
	/** 车牌 **/
	private String carPlatenum;
	
	/** 车辆位置 **/
	private String orderAddress;
	
	/** 车辆距离 **/
	private String distance;
	
	/** 订单号 **/
	private String orderId;
	
	/** 预约时间 **/
	private String bookingTime;
	
	/** 预期起始服务时间 **/
	private String serviceEndTime;
	
	/** 预期结束服务时间 **/
	private String serviceBeginTime;

	public String getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(String dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public String getCarPlatenum() {
		return carPlatenum;
	}

	public void setCarPlatenum(String carPlatenum) {
		this.carPlatenum = carPlatenum;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}

	public String getServiceEndTime() {
		return serviceEndTime;
	}

	public void setServiceEndTime(String serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}

	public String getServiceBeginTime() {
		return serviceBeginTime;
	}

	public void setServiceBeginTime(String serviceBeginTime) {
		this.serviceBeginTime = serviceBeginTime;
	}
}
