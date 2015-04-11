package com.clt.runman.activity.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.clt.runman.R;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.db.dao.WashcarPicInfoDao;
import com.clt.runman.db.model.WashcarPicInfoDaoModel;
import com.clt.runman.model.OrderQueryDetailBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.BitmapCache;
import com.clt.runman.utils.BitmapCache.ImageCallback;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.PictureUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.SwipeDirect;

/**
 * 检查洗车后照片页面
 * @author yanshengli
 * @since 2015-3-24
 */
public class CheckAfterPicActivity extends BaseActivity implements OnClickListener {

    /** 导航左按钮 **/
    private Button                   check_after_pic_nav_bar_left_btn;

    /** 当前的订单信息 **/
    private OrderQueryDetailBean     orderItem;

    /** 图片路径 **/
    private Map<String, String>      picFilePaths;

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

    /** 按钮 **/
    private Map<String, ImageButton> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // 1.调用父类的方法
        super.onCreate (savedInstanceState);

        // 2.初始化uiview
        initUIView ();

        // 3.初始化数据
        initData ();

        // 4.注册事件监听器
        registerListener ();
    }

    /**
     * 注册事件监听器
     */
    private void registerListener(){
        // 1.左导航按钮
        check_after_pic_nav_bar_left_btn.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v){
                finish ();
                CommonUtils.outAnim ();
            }
        });
        washcar_after_1_0_0.setOnClickListener (this);
        washcar_after_1_0_1.setOnClickListener (this);
        washcar_after_1_0_2.setOnClickListener (this);
        washcar_after_1_0_3.setOnClickListener (this);
        washcar_after_1_0_4.setOnClickListener (this);
        washcar_after_1_0_5.setOnClickListener (this);
        washcar_after_1_0_6.setOnClickListener (this);
        washcar_after_1_0_7.setOnClickListener (this);
        washcar_after_1_0_8.setOnClickListener (this);
    }

    @Override
    public void onClick(View v){
        if (v instanceof ImageButton) {
            ImageButton btn = (ImageButton) v;
            String tag = (String) btn.getTag ();
            String picUrl = picFilePaths.get (tag);
            if (!StringUtils.isEmpty (picUrl)) {
                Bundle bundle = new Bundle ();
                bundle.putString ("picUrl", picUrl);
                redirectToTargetActivity (CheckAfterPicActivity.this, SinglePicCheckActivity.class, bundle, SwipeDirect.DIRECT_NONE);
                CommonUtils.fadeAlphaAnim ();
            }
        }
    }

    /**
     * 初始化数据
     */
    private void initData(){
        // 1.获取订单信息
        Bundle bundle = getIntent ().getExtras ();
        orderItem = (OrderQueryDetailBean) bundle.get ("orderItem");

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
     * 初始化之前拍照的图片
     */
    private void initPrePicsInfoList(){
        List<WashcarPicInfoDaoModel> dataList = queryPicInfoList (AppConstant.washcar_after_type);
        for ( WashcarPicInfoDaoModel washcarPicInfoDaoModel : dataList ) {
            int type = washcarPicInfoDaoModel.getPicType ();
            int subtype = washcarPicInfoDaoModel.getPicSubtype ();
            int index = washcarPicInfoDaoModel.getPicIndex ();
            String key = String.valueOf (type) + "_" + String.valueOf (subtype) + "_" + String.valueOf (index);
            String filePath = StringUtils.trimNull (washcarPicInfoDaoModel.getPicUrl ());
            String thumbnailPath = StringUtils.trimNull (washcarPicInfoDaoModel.getThumbnailPath ());
            if (StringUtils.isEmpty (filePath)) {
                continue;
            }
            if (picFilePaths == null) {
                picFilePaths = new HashMap<String, String> ();
            }
            picFilePaths.put (key, StringUtils.trimNull (filePath));
            ImageButton button = buttons.get (key);
            BitmapCache.getInstance (this).displayBmpByFilePath (button, thumbnailPath, new ImageCallback () {

                @Override
                public void imageLoad(ImageView imageView,Bitmap bitmap,Object... params){
                    imageView.setImageBitmap (bitmap);
                }

                @Override
                public Bitmap getBitMapFromFile(String sourcePath){
                    return PictureUtils.zoomBitmap (PictureUtils.getBitmapByPath (sourcePath), CommonUtils.dip2px (getApplicationContext(), 88), CommonUtils.dip2px (getApplicationContext(), 70));
                }
            });
        }
    }

    /**
     * 查询洗车后拍照的照片
     * @param washcarBeforeType
     * @return
     */
    private List<WashcarPicInfoDaoModel> queryPicInfoList(int picType){
        WashcarPicInfoDaoModel picInfo = new WashcarPicInfoDaoModel ();
        RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
        String orderTime = orderItem.getOrderTime ();
        picInfo.setPicDate (orderTime.substring (0, 8));// 日期
        picInfo.setRunmanid (runMan.getRunManId ());// 跑男ID
        picInfo.setOrderid (orderItem.getOrderId ());// 订单号
        picInfo.setPicType (picType);
        return WashcarPicInfoDao.newInstance (this).queryPicInfoList (picInfo);
    }

    /**
     * 初始化uiview
     */
    private void initUIView(){
        // 1.布局文件
        setContentView (R.layout.check_after_pic_info_activity);

        // 2.导航左按钮
        check_after_pic_nav_bar_left_btn = (Button) findViewById (R.id.check_after_pic_nav_bar_left_btn);

        // 3.拍照按钮
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

    }

}
