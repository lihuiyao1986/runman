package com.clt.runman.activity.orderflow;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.http.AjaxParams;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.clt.runman.R;
import com.clt.runman.activity.index.OrderReceiveActivity;
import com.clt.runman.core.WashCarFlowActivity;
import com.clt.runman.db.dao.OrderDao;
import com.clt.runman.db.dao.WashcarPicInfoDao;
import com.clt.runman.db.model.WashcarPicInfoDaoModel;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.BitmapCache;
import com.clt.runman.utils.BitmapCache.ImageCallback;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.PictureUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.SwipeDirect;
import com.clt.runman.widget.toast.CustomToastDialog;

/**
 *@Description:步骤4/4：洗车后拍照
 *@Author:张聪
 *@Since:2015年1月19日下午4:24:50
 */
public class WashCarAfterActivity extends WashCarFlowActivity implements OnClickListener {

    /** 上传洗车后照片的请求tag **/
    private static final int         upload_wash_car_pic_http_tag       = 100;
    /**上传洗车照片后点击拍照完成请求tag**/
    private static final int         upload_wash_car_pic_after_http_tag = 200;

    /** 请求拍照的请求码 **/
    private final static int         REQUEST_TAKE_PHOTO_WASHCAR_AFTER   = 10000;

    /** 我已完成按钮 **/
    private Button                   wash_car_after_finished_btn;

    /**拍照按钮**/
    private ImageButton              washcar_after_1_0_0;
    private ImageButton              washcar_after_1_0_1;
    private ImageButton              washcar_after_1_0_2;
    private ImageButton              washcar_after_1_0_3;
    private ImageButton              washcar_after_1_0_4;
    private ImageButton              washcar_after_1_0_5;
    private ImageButton              washcar_after_1_0_6;
    private ImageButton              washcar_after_1_0_7;
    private ImageButton              washcar_after_1_0_8;
    private ImageButton              imageButton;

    /** 按钮 **/
    private Map<String, ImageButton> buttons;

    /** 洗车前拍照信息 **/
    private WashcarPicInfoDaoModel   picAfterBean;

    /** dateFormat **/
    @SuppressLint("SimpleDateFormat")
    private DateFormat               dateFormat                         = new SimpleDateFormat ("yyyyMMdd");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);
        // 2.初始化UIView
        initUIView ();
        // 3.初始化数据
        initData ();
        // 4.初始化监听
        initListeners ();
    }

    /**
     * 初始化UIView视图
     */
    private void initUIView(){
        // 1.布局和导航
        setContentView (R.layout.orderflow_washcar_after_activity);
        super.initTopTitle (R.drawable.top_service, appContext.getMachineStatusTitle (), R.drawable.top_customer);
        // 2.拍照按钮
        washcar_after_1_0_0 = (ImageButton) findViewById (R.id.washcar_after_1_0_0);
        washcar_after_1_0_0.setTag ("1_0_0");

        washcar_after_1_0_1 = (ImageButton) findViewById (R.id.washcar_after_1_0_1);
        washcar_after_1_0_1.setTag ("1_0_1");

        washcar_after_1_0_2 = (ImageButton) findViewById (R.id.washcar_after_1_0_2);
        washcar_after_1_0_2.setTag ("1_0_2");

        washcar_after_1_0_3 = (ImageButton) findViewById (R.id.washcar_after_1_0_3);
        washcar_after_1_0_3.setTag ("1_0_3");

        washcar_after_1_0_4 = (ImageButton) findViewById (R.id.washcar_after_1_0_4);
        washcar_after_1_0_4.setTag ("1_0_4");

        washcar_after_1_0_5 = (ImageButton) findViewById (R.id.washcar_after_1_0_5);
        washcar_after_1_0_5.setTag ("1_0_5");

        washcar_after_1_0_6 = (ImageButton) findViewById (R.id.washcar_after_1_0_6);
        washcar_after_1_0_6.setTag ("1_0_6");

        washcar_after_1_0_7 = (ImageButton) findViewById (R.id.washcar_after_1_0_7);
        washcar_after_1_0_7.setTag ("1_0_7");

        washcar_after_1_0_8 = (ImageButton) findViewById (R.id.washcar_after_1_0_8);
        washcar_after_1_0_8.setTag ("1_0_8");

        // 3.按钮
        wash_car_after_finished_btn = (Button) findViewById (R.id.wash_car_after_finished_btn);
    }

    private void initData(){
        // 1.初始化照片信息
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        picAfterBean = new WashcarPicInfoDaoModel ();
        picAfterBean.setPicDate (dateFormat.format (new Date ()));// 日期
        picAfterBean.setRunmanid (runMan.getRunManId ());// 跑男ID
        picAfterBean.setOrderid (CommonUtils.queryCurrentReceivedOrderId (this));// 订单号

        // 2.初始化按钮
        buttons = new HashMap<String, ImageButton> ();
        buttons.put ("1_0_0", washcar_after_1_0_0);
        buttons.put ("1_0_1", washcar_after_1_0_1);
        buttons.put ("1_0_2", washcar_after_1_0_2);
        buttons.put ("1_0_3", washcar_after_1_0_3);
        buttons.put ("1_0_4", washcar_after_1_0_4);
        buttons.put ("1_0_5", washcar_after_1_0_5);
        buttons.put ("1_0_6", washcar_after_1_0_6);
        buttons.put ("1_0_7", washcar_after_1_0_7);
        buttons.put ("1_0_8", washcar_after_1_0_8);

        // 3.初始化之前拍照的图片
        initPrePicsInfoList ();
    }

    /**
     *@Description: 初始化按钮监听 
     *@Author: 张聪
     *@Since: 2015年3月21日下午5:00:27
     */
    private void initListeners(){
        washcar_after_1_0_0.setOnClickListener (this);
        washcar_after_1_0_1.setOnClickListener (this);
        washcar_after_1_0_2.setOnClickListener (this);
        washcar_after_1_0_3.setOnClickListener (this);
        washcar_after_1_0_4.setOnClickListener (this);
        washcar_after_1_0_5.setOnClickListener (this);
        washcar_after_1_0_6.setOnClickListener (this);
        washcar_after_1_0_7.setOnClickListener (this);
        washcar_after_1_0_8.setOnClickListener (this);
        wash_car_after_finished_btn.setOnClickListener (this);
    }

    /**
     *@Description: 我已完成 
     *@Author: 张聪
     *@Since: 2015年1月19日下午7:57:15
     */
    public void aFinishClick(){

        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        String orderId = CommonUtils.queryCurrentReceivedOrderId (this);
        if (!StringUtils.isEmpty (runMan.getRunManId ()) && !StringUtils.isEmpty (orderId)) {
            List<NameValuePair> params = new ArrayList<NameValuePair> ();
            // params.add (new BasicNameValuePair ("runManId",runMan.getRunManId ()));
            params.add (new BasicNameValuePair ("orderId",orderId));
            // params.add (new BasicNameValuePair ("type","1"));
            // String afterFinishUrl = AppUtils.getContext ().getResources ().getString (R.string.finishWash);
            String afterFinishUrl = AppUtils.getContext ().getResources ().getString (R.string.washAfter);
            doPost (afterFinishUrl, params, BaseRespBean.class, upload_wash_car_pic_after_http_tag);
        }
    }

    /**
     * http请求成功响应回调
     */
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        switch (reqTag) {
            case upload_wash_car_pic_after_http_tag:
                processUploadWashCarPicCallBack (data);
                break;
            case upload_wash_car_pic_http_tag:
                // 洗车后整体照片上传成功,更新数据库状态
                processUploadWashCarPicSuccess ();
                break;
            default:
                break;
        }
    }

    /*
     * 图片上传成功的回调
     */
    private void processUploadWashCarPicSuccess(){
        synchronized (picAfterBean) {
            picAfterBean.setIsUpload (1);
            WashcarPicInfoDao.newInstance (appContext).updatePicInfo (picAfterBean);
        }
    }

    /**
     * 处理上传洗车后照片的http请求成功的回调
     * @param obj
     */
    private void processUploadWashCarPicCallBack(Object obj){
        // 1.当订单支付方式不是“现金支付”和“体验券”就删除所有的订单
        if (obtainCurrOrderPayType () == AppConstant.PAYTYPE_WECHAT_PAYMENT) {
            OrderDao.newInstance (this).deleteAllOrder ();
            CommonUtils.saveCurrentReceivedOrderId ("", this);
        }

        // 2.显示弹框
        switch (obtainCurrOrderPayType ()) {
            case AppConstant.PAYTYPE_CURRENCY:
                redirectToTargetActivity (OfflinePayConfirmActivity.class, null);
                finish ();
                break;
            case AppConstant.PAYTYPE_EXPERIENCE_TICKET:
                redirectToTargetActivity (OfflinePayConfirmActivity.class, null);
                finish ();
                break;
            case AppConstant.PAYTYPE_WECHAT_PAYMENT:
                CustomToastDialog.makeText (this, "完成订单", "恭喜你，又有一笔新收入了，继续加油", Toast.LENGTH_SHORT, true).show ();
                wash_car_after_finished_btn.postDelayed (new Runnable () {

                    @Override
                    public void run(){
                        redirectToTargetActivity (OrderReceiveActivity.class, null);
                        finish ();
                    }
                }, 2000);
                break;
            default:
                break;
        }
    }

    /**
     *@Description: 监听屏幕滑动事件
     *@Author: 张聪
     *@Since: 2015年1月20日下午2:26:51
     *@param event
     *@return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return gestureDetector.onTouchEvent (event);
    }

    /**
     * 下划
     */
    @Override
    protected void swipeDown(){
        super.swipeDown ();
        redirectToTargetActivity (WashCarAfterActivity.this, StartWaskCarActivity.class, new Bundle (), SwipeDirect.DIRECT_DOWN);
        finish ();
    }

    /**
     * 更新按钮状态
     */
    @Override
    protected void updateButtonStatus(){
        super.updateButtonStatus ();
        if (obtainCurrOrderStatus () < AppConstant.ORDER_WASH_AFTER) {
            wash_car_after_finished_btn.setEnabled (true);
            wash_car_after_finished_btn.setBackgroundResource (R.drawable.shape_blue_btn);
        } else {
            wash_car_after_finished_btn.setEnabled (false);
            wash_car_after_finished_btn.setBackgroundResource (R.drawable.shape_gray_btn);
        }
    }

    @Override
    public void onClick(View v){
        if (v instanceof ImageButton) {
            imageButton = (ImageButton) v;
            String tag = (String) imageButton.getTag ();
            String[] items = tag.split ("_");
            int type = Integer.parseInt (items[0]);
            int subtype = Integer.parseInt (items[1]);
            int index = Integer.parseInt (items[2]);
            takeWashCarAfterPic (type, subtype, index);
        } else if (wash_car_after_finished_btn == v) {
            aFinishClick ();
        }
    }

    /**
     *@Description: 拍照
     *@Author: 张聪
     *@Since: 2015年3月21日下午5:38:02
     *@param type 洗车后
     *@param subtype
     *@param index
     */
    private void takeWashCarAfterPic(int type,int subtype,int index){
        picAfterBean.setPicType (type);
        picAfterBean.setPicIndex (index);
        picAfterBean.setPicSubtype (subtype);
        Intent takePictureIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        File file = createWashAfterFile ();
        takePictureIntent.putExtra (MediaStore.EXTRA_OUTPUT, Uri.fromFile (file));
        startActivityForResult (takePictureIntent, REQUEST_TAKE_PHOTO_WASHCAR_AFTER);
    }

    /**
     *@Description: 创建保存路径 
     *@Author: 张聪
     *@Since: 2015年3月21日下午5:38:27
     *@return
     */
    private File createWashAfterFile(){
        String filePath = CommonUtils.takePicRootDir (this);
        File rootDir = new File (filePath);
        if (!rootDir.exists ()) {
            rootDir.mkdirs ();
        }
        File file = new File (rootDir,picAfterBean.getFilename ());
        picAfterBean.setPicUrl (file.getAbsolutePath ());
        return file;
    }

    /**
     *@Description: 拍照回来之后的回调 
     *@Author: 张聪
     *@Since: 2015年3月21日下午5:46:21
     *@param requestCode
     *@param resultCode
     *@param data
     */
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO_WASHCAR_AFTER && resultCode == Activity.RESULT_OK) {
            finishTakePic ();
        }
    }

    /**
     *@Description: 完成拍照 
     *@Author: 张聪
     *@Since: 2015年3月21日下午5:40:31
     */
    private void finishTakePic(){
        // 1.展示图片
        final String filePath = picAfterBean.getPicUrl ();
        PictureUtils.convertWashcarTakePicture (filePath, this);
        int height = CommonUtils.dip2px (this, 70);
        int width = CommonUtils.dip2px (this, 88);
        String thumbnailPath = CommonUtils.getWashcarThumbnailPath (filePath);
        PictureUtils.createImageThumbnail (filePath, thumbnailPath, width, height);
        picAfterBean.setThumbnailPath (thumbnailPath);

        BitmapCache.getInstance (this).displayBmpByFilePath (imageButton, thumbnailPath, new ImageCallback () {

            @Override
            public void imageLoad(ImageView imageView,Bitmap bitmap,Object... params){
                imageView.setImageBitmap (bitmap);
            }

            @Override
            public Bitmap getBitMapFromFile(String sourcePath){
                return PictureUtils.getBitmapByPath (sourcePath);
            }
        }, false);

        // 2.保存图片信息到数据库中
        WashcarPicInfoDao.newInstance (this).saveOrUpdatePicInfo (picAfterBean);

        // 3.上传洗车后整体照片
        if (imageButton == washcar_after_1_0_0) {
            synchronized (picAfterBean) {
                uploadTakePhoto (picAfterBean);
            }
        }
    }

    /** 
    * @Title: uploadTakePhoto 
    * @Description: TODO() 
    * @param     
    * @return void    
    * @throws 
    */
    private void uploadTakePhoto(WashcarPicInfoDaoModel picInfo){

        // // 查询跑男id
        // RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        // // 查询当前订单号
        // String orderId = CommonUtils.queryCurrentReceivedOrderId (this);
        // WashcarPicInfoDaoModel model = new WashcarPicInfoDaoModel ();
        // model.setRunmanid (runMan.getRunManId ());
        // model.setOrderid (orderId);
        // model.setPicIndex (0);
        // model.setPicType (1);
        // model.setPicSubtype (0);

        // // 根据条件查询当前订单号洗车后首张整体照片
        // List<WashcarPicInfoDaoModel> list = WashcarPicInfoDao.newInstance (appContext).queryPicInfoByFileName (model);
        //
        // if (list == null || list.isEmpty ()) { return; }
        // WashcarPicInfoDaoModel picInfo = list.get (0);

        if (!StringUtils.isEmpty (picInfo.getRunmanid ()) && !StringUtils.isEmpty (picInfo.getOrderid ())) {
            AjaxParams params = new AjaxParams ();
            params.put ("orderId", picInfo.getOrderid ());
            params.put ("photoType", picInfo.getUploadPhotoType ());
            try {
                params.put ("photos", new File (picInfo.getPicUrl ()));
            } catch (FileNotFoundException e) {
                e.printStackTrace ();
            }
            String afterFinishUrl = AppUtils.getContext ().getResources ().getString (R.string.uploadAfterwashCar);
            uploadFile (afterFinishUrl, params, BaseRespBean.class, upload_wash_car_pic_http_tag, true);
        }
    }

    /**
     * 初始化之前拍照的图片
     */
    private void initPrePicsInfoList(){
        // 3.查询图片集合
        List<WashcarPicInfoDaoModel> list = queryPrePicInfoList ();
        for ( WashcarPicInfoDaoModel washcarPicInfoDaoModel : list ) {
            int type = washcarPicInfoDaoModel.getPicType ();
            int subtype = washcarPicInfoDaoModel.getPicSubtype ();
            int index = washcarPicInfoDaoModel.getPicIndex ();
            String thumbnailPath = StringUtils.trimNull (washcarPicInfoDaoModel.getThumbnailPath ());
            if (StringUtils.isEmpty (thumbnailPath)) {
                continue;
            }
            String key = String.valueOf (type) + "_" + String.valueOf (subtype) + "_" + String.valueOf (index);
            ImageButton button = buttons.get (key);
            BitmapCache.getInstance (this).displayBmpByFilePath (button, thumbnailPath, new ImageCallback () {

                @Override
                public void imageLoad(ImageView imageView,Bitmap bitmap,Object... params){
                    imageView.setImageBitmap (bitmap);
                }

                @Override
                public Bitmap getBitMapFromFile(String sourcePath){
                    return PictureUtils.getBitmapByPath (sourcePath);
                }
            });
        }
    }

    /**
     * 查询之前拍照过的照片信息列表
     */
    private List<WashcarPicInfoDaoModel> queryPrePicInfoList(){
        WashcarPicInfoDaoModel picInfo = new WashcarPicInfoDaoModel ();
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        picInfo.setPicDate (dateFormat.format (new Date ()));// 日期
        picInfo.setRunmanid (runMan.getRunManId ());// 跑男ID
        picInfo.setOrderid (CommonUtils.queryCurrentReceivedOrderId (this));// 订单号
        picInfo.setPicType (AppConstant.washcar_after_type);// 类型
        return WashcarPicInfoDao.newInstance (this).queryPicInfoList (picInfo);
    }
}
