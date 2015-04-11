package com.clt.runman.widget.alertview;

/**
 * 自定义警告框回调接口
 * @author yanshengli
 * @since 2015-2-6
 */
public interface CustomAlertDialogCallBack 
{

	/**
	 * 取消按钮被点击
	 * @param dialog
	 */
	public void onCancelBtnClicked(CustomAlertDialog dialog);
	
	/**
	 * 确定按钮被点击
	 * @param dialog
	 */
	public void onSureBtnClicked(CustomAlertDialog dialog);
}
