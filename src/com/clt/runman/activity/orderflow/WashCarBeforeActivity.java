package com.clt.runman.activity.orderflow;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.clt.runman.R;
import com.clt.runman.core.WashCarFlowActivity;
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

/**
 *@Description:步骤2/4：洗车前拍照
 *@Author:张聪
 *@Since:2015年1月19日下午4:25:32
 */
public class WashCarBeforeActivity extends WashCarFlowActivity implements OnClickListener {

    /** 完成按钮 **/
    private Button                   wash_car_before_finished_btn;

    /** 整体照片**/
    private ImageButton              washcar_before_0_0_0;

    /** 正前照片 **/
    private ImageButton              washcar_before_0_1_0;
    private ImageButton              washcar_before_0_1_1;
    private ImageButton              washcar_before_0_1_2;

    /** 左侧照片 **/
    private ImageButton              washcar_before_0_2_0;
    private ImageButton              washcar_before_0_2_1;
    private ImageButton              washcar_before_0_2_2;

    /** 正后照片 **/
    private ImageButton              washcar_before_0_3_0;
    private ImageButton              washcar_before_0_3_1;
    private ImageButton              washcar_before_0_3_2;

    /** 右侧照片 **/
    private ImageButton              washcar_before_0_4_0;
    private ImageButton              washcar_before_0_4_1;
    private ImageButton              washcar_before_0_4_2;

    /** 顶部照片 **/
    private ImageButton              washcar_before_0_5_0;
    private ImageButton              washcar_before_0_5_1;
    private ImageButton              washcar_before_0_5_2;

    /** 按钮 **/
    private Map<String, ImageButton> buttons;

    /** 当前被点击的按钮 **/
    private ImageButton              currentImgBtn;

    /** 请求tag **/
    private final static int         confirm_wash_car_before_http_tag = 100;

    /** 洗车前拍照信息 **/
    private WashcarPicInfoDaoModel   picInfoBean;

    /** 请求拍照的请求码 **/
    private final static int         REQUEST_TAKE_PHOTO               = 10000;

    /** dateFormat **/
    @SuppressLint("SimpleDateFormat")
    private DateFormat               dateFormat                       = new SimpleDateFormat ("yyyyMMdd");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类方法
        super.onCreate (savedInstanceState);

        // 2.初始化视图
        initUIView ();

        // 3.初始化数据
        initData ();

        // 4.初始化事件监听器
        initListeners ();
    }

    /**
     * 初始化事件监听器
     */
    private void initListeners(){
        wash_car_before_finished_btn.setOnClickListener (this);
        washcar_before_0_0_0.setOnClickListener (this);

        washcar_before_0_1_0.setOnClickListener (this);
        washcar_before_0_1_1.setOnClickListener (this);
        washcar_before_0_1_2.setOnClickListener (this);

        washcar_before_0_2_0.setOnClickListener (this);
        washcar_before_0_2_1.setOnClickListener (this);
        washcar_before_0_2_2.setOnClickListener (this);

        washcar_before_0_3_0.setOnClickListener (this);
        washcar_before_0_3_1.setOnClickListener (this);
        washcar_before_0_3_2.setOnClickListener (this);

        washcar_before_0_4_0.setOnClickListener (this);
        washcar_before_0_4_1.setOnClickListener (this);
        washcar_before_0_4_2.setOnClickListener (this);

        washcar_before_0_5_0.setOnClickListener (this);
        washcar_before_0_5_1.setOnClickListener (this);
        washcar_before_0_5_2.setOnClickListener (this);

    }

    /**
     * 按钮被点击
     */
    @Override
    public void onClick(View v){
        if (v instanceof ImageButton) {
            currentImgBtn = (ImageButton) v;
            String tag = (String) currentImgBtn.getTag ();
            String[] items = tag.split ("_");
            int type = Integer.parseInt (items[0]);
            int subtype = Integer.parseInt (items[1]);
            int index = Integer.parseInt (items[2]);
            takePic (type, subtype, index);
        } else if (wash_car_before_finished_btn == v) {
            bFinishClick ();
        }
    }

    /**
     * 初始化数据
     */
    private void initData(){
        // 1.初始化照片信息
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        picInfoBean = new WashcarPicInfoDaoModel ();
        picInfoBean.setPicDate (dateFormat.format (new Date ()));// 日期
        picInfoBean.setRunmanid (runMan.getRunManId ());// 跑男ID
        picInfoBean.setOrderid (CommonUtils.queryCurrentReceivedOrderId (this));// 订单号

        // 2.初始化按钮
        buttons = new HashMap<String, ImageButton> ();
        buttons.put ("0_0_0", washcar_before_0_0_0);
        buttons.put ("0_1_0", washcar_before_0_1_0);
        buttons.put ("0_1_1", washcar_before_0_1_1);
        buttons.put ("0_1_2", washcar_before_0_1_2);
        buttons.put ("0_2_0", washcar_before_0_2_0);
        buttons.put ("0_2_1", washcar_before_0_2_1);
        buttons.put ("0_2_2", washcar_before_0_2_2);
        buttons.put ("0_3_0", washcar_before_0_3_0);
        buttons.put ("0_3_1", washcar_before_0_3_1);
        buttons.put ("0_3_2", washcar_before_0_3_2);
        buttons.put ("0_4_0", washcar_before_0_4_0);
        buttons.put ("0_4_1", washcar_before_0_4_1);
        buttons.put ("0_4_2", washcar_before_0_4_2);
        buttons.put ("0_5_0", washcar_before_0_5_0);
        buttons.put ("0_5_1", washcar_before_0_5_1);
        buttons.put ("0_5_2", washcar_before_0_5_2);

        // 3.初始化之前拍照的图片
        initPrePicsInfoList ();
    }

    /**
     * 初始化之前拍照的图片
     */
    private void initPrePicsInfoList(){
        // 3.查询图片集合
        List<WashcarPicInfoDaoModel> list = queryPrePicInfoList ();
        for ( WashcarPicInfoDaoModel washcarPicInfoDaoModel : list ) {
            final int type = washcarPicInfoDaoModel.getPicType ();
            final int subtype = washcarPicInfoDaoModel.getPicSubtype ();
            final int index = washcarPicInfoDaoModel.getPicIndex ();
            String filePath = StringUtils.trimNull (washcarPicInfoDaoModel.getThumbnailPath ());
            if (StringUtils.isEmpty (filePath)) {
                continue;
            }
            String key = String.valueOf (type) + "_" + String.valueOf (subtype) + "_" + String.valueOf (index);
            ImageButton button = buttons.get (key);
            BitmapCache.getInstance (this).displayBmpByFilePath (button, filePath, new ImageCallback () {

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

    /***
     * 初始化视图
     */
    private void initUIView(){
        // 1.布局和导航
        setContentView (R.layout.orderflow_washcar_before_activity);
        super.initTopTitle (R.drawable.top_service, appContext.getMachineStatusTitle (), R.drawable.top_customer);

        // 2.我已完成按钮
        wash_car_before_finished_btn = (Button) findViewById (R.id.wash_car_before_finished_btn);

        // 3.初始化按钮
        initTakePicBtn ();
    }

    /**
     * 初始化按钮
     */
    private void initTakePicBtn(){
        washcar_before_0_0_0 = (ImageButton) findViewById (R.id.washcar_before_0_0_0);
        washcar_before_0_0_0.setTag ("0_0_0");

        washcar_before_0_1_0 = (ImageButton) findViewById (R.id.washcar_before_0_1_0);
        washcar_before_0_1_0.setTag ("0_1_0");
        washcar_before_0_1_1 = (ImageButton) findViewById (R.id.washcar_before_0_1_1);
        washcar_before_0_1_1.setTag ("0_1_1");
        washcar_before_0_1_2 = (ImageButton) findViewById (R.id.washcar_before_0_1_2);
        washcar_before_0_1_2.setTag ("0_1_2");

        washcar_before_0_2_0 = (ImageButton) findViewById (R.id.washcar_before_0_2_0);
        washcar_before_0_2_0.setTag ("0_2_0");
        washcar_before_0_2_1 = (ImageButton) findViewById (R.id.washcar_before_0_2_1);
        washcar_before_0_2_1.setTag ("0_2_1");
        washcar_before_0_2_2 = (ImageButton) findViewById (R.id.washcar_before_0_2_2);
        washcar_before_0_2_2.setTag ("0_2_2");

        washcar_before_0_3_0 = (ImageButton) findViewById (R.id.washcar_before_0_3_0);
        washcar_before_0_3_0.setTag ("0_3_0");
        washcar_before_0_3_1 = (ImageButton) findViewById (R.id.washcar_before_0_3_1);
        washcar_before_0_3_1.setTag ("0_3_1");
        washcar_before_0_3_2 = (ImageButton) findViewById (R.id.washcar_before_0_3_2);
        washcar_before_0_3_2.setTag ("0_3_2");

        washcar_before_0_4_0 = (ImageButton) findViewById (R.id.washcar_before_0_4_0);
        washcar_before_0_4_0.setTag ("0_4_0");
        washcar_before_0_4_1 = (ImageButton) findViewById (R.id.washcar_before_0_4_1);
        washcar_before_0_4_1.setTag ("0_4_1");
        washcar_before_0_4_2 = (ImageButton) findViewById (R.id.washcar_before_0_4_2);
        washcar_before_0_4_2.setTag ("0_4_2");

        washcar_before_0_5_0 = (ImageButton) findViewById (R.id.washcar_before_0_5_0);
        washcar_before_0_5_0.setTag ("0_5_0");
        washcar_before_0_5_1 = (ImageButton) findViewById (R.id.washcar_before_0_5_1);
        washcar_before_0_5_1.setTag ("0_5_1");
        washcar_before_0_5_2 = (ImageButton) findViewById (R.id.washcar_before_0_5_2);
        washcar_before_0_5_2.setTag ("0_5_2");
    }

    /**
     * 拍照
     * @param type
     * @param subtype
     * @param index
     */
    private void takePic(int type,int subtype,int index){
        picInfoBean.setPicType (type);
        picInfoBean.setPicIndex (index);
        picInfoBean.setPicSubtype (subtype);
        Intent takePictureIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        File file = createImageFile ();
        takePictureIntent.putExtra (MediaStore.EXTRA_OUTPUT, Uri.fromFile (file));
        startActivityForResult (takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 创建存放图片的目录
     * @return
     */
    private File createImageFile(){
        String filePath = CommonUtils.takePicRootDir (this);
        File rootDir = new File (filePath);
        if (!rootDir.exists ()) {
            rootDir.mkdirs ();
        }
        File file = new File (rootDir,picInfoBean.getFilename ());
        picInfoBean.setPicUrl (file.getAbsolutePath ());
        return file;
    }

    /**
     * 拍照回来之后的回调
     */
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            finishTakePic ();
        }
    }

    /**
     * 完成拍照
     */
    private void finishTakePic(){
        // 1.展示图片
        String filePath = picInfoBean.getPicUrl ();
        PictureUtils.convertWashcarTakePicture (filePath, this);
        int height = CommonUtils.dip2px (this, 70);
        int width = CommonUtils.dip2px (this, 88);
        String thumbnailPath = CommonUtils.getWashcarThumbnailPath (filePath);
        picInfoBean.setThumbnailPath (thumbnailPath);
        PictureUtils.createImageThumbnail (filePath, thumbnailPath, width, height);
        BitmapCache.getInstance (this).displayBmpByFilePath (currentImgBtn, thumbnailPath, new ImageCallback () {

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
        WashcarPicInfoDao.newInstance (this).saveOrUpdatePicInfo (picInfoBean);
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
        picInfo.setPicType (AppConstant.washcar_before_type);// 类型
        return WashcarPicInfoDao.newInstance (this).queryPicInfoList (picInfo);
    }

    /**
     *@Description: 洗车前拍照完成 
     *@Author: 张聪
     *@Since: 2015年1月19日下午7:47:31
     */
    public void bFinishClick(){
        String washcarBeforeUrl = AppUtils.getContext ().getResources ().getString (R.string.washCarBefore);
        String orderId = CommonUtils.queryCurrentReceivedOrderId (this);
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        params.add (new BasicNameValuePair ("orderId",orderId));
        params.add (new BasicNameValuePair ("type","0"));
        doPost (washcarBeforeUrl, params, BaseRespBean.class, confirm_wash_car_before_http_tag);
    }

    /**
     * http请求成功响应回调
     */
    protected void handleHttpSuccess(BaseRespBean data,int reqTag){
        super.handleHttpSuccess (data, reqTag);
        switch (reqTag) {
            case confirm_wash_car_before_http_tag:
                processConfirmWashCarBeforeCallBack (data);
                break;
            default:
                break;
        }
    }

    /**
     * 处理上传洗车后照片的http请求成功的回调
     * @param obj
     */
    private void processConfirmWashCarBeforeCallBack(Object obj){
        redirectToTargetActivity (WashCarBeforeActivity.this, StartWaskCarActivity.class, null, false);
        finish ();
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
        redirectToTargetActivity (WashCarBeforeActivity.this, FoundActivity.class, new Bundle (), SwipeDirect.DIRECT_DOWN);
        finish ();
    }

    /**
     * 上划
     */
    @Override
    protected void swipeUp(){
        super.swipeUp ();
        if (obtainCurrOrderStatus () >= AppConstant.ORDER_WASH_BEFORE) {
            // redirectToTargetActivity (WashCarBeforeActivity.this, StartWaskCarActivity.class, setParam (), SwipeDirect.DIRECT_UP);
            redirectToTargetActivity (WashCarBeforeActivity.this, StartWaskCarActivity.class, null, SwipeDirect.DIRECT_UP);
            finish ();
        }
    }

    /**
     * 更新按钮状态
     */
    @Override
    protected void updateButtonStatus(){
        super.updateButtonStatus ();
        if (obtainCurrOrderStatus () < AppConstant.ORDER_WASH_BEFORE) {
            wash_car_before_finished_btn.setEnabled (true);
            wash_car_before_finished_btn.setBackgroundResource (R.drawable.shape_blue_btn);
        } else {
            wash_car_before_finished_btn.setEnabled (false);
            wash_car_before_finished_btn.setBackgroundResource (R.drawable.shape_gray_btn);
        }
    }

}
