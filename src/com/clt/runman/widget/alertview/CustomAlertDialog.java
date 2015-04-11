package com.clt.runman.widget.alertview;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.utils.StringUtils;

/**
 * 自定义
 * @author yanshengli
 *
 */
public class CustomAlertDialog {

    /** 应用上下文 **/
    private Context                   context;

    /** 回调接口 **/
    private CustomAlertDialogCallBack callback;

    /** 标题视图 **/
    private TextView                  titleView;

    /** 警告框 **/
    private AlertDialog               dialog;

    /** 提示消息视图 **/
    private TextView                  messageView;

    /** 取消按钮对应的线性布局 **/
    private LinearLayout              alert_view_cancel_btn_ll;

    /** 取消按钮 **/
    private Button                    cancelBtn;

    /** 确认按钮对应的线性布局 **/
    private LinearLayout              alert_view_sure_btn_ll;

    /** 确认按钮 **/
    private Button                    sureBtn;

    /**
     * 重载的构造方法
     * @param context
     * @param callback
     */
    public CustomAlertDialog(Context context, CustomAlertDialogCallBack callback) {
       this (context, callback, -1);
    }
    
    public CustomAlertDialog(Context context,CustomAlertDialogCallBack callback,int windowType){
     // 1.接受上下文对象
        this.context = context;

        // 2.接受回调
        this.callback = callback;

        // 3.生成窗体
        dialog = new AlertDialog.Builder (this.context).create ();
        if(windowType!=-1){
            dialog.getWindow ().setType (windowType);
        }
        dialog.show ();

        // 4.初始化视图
        initUIView ();
    }

    /**
     * 初始化窗口视图
     */
    private void initUIView(){
        // 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        Window window = dialog.getWindow ();
        window.setContentView (R.layout.custom_alert_dialog);
        titleView = (TextView) window.findViewById (R.id.alert_view_title_tv);
        messageView = (TextView) window.findViewById (R.id.alert_view_message_tv);
        alert_view_cancel_btn_ll = (LinearLayout) window.findViewById (R.id.alert_view_cancel_btn_ll);
        cancelBtn = (Button) window.findViewById (R.id.alert_view_cancel_btn);
        cancelBtn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                if (callback != null) {
                    callback.onCancelBtnClicked (CustomAlertDialog.this);
                }
                dismiss ();
            }
        });
        alert_view_sure_btn_ll = (LinearLayout) window.findViewById (R.id.alert_view_sure_btn_ll);
        sureBtn = (Button) window.findViewById (R.id.alert_view_sure_btn);
        sureBtn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                if (callback != null) {
                    callback.onSureBtnClicked (CustomAlertDialog.this);
                }
                dismiss ();
            }
        });
    }

    /**
     * 构造方法
     * @param context
     */
    public CustomAlertDialog(Context context) {
        this (context, null);
    }

    public void setTitle(int resId){
        titleView.setText (resId);
        titleView.setVisibility (View.VISIBLE);
    }

    public void setTitle(String title){
        titleView.setText (title);
        titleView.setVisibility (View.VISIBLE);
    }

    public void setMessage(int resId){
        messageView.setText (resId);
    }

    public void setMessage(String message){
        messageView.setText (message);
    }

    /**
     * 设置按钮
     * @param text
     * @param listener
     */
    public void setPositiveButton(String text){
        alert_view_sure_btn_ll.setVisibility (View.VISIBLE);
        sureBtn.setText (StringUtils.trimNull (text, "确定"));
    }

    /**
     * 设置按钮
     * @param text
     * @param listener
     */
    public void setNegativeButton(String text){
        alert_view_cancel_btn_ll.setVisibility (View.VISIBLE);
        cancelBtn.setText (StringUtils.trimNull (text, "取消"));
    }

    /**
     * 关闭对话框
     */
    public void dismiss(){
        dialog.dismiss ();
    }

    /*
     * 获取当前dialog
     */
    public AlertDialog getDialog(){
        return this.dialog;
    }
}
