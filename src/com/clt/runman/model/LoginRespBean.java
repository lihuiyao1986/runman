package com.clt.runman.model;

/**
 * 登录响应bean
 * @author yanshengli
 * @since 2015-1-27
 */
public class LoginRespBean extends BaseRespBean 
{

	/** 跑男信息 **/
	private RunMan runMan;

	public RunMan getRunMan() {
		return runMan;
	}

	public void setRunMan(RunMan runMan) {
		this.runMan = runMan;
	}
}
