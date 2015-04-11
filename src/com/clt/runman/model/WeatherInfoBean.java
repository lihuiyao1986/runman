package com.clt.runman.model;

/**
 * 天气信息bean
 * @author yanshengli
 * @since 2015-3-2
 */
public class WeatherInfoBean 
{
	//日期
	private String date;
	//白天天气的图片
	private String dayPictureUrl;
	//晚上天气的图片
	private String nightPictureUrl;
	//天气描述
	private String weather;
	//风向描述
	private String wind;
	//温度描述
	private String temperature;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDayPictureUrl() {
		return dayPictureUrl;
	}
	public void setDayPictureUrl(String dayPictureUrl) {
		this.dayPictureUrl = dayPictureUrl;
	}
	public String getNightPictureUrl() {
		return nightPictureUrl;
	}
	public void setNightPictureUrl(String nightPictureUrl) {
		this.nightPictureUrl = nightPictureUrl;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

}
