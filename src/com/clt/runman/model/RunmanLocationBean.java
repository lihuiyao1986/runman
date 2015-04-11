package com.clt.runman.model;

/**
 * 跑男的地理位置信息
 * @author yanshengli
 * @since 2015-3-2
 */
public class RunmanLocationBean 
{
    /** 跑男位置的经度 **/
    private  String   longitude;
    
    /** 跑男位置的纬度 **/
    private  String   latitude;
    
    /** 跑男的地理位置 **/
    private  String   address;
    
    /** 跑男的id **/
    private  String   runmanId;
    
    /** 保存的时间 **/
    private  String   createtime;
    
    /** 城市 **/
    private  String   city;
    
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRunmanId() {
		return runmanId;
	}
	public void setRunmanId(String runmanId) {
		this.runmanId = runmanId;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
    
    @Override
    public String toString() {
    	return "longitude=" + longitude 
    			+ ",latitude=" + latitude
    			+ ",address=" + address 
    			+ ",runmanId=" + runmanId
    			+ ",createtime=" + createtime;
    }
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
