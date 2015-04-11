package com.clt.runman.db.model;

import net.tsz.afinal.annotation.sqlite.Id;

/**
 * 订单
 * @author yanshengli
 * @since 2015-1-27
 */
public class OrderDaoModel extends BaseDaoModel {

    private static final long serialVersionUID = -4806458446309510062L;

    @Id
    protected long            id;

    // 订单编号
    private String            orderId;

    // 下单时间
    //private String            orderTime;

    // 用户手机
    private String            phone;

    // 车辆编号
    private String            carId;

    // 车牌号
    private String            carPlateNum;

    // 车辆经度
    private String            carLongitude;

    // 车辆维度
    private String            carLatitude;

    // 创建时间
    private String            createTime;

    // 用户经度
    //private String            userLongitude;

    // 用户纬度
    //private String            userLatitude;

    // 订单地址
    private String            address;

    // 车辆品牌
    private String            carBrand;

    // 车辆所属系列
    private String            carSeries;

    // 车颜色
    private String            carColor;

    /**
     * 订单状态 已接单 2000 已到达 2010 找到车 2020 洗前拍照完成2030 洗车中2040 洗车完成2050 洗后拍照完成3000
     */
    private String            status;

    // 支付方式:0现金;1体验券;2微信支付
    private String            payType;

    // 订单金额
    private String            orderAmt;

    // 订单确认时间
    //private String            confirmTime;

    // 订单撤销时间
    //private String            cancelTime;
    
    // 订单完成时间
    private String            finishTime;

    // 备注
    private String            comment;
    
    // 订单预约开始时间
    private String serviceBeginTime;
    
    // 订单预约结束时间
    private String serviceEndTime;
    
    // 期望服务时间
    private String orderHopeTime;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getOrderId(){
        return orderId;
    }

    public void setOrderId(String orderId){
        this.orderId = orderId;
    }

    //    public String getOrderTime(){
    //        return orderTime;
    //    }
    //
    //    public void setOrderTime(String orderTime){
    //        this.orderTime = orderTime;
    //    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getCarId(){
        return carId;
    }

    public void setCarId(String carId){
        this.carId = carId;
    }

    public String getCarPlateNum(){
        return carPlateNum;
    }

    public void setCarPlateNum(String carPlateNum){
        this.carPlateNum = carPlateNum;
    }

    public String getCarLongitude(){
        return carLongitude;
    }

    public void setCarLongitude(String carLongitude){
        this.carLongitude = carLongitude;
    }

    public String getCarLatitude(){
        return carLatitude;
    }

    public void setCarLatitude(String carLatitude){
        this.carLatitude = carLatitude;
    }

    public String getCreateTime(){
        return createTime;
    }

    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }

    //    public String getUserLongitude(){
    //        return userLongitude;
    //    }
    //
    //    public void setUserLongitude(String userLongitude){
    //        this.userLongitude = userLongitude;
    //    }
    //
    //    public String getUserLatitude(){
    //        return userLatitude;
    //    }
    //
    //    public void setUserLatitude(String userLatitude){
    //        this.userLatitude = userLatitude;
    //    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getCarBrand(){
        return carBrand;
    }

    public void setCarBrand(String carBrand){
        this.carBrand = carBrand;
    }

    public String getCarSeries(){
        return carSeries;
    }

    public void setCarSeries(String carSeries){
        this.carSeries = carSeries;
    }

    public String getCarColor(){
        return carColor;
    }

    public void setCarColor(String carColor){
        this.carColor = carColor;
    }

    public String getPayType(){
        return payType;
    }

    public void setPayType(String payType){
        this.payType = payType;
    }

    public String getOrderAmt(){
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt){
        this.orderAmt = orderAmt;
    }

    //    public String getConfirmTime(){
    //        return confirmTime;
    //    }
    //
    //    public void setConfirmTime(String confirmTime){
    //        this.confirmTime = confirmTime;
    //    }
    //
    //    public String getCancelTime(){
    //        return cancelTime;
    //    }
    //
    //    public void setCancelTime(String cancelTime){
    //        this.cancelTime = cancelTime;
    //    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getServiceBeginTime() {
		return serviceBeginTime;
	}

	public void setServiceBeginTime(String serviceBeginTime) {
		this.serviceBeginTime = serviceBeginTime;
	}

	public String getServiceEndTime() {
		return serviceEndTime;
	}

	public void setServiceEndTime(String serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}

	public String getOrderHopeTime() {
		return orderHopeTime;
	}

	public void setOrderHopeTime(String orderHopeTime) {
		this.orderHopeTime = orderHopeTime;
	}

}
