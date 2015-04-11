package com.clt.runman.model;

/**
 * 订单查询详情
 * @author yanshengli
 * @since 2015-3-6
 */
public class OrderQueryDetailBean extends BaseOrderQueryItemBean {

    private static final long serialVersionUID = -2792558139660208609L;

    // 车牌号
    private String            carNum;

    // 车辆位置
    private String            carPosition;

    // 支付方式
    private String            payway;

    // 支付方式描述
    private String            paywayStr;

    // 下单时间
    private String            orderTime;

    // 订单状态
    private String            orderStatus;

    // 订单金额
    private String            orderSum;

    // 原始的订单时间
    //private String            originalOrderDate;

    // 订单状态描述
    private String            orderStatusStr;

    // 预约时间
    private String            subscribeTime;

    // 原始确认时间 格式为yyyyMMddHHmmss
    private String            confirmTime;

    // 订单号
    private String            orderId;

    // 手机号
    private String            phone;

    // 距离
    private String            distance;

    public OrderQueryDetailBean() {}

    public OrderQueryDetailBean(String carNum, String carPosition, String distance, String subscribeTime) {
        this.carNum = carNum;
        this.carPosition = carPosition;
        this.distance = distance;
        this.subscribeTime = subscribeTime;
    }

    public String getCarNum(){
        return carNum;
    }

    public void setCarNum(String carNum){
        this.carNum = carNum;
    }

    public String getCarPosition(){
        return carPosition;
    }

    public void setCarPosition(String carPosition){
        this.carPosition = carPosition;
    }

    public String getPayway(){
        return payway;
    }

    public void setPayway(String payway){
        this.payway = payway;
    }

    public String getOrderTime(){
        return orderTime;
    }

    public void setOrderTime(String orderTime){
        this.orderTime = orderTime;
    }

    public String getOrderStatus(){
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus){
        this.orderStatus = orderStatus;
    }

    public String getOrderSum(){
        return orderSum;
    }

    public void setOrderSum(String orderSum){
        this.orderSum = orderSum;
    }

    //    public String getOriginalOrderDate(){
    //    	return originalOrderDate;
    //    }
    //
    //    public void setOriginalOrderDate(String originalOrderDate){
    //    	this.originalOrderDate = originalOrderDate;
    //    }

    public String getOrderStatusStr(){
        return orderStatusStr;
    }

    public void setOrderStatusStr(String orderStatusStr){
        this.orderStatusStr = orderStatusStr;
    }

    public String getPaywayStr(){
        return paywayStr;
    }

    public void setPaywayStr(String paywayStr){
        this.paywayStr = paywayStr;
    }

    public String getSubscribeTime(){
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime){
        this.subscribeTime = subscribeTime;
    }

    public String getOrderId(){
        return orderId;
    }

    public void setOrderId(String orderId){
        this.orderId = orderId;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getConfirmTime(){
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime){
        this.confirmTime = confirmTime;
    }

    public String getDistance(){
        return distance;
    }

    public void setDistance(String distance){
        this.distance = distance;
    }

}
