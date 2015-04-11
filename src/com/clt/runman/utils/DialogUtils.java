package com.clt.runman.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.widget.Toast;

import com.clt.runman.R;
import com.clt.runman.interfaces.AlertDialogueCallBack;
import com.clt.runman.widget.alertview.CustomAlertDialog;
import com.clt.runman.widget.alertview.CustomAlertDialogCallBack;
import com.clt.runman.widget.customprogressview.CustomProgressDialog;

/**
 * 对话框工具类
 * @author yanshengli
 * @since 2015-1-30
 */
public class DialogUtils {

    private static CustomProgressDialog dialog = null;

    /**
     * 显示进度对话框
     */
    public static void showProgressDialog(){
        showProgressDialog (false, null);
    }

    /**
     * 显示进度对话框
     * @param tipInfo
     */
    public static void showProgressDialog(String tipInfo){
        showProgressDialog (false, StringUtils.trimNull (tipInfo, "正在加载，请稍后..."));
    }

    /**
     * 显示进度对话框--重载
     * @param cancelable
     * @param text
     */
    public static void showProgressDialog(boolean cancelable,String text){
        closeDialog ();
        dialog = new CustomProgressDialog (AppUtils.getActivity (),text,R.anim.custom_progress_img_anim);
        dialog.setCancelable (cancelable);
        dialog.setOnCancelListener (new OnCancelListener () {

            @Override
            public void onCancel(DialogInterface dialog){}
        });
        try {
            dialog.show ();
        } catch (Exception e) {}
    }

    /**
     * 关闭对话框
     */
    public static void closeDialog(){
        if (dialog != null && dialog.isShowing ()) {
            try {
                dialog.dismiss ();
            } catch (Exception e) {}
            dialog = null;
        }
    }

    /**
     *@Description: TODO(这里用一句话描述这个方法的作用) 
     *@Author: 张聪
     *@Since: 2014年12月29日下午5:19:10
     *@param context
     *@param iconId
     *@param title
     *@param message
     *@param positiveBtnName
     *@param negativeBtnName
     *@param positiveBtnListener
     *@param negativeBtnListener
     *@return
     */
    public static Dialog createConfirmDialog(Context context,int iconId,String title,String message,String positiveBtnName,String negativeBtnName,
            android.content.DialogInterface.OnClickListener positiveBtnListener,android.content.DialogInterface.OnClickListener negativeBtnListener){
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder (context);
        builder.setIcon (iconId);
        builder.setTitle (title);
        builder.setMessage (message);
        builder.setPositiveButton (positiveBtnName, positiveBtnListener);
        builder.setNegativeButton (negativeBtnName, negativeBtnListener);
        dialog = builder.create ();
        return dialog;
    }

    /**
     *@Description: TODO(这里用一句话描述这个方法的作用) 
     *@Author: 张聪
     *@Since: 2014年12月29日下午5:43:39
     *@param context
     *@param msg
     */
    public static void showToast(Context context,String msg){
        Toast.makeText (context, msg, Toast.LENGTH_SHORT).show ();
    }

    /**
     * 提示对话框
     * @param @param msg
     * @author liys
     * @date 2015-1-29
     */
    public static void showAlertDialog(String msg,Context context){
        AlertDialog.Builder alert_dialog = new AlertDialog.Builder (context);
        alert_dialog.setMessage (msg);
        alert_dialog.setPositiveButton ("确定", new DialogInterface.OnClickListener () {

            @Override
            public void onClick(DialogInterface dialog,int which){
                dialog.cancel ();
                return;
            }
        });
        AlertDialog dialog = alert_dialog.create ();
        dialog.setCanceledOnTouchOutside (false);
        dialog.setTitle ("提示");
        dialog.show ();
    }

    /**
     * 提示对话框
     *
     * @param @param msg
     * @author liys
     * @date 2015-1-29
     */
    public static void showAlertDialog(String msg,Context context,final AlertDialogueCallBack callback){
        CustomAlertDialog dialog = new CustomAlertDialog (context,new CustomAlertDialogCallBack () {

            @Override
            public void onSureBtnClicked(CustomAlertDialog dialog){
                if (callback != null) {
                    callback.doCallBack ();
                }
            }

            @Override
            public void onCancelBtnClicked(CustomAlertDialog dialog){

            }
        });
        dialog.setMessage (msg);
        dialog.setPositiveButton ("确定");
    }

    /**
     * 提示对话框
     *
     * @param @param msg
     * @author liys
     * @date 2015-1-29
     */
    public static void showAlertDialog(String msg,Context context,String leftBtnMsg,String rightBtnMsg,final AlertDialogueCallBack callback){
        CustomAlertDialog dialog = new CustomAlertDialog (context,new CustomAlertDialogCallBack () {

            @Override
            public void onSureBtnClicked(CustomAlertDialog dialog){
                if (callback != null) {
                    callback.doCallBack ();
                }
            }

            @Override
            public void onCancelBtnClicked(CustomAlertDialog dialog){

            }
        });
        dialog.setMessage (msg);
        dialog.setPositiveButton (StringUtils.trimNull (rightBtnMsg, "确定"));
        dialog.setNegativeButton (StringUtils.trimNull (leftBtnMsg, "取消"));
    }

    public static void showAlertDialog(String msg,Context context,String leftBtnMsg,String rightBtnMsg,int windowType,final AlertDialogueCallBack callback){
        CustomAlertDialog dialog = new CustomAlertDialog (context,new CustomAlertDialogCallBack () {

            @Override
            public void onSureBtnClicked(CustomAlertDialog dialog){
                if (callback != null) {
                    callback.doCallBack ();
                }
            }

            @Override
            public void onCancelBtnClicked(CustomAlertDialog dialog){

            }
        },windowType);
        dialog.setMessage (msg);
        dialog.setPositiveButton (StringUtils.trimNull (rightBtnMsg, "确定"));
        dialog.setNegativeButton (StringUtils.trimNull (leftBtnMsg, "取消"));
    }
}
