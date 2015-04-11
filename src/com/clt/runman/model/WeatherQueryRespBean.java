package com.clt.runman.model;

import java.util.List;


/**
 * 请求天气的响应bean
 * @author yanshengli
 * @since 2015-3-2
 */
public class WeatherQueryRespBean 
{
	//错误码
	private String error;
	//状态
	private String status;
	//日期
	private String date;
	//天气数据
	private List<WeatherInfoResultBean> results;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<WeatherInfoResultBean> getResults() {
		return results;
	}
	public void setResults(List<WeatherInfoResultBean> results) {
		this.results = results;
	}
	
}
