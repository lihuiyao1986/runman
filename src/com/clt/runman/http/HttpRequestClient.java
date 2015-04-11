package com.clt.runman.http;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.clt.runman.R;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.ReqHeaderBean;
import com.clt.runman.utils.JsonUtils;
import com.clt.runman.utils.MyLog;
import com.clt.runman.utils.StringUtils;

/**
 * 处理http请求的方法
 * @author yanshengli
 * @since 2015-1-29
 */
public class HttpRequestClient
{
	
	/** 日志TAG **/
	private static final String TAG = "HttpRequestClient";
	
	/** 业务请求成功的代码 **/
	private static final String http_req_business_success_code = "0";
	
	/** http post请求  **/
	public static final String HTTP_POST_REQ = "POST";
	
	/** http get请求  **/
	public static final String HTTP_GET_REQ = "GET";
	
	/**
	 * 发送异步的http请求
	 * @param params
	 * @param url
	 */
	public static void doHttpRequest(Context ctx,List<NameValuePair>params ,String url,final Class<? extends BaseRespBean> clazz,final int reqTag,final HttpRequestClientCallback callback,String httpReqMethod)
	{
		//1.参数
		final AjaxParams reqParams = packPostParams(ctx,params,url);
		
		//2.发送http请求
		FinalHttp fh = new FinalHttp();
		
		//3.设置请求回调
		AjaxCallBack<String> ajaxCallback = new AjaxCallBack<String>() {
			@Override
			public void onLoading(long count, long current)
			{
				super.onLoading(count, current);
				if(callback != null)
				{
					callback.httpOnloading(count, current, reqTag);
				}
			}
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				MyLog.d(TAG, "http请求的参数："+ reqParams.toString() + "," + "http请求的响应结果："+ result);
				if (callback != null) 
				{
					Class<? extends BaseRespBean> resultClazz = clazz == null ? BaseRespBean.class : clazz;
					BaseRespBean resultBean = JsonUtils.parseObject(result, resultClazz);
					String errorcode = StringUtils.trimNull(resultBean.getError().getCode());
					String errorMsg = StringUtils.trimNull(resultBean.getError().getMessage(),"未知错误");
					if(http_req_business_success_code.equalsIgnoreCase(errorcode))
					{
						callback.httpRespSuccess(resultBean, reqTag, resultClazz);
					}
					else
					{
						callback.httpRespFail(errorcode, errorMsg, reqTag);
					}
				}
			}
			@Override
			public AjaxCallBack<String> progress(boolean progress, int rate) 
			{
				if(callback !=null)
				{
					callback.httpOnProgress(progress, rate, reqTag);
				}
				return super.progress(progress, rate);
			}
			
			@Override
			public void onStart() 
			{
				super.onStart();
				if(callback !=null)
				{
					callback.httpReqBegin(reqTag);
				}
			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) 
			{
				super.onFailure(t, errorNo, strMsg);
				if(callback != null)
				{
					callback.httpRespFail(String.valueOf(errorNo), StringUtils.trimNull(strMsg,"未知错误"), reqTag);
				}
			}
		};
		
		//发送请求
		httpReqMethod = StringUtils.trimNull(httpReqMethod, HTTP_POST_REQ);
		if(httpReqMethod.equals(HTTP_POST_REQ))
		{
			fh.post(reqUrl(url,ctx), reqParams,ajaxCallback);
		}
		else
		{
			fh.get(reqUrl(url,ctx), reqParams,ajaxCallback);
		}
	}
	
	
	/**
	 * http request请求
	 * @param params
	 * @param url
	 * @param clazz
	 * @param reqTag
	 * @param callback
	 * @param httpReqMethod
	 */
	public static void doHttpRequest(Context ctx,List<NameValuePair>params ,String url,final Class<? extends BaseRespBean> clazz,final int reqTag,AjaxCallBack<String> ajaxCallback,String httpReqMethod)
	{
		// 1.参数
		AjaxParams reqParams = packPostParams(ctx,params, url);
		MyLog.d(TAG, "http请求的参数：" + reqParams.toString());

		// 2.发送http请求
		FinalHttp fh = new FinalHttp();
		
		// 3.发送请求
		httpReqMethod = StringUtils.trimNull(httpReqMethod, HTTP_POST_REQ);
		if (httpReqMethod.equals(HTTP_POST_REQ)) 
		{
			fh.post(reqUrl(url,ctx), reqParams, ajaxCallback);
		} 
		else 
		{
			fh.get(reqUrl(url,ctx), reqParams, ajaxCallback);
		}
	}
	
	/**
	 * 图片上传接口
	 * @param ctx
	 * @param params
	 * @param url
	 * @param clazz
	 * @param reqTag
	 * @param callback
	 */
	public static void uploadFiles(Context ctx,AjaxParams params, String url ,final Class<? extends BaseRespBean> clazz,final int reqTag,final HttpRequestClientCallback callback)
	{
		//1.参数
		final AjaxParams reqParams = packUploadFileParams(ctx,params,url);

		//2.发送http请求
		FinalHttp fh = new FinalHttp();

		//3.设置请求回调
		AjaxCallBack<String> ajaxCallback = new AjaxCallBack<String>() {
			@Override
			public void onLoading(long count, long current)
			{
				super.onLoading(count, current);
				if(callback != null)
				{
					callback.httpOnloading(count, current, reqTag);
				}
			}
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				MyLog.d(TAG, "http请求的参数："+ reqParams.toString() + "," + "http请求的响应结果："+ result);
				if (callback != null) 
				{
					Class<? extends BaseRespBean> resultClazz = clazz == null ? BaseRespBean.class : clazz;
					BaseRespBean resultBean = JsonUtils.parseObject(result, resultClazz);
					String errorcode = StringUtils.trimNull(resultBean.getError().getCode());
					String errorMsg = StringUtils.trimNull(resultBean.getError().getMessage(),"未知错误");
					if(http_req_business_success_code.equalsIgnoreCase(errorcode))
					{
						callback.httpRespSuccess(resultBean, reqTag, resultClazz);
					}
					else
					{
						callback.httpRespFail(errorcode, errorMsg, reqTag);
					}
				}
			}
			@Override
			public AjaxCallBack<String> progress(boolean progress, int rate) 
			{
				if(callback !=null)
				{
					callback.httpOnProgress(progress, rate, reqTag);
				}
				return super.progress(progress, rate);
			}

			@Override
			public void onStart() 
			{
				super.onStart();
				if(callback !=null)
				{
					callback.httpReqBegin(reqTag);
				}
			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) 
			{
				super.onFailure(t, errorNo, strMsg);
				if(callback != null)
				{
					callback.httpRespFail(String.valueOf(errorNo), StringUtils.trimNull(strMsg,"未知错误"), reqTag);
				}
			}
		};
		fh.post(reqUrl(url,ctx), reqParams,ajaxCallback);
	}
	
	/**
	 * 处理图片上传请求
	 * @param ctx
	 * @param params
	 * @param url
	 * @return
	 */
	private static AjaxParams packUploadFileParams(Context ctx,AjaxParams ajaxParams, String url) 
	{
		//1.传入头信息
		ReqHeaderBean header = new ReqHeaderBean(url,ctx);
		List<NameValuePair> headerParams = header.getReqHeaderParams();

		//2.组装参数
		if(ajaxParams == null)
		{
			ajaxParams = new AjaxParams();
		}
		for (NameValuePair nameValuePair : headerParams) 
		{
			ajaxParams.put(StringUtils.trimNull(nameValuePair.getName()), StringUtils.trimNull(nameValuePair.getValue()));
		}
		
		//3.返回结果
		return ajaxParams;
	}


	/**
	 * 获取请求url
	 * @param url
	 * @return
	 */
	public static String reqUrl(String url,Context ctx)
	{
		String reqUrl = null;
		if (url.startsWith("http://")|| url.startsWith("https://"))
		{
			reqUrl = url;
		} 
		else 
		{
			reqUrl = StringUtils.trimNull(ctx.getResources().getString(R.string.APIHOST)) + url;
		}
		return reqUrl;
	}
	
	/**
	 * 发送异步的http请求
	 * @param params
	 * @param url
	 */
	public static void doPost(Context ctx,List<NameValuePair>params ,String url,final int reqTag,final HttpRequestClientCallback callback)
	{
		doHttpRequest(ctx,params, url, null, reqTag, callback,HTTP_POST_REQ);
	}
	
	/**
	 * 发送http POST请求--重载
	 * @param params
	 * @param url
	 * @param clazz
	 * @param reqTag
	 * @param callback
	 * @param httpReqMethod
	 */
	public static void doPost(Context ctx,List<NameValuePair>params ,String url,final Class<? extends BaseRespBean> clazz,final int reqTag,final HttpRequestClientCallback callback)
	{
		doHttpRequest(ctx,params, url, clazz, reqTag, callback,HTTP_POST_REQ);
	}
	
	
	/**
	 * 组装post请求参数
	 * @param params
	 * @param url
	 * @return
	 */
	private static AjaxParams packPostParams(Context ctx,List<NameValuePair>params ,String url)
	{
		//1.传入的参数为空
		if (params == null || params.isEmpty()) {
			params = new ArrayList<NameValuePair>();
		}
		//2.传入头信息
		ReqHeaderBean header = new ReqHeaderBean(url,ctx);
		params.addAll(header.getReqHeaderParams());
		
		//3组装参数
		AjaxParams ajaxParams = new AjaxParams();
		for (NameValuePair nameValuePair : params) {
			ajaxParams.put(StringUtils.trimNull(nameValuePair.getName()), StringUtils.trimNull(nameValuePair.getValue()));
		}
		
		//4.返回结果
		return ajaxParams;
	}
	
	
	/**
	 * 发送http GET请求
	 * @param params
	 * @param url
	 * @param clazz
	 * @param reqTag
	 * @param callback
	 */
	public static void doGet(Context ctx,List<NameValuePair>params ,String url,final Class<? extends BaseRespBean> clazz,final int reqTag,final HttpRequestClientCallback callback)
	{
		doHttpRequest(ctx,params, url, clazz, reqTag, callback, HTTP_GET_REQ);
	}

}
