package com.clt.runman.http;

import com.clt.runman.model.BaseRespBean;

/**
 * HttpRequestClientCallback适配器类
 * @author yanshengli
 * @since 2015-1-29
 */
public class HttpRequestClientCallbackAdapter implements HttpRequestClientCallback {

	@Override
	public void httpRespSuccess(BaseRespBean result, int reqTag, Class<?> clazz) {
		// TODO Auto-generated method stub

	}

	@Override
	public void httpRespFail(String errorcode, String errorMsg, int reqTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void httpReqBegin(int reqTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void httpOnProgress(boolean progress, int rate, int reqTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void httpOnloading(long count, long current, int reqTag) {
		// TODO Auto-generated method stub

	}

}
