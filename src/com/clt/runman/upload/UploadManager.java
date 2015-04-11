package com.clt.runman.upload;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.AsyncTask.Status;

import com.clt.runman.db.dao.WashcarPicInfoDao;
import com.clt.runman.db.model.WashcarPicInfoDaoModel;
import com.clt.runman.model.RunMan;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.MyLog;

/**
 * 
* @ClassName: UploadManager 
* @Description: TODO(图片上传任务管理器) 
* @author fuxianwei 
* @date 2015-4-7 下午4:01:27 
*
 */
public class UploadManager {

    private static final String                         TAG  = UploadManager.class.getSimpleName ();

    private static UploadManager                        INSTANCE;
    // 同步锁
    private static final Object                         SYNC = new Object ();

    // 等待上传任务队列
    private final BlockingQueue<WashcarPicInfoDaoModel> mPrepareQueue;
    // 已上传或正在上传队列
    private final BlockingQueue<WashcarPicInfoDaoModel> mRunningQueue;

    // 上下文对象
    private final Context                               mContext;

    private UploadTask                                  mUploadTask;

    public static UploadManager getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (SYNC) {
                if (INSTANCE == null) INSTANCE = new UploadManager (context);
            }
        }
        return INSTANCE;
    }

    private UploadManager(Context context) {
        // 初始化任务队列
        mPrepareQueue = new LinkedBlockingDeque<WashcarPicInfoDaoModel> (20);
        mRunningQueue = new LinkedBlockingDeque<WashcarPicInfoDaoModel> (5);
        this.mContext = context;
    }

    /**
     * 
    * @Title: initQueues 
    * @Description: TODO(初始化任务队列) 
    * @param     
    * @return void    
    * @throws
     */
    public void initQueues(){
        mPrepareQueue.clear ();
        RunMan runMan = CommonUtils.getLoginedRunManInfo (mContext);
        // 判断是否已登录,如果未登录,则不查询本地数据
        if (runMan == null) return;
        List<WashcarPicInfoDaoModel> list = WashcarPicInfoDao.newInstance (mContext).queryAllWashCarAfterListByRunManId (runMan.getRunManId ());
        // 判断是否有未上传的图片记录,如果有则添加到上传任务队列
        if (list != null && !list.isEmpty ()) {
            for ( WashcarPicInfoDaoModel model : list ) {
                mPrepareQueue.offer (model);
            }
        }
    }

    /**
     * 
    * @Title: poll 
    * @Description: TODO(移除并返回队列第一个元素,在5秒内取出,如果不能取出,则返回null) 
    * @param @return    
    * @return WashcarPicInfoDaoModel    
    * @throws
     */
    public WashcarPicInfoDaoModel poll(){
        try {
            return mPrepareQueue.poll (5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        return null;
    }

    /**
     * 
    * @Title: listQueues 
    * @Description: TODO(返回等待中的任务列表) 
    * @param @return    
    * @return WashcarPicInfoDaoModel[]    
    * @throws
     */
    public WashcarPicInfoDaoModel[] listQueues(){
        WashcarPicInfoDaoModel[] arrays = (WashcarPicInfoDaoModel[]) mPrepareQueue.toArray ();
        return arrays;
    }

    /**
     * 
    * @Title: listRunningQueues 
    * @Description: TODO(返回正在进行的任务列表) 
    * @param @return    
    * @return WashcarPicInfoDaoModel[]    
    * @throws
     */
    public WashcarPicInfoDaoModel[] listRunningQueues(){
        WashcarPicInfoDaoModel[] arrays = (WashcarPicInfoDaoModel[]) mRunningQueue.toArray ();
        return arrays;
    }

    /**
     * 
    * @Title: getQueueCount 
    * @Description: TODO(返回任务剩余总数) 
    * @param @return    
    * @return int    
    * @throws
     */
    public int getQueueCount(){
        return mPrepareQueue.size ();
    }

    /**
     * 
    * @Title: offer 
    * @Description: TODO(向任务列表追加任务) 
    * @param @param model
    * @param @return    
    * @return boolean    
    * @throws
     */
    public boolean offer(WashcarPicInfoDaoModel model){
        return mRunningQueue.offer (model);
    }

    /**
     * 
    * @Title: remove 
    * @Description: TODO(任务已完成,移除队列) 
    * @param @param model
    * @param @return    
    * @return boolean    
    * @throws
     */
    public boolean remove(WashcarPicInfoDaoModel model){
        return mRunningQueue.remove (model);
    }

    /**
     * 
    * @Title: startUpload 
    * @Description: TODO(开始上传) 
    * @param     
    * @return void    
    * @throws
     */
    public void startUpload(){
        synchronized (SYNC) {
            if (mUploadTask == null) {
                mUploadTask = new UploadTask (mContext);
            }
            if (mUploadTask.getStatus () == Status.FINISHED) {
                MyLog.d (TAG, "当前任务已执行完成,重新启动任务");
                mUploadTask = new UploadTask (mContext);
                mUploadTask.execute ();
            } else if (mUploadTask.getStatus () == Status.RUNNING) {
                MyLog.d (TAG, "当前任务正在进行,不需要重新执行");
            } else if (mUploadTask.getStatus () == Status.PENDING) {
                MyLog.d (TAG, "当前任务等待被执行");
                mUploadTask.execute ();
            } else {
                mUploadTask.execute ();
            }
        }
    }

    /**
     * 
    * @Title: stopUpload 
    * @Description: TODO(停止上传线程) 
    * @param     
    * @return void    
    * @throws
     */
    public void stopUpload(){
        MyLog.d (TAG, "执行stopUpload方法");
        if (mUploadTask != null) {
            mUploadTask.cancel (true);
        }
        mUploadTask = null;
    }
}
