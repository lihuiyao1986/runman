package com.clt.runman.model;

import java.io.Serializable;

/**
 * 抢单返回的字段
 * @author yanshengli
 * @since 2015-3-31
 */
public class RobOrderDetailBean implements Serializable {

    private static final long serialVersionUID = 4458955265087173009L;

    // 订单号
    private String            orderId;

    // 车牌号
    private String            carPlateNum;

    // 停车点
    private String            address;

    // 预期服务时间
    private String            orderHopeTime;

    // 预期服务结束时间
    private String            serviceEndTime;

    // 预期服务开始时间
    private String            serviceBeginTime;

    // 距离
    private String            distance;

    // 订单状态
    private int               status;

    // 支付类型
    private String            payType;

    // 车主号码
    private String            phone;

    public String getOrderId(){
        return orderId;
    }

    public void setOrderId(String orderId){
        this.orderId = orderId;
    }

    public String getCarPlateNum(){
        return carPlateNum;
    }

    public void setCarPlateNum(String carPlateNum){
        this.carPlateNum = carPlateNum;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getOrderHopeTime(){
        return orderHopeTime;
    }

    public void setOrderHopeTime(String orderHopeTime){
        this.orderHopeTime = orderHopeTime;
    }

    public String getServiceEndTime(){
        return serviceEndTime;
    }

    public void setServiceEndTime(String serviceEndTime){
        this.serviceEndTime = serviceEndTime;
    }

    public String getServiceBeginTime(){
        return serviceBeginTime;
    }

    public void setServiceBeginTime(String serviceBeginTime){
        this.serviceBeginTime = serviceBeginTime;
    }

    public String getDistance(){
        return distance;
    }

    public void setDistance(String distance){
        this.distance = distance;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public int getStatus(){
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public String getPayType(){
        return payType;
    }

    public void setPayType(String payType){
        this.payType = payType;
    }

}
