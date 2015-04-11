package com.clt.runman.model;

import java.util.List;

public class WeatherInfoResultBean {

	//当前城市
	private String currentCity;
	
	//pm2.5指数
	private String pm25;
	
	//天气信息
	private List<WeatherInfoBean>weather_data;
	
	//index
	private List<WeatherIndexInfo>index;

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public List<WeatherInfoBean> getWeather_data() {
		return weather_data;
	}

	public void setWeather_data(List<WeatherInfoBean> weather_data) {
		this.weather_data = weather_data;
	}

	public List<WeatherIndexInfo> getIndex() {
		return index;
	}

	public void setIndex(List<WeatherIndexInfo> index) {
		this.index = index;
	}
	
	
}
