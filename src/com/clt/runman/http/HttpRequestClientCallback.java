package com.clt.runman.http;

import com.clt.runman.model.BaseRespBean;

/**
 * 请求回调
 * @author yanshengli
 * @since 2015-1-28
 */
public interface HttpRequestClientCallback {
	
	/**
	 * 请求成功回调
	 * @param result
	 * @param reqTag
	 * @param clazz
	 */
	public void httpRespSuccess(BaseRespBean result,int reqTag,Class<?> clazz);
	
	/**
	 * 请求失败
	 * @param errorcode
	 * @param errorMsg
	 * @param reqTag
	 */
	public void httpRespFail(String errorcode,String errorMsg,int reqTag);
	
	/**
	 * http请求开始
	 */
	public void httpReqBegin(int reqTag);
	
	/**
	 * 加载进度
	 * @param progress
	 * @param rate
	 * @param reqTag
	 */
	public void httpOnProgress(boolean progress, int rate,int reqTag);
	
	/***
	 * http正在加载
	 * @param count
	 * @param current
	 */
	public void httpOnloading(long count, long current,int reqTag);

}
