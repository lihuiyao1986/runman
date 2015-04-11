package com.clt.runman.model;

import java.io.Serializable;


/**
 * Created by yanshengli on 15-1-22.
 * 推送过来的订单信息
 * @author liys
 * @since 2015-1-30
 */
public class PushedOrderInfo implements Serializable {

    private static final long serialVersionUID = -6287656135006880026L;
    
    // 距离
    private String            distance;
    // 车牌
    private String            carNum;
    // 停车位置
    private String            carPosition;
    // 订单编号
    private String            orderId;
    // 返送时间
    private String            dispatchTime;

    public String getDistance(){
        return distance;
    }

    public void setDistance(String distance){
        this.distance = distance;
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

    public String getOrderId(){
        return orderId;
    }

    public void setOrderId(String orderId){
        this.orderId = orderId;
    }

	public String getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(String dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("distance = " + distance + ",");
		sb.append("orderId = " + orderId + ",");
		sb.append("dispatchTime = " + dispatchTime + ",");
		sb.append("carNum = " + carNum + ",");
		sb.append("carPosition = " + carPosition + ",");
		return sb.toString();
	}
}
