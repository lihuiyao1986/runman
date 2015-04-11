package com.clt.runman.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.clt.runman.R;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DateUtils;
import com.clt.runman.utils.MD5;

/**
 *@Description:获取客户端信息
 *@Author:张聪
 *@Since:2014年12月27日下午12:33:00
 */
public class ReqHeaderBean implements Serializable {

    private static final long serialVersionUID = 2504136036556454365L;
    // 版本
    private String            version;
    // 随机数
    private String            random;
    // 时间戳
    private String            timestamp;
    // 请求url
    private String            url;
    // 签名串
    private String            sign;
    // 令牌
    private String            token;
    // 唯一串
    private String            uuid;
    // 应用程序对象
    private Context           context;

    public ReqHeaderBean(String reqUrl,Context ctx) {
    	this.context = ctx;
        String separator = "|";
        String key = this.context.getResources ().getString (R.string.sign_key);
        this.url = reqUrl;
        this.version = this.context.getResources ().getString (R.string.server_version);
        this.random = String.valueOf (new Random ().nextInt (999) + 1);
        this.timestamp = DateUtils.getNowTimeStamp ();
        this.token = CommonUtils.getRunmanUUID(context);
        this.uuid = CommonUtils.uuid ();
        StringBuffer sb = new StringBuffer ();
        sb.append (this.version).append (separator).append (this.timestamp).append (separator).append (token).append (separator).append (this.random)
                .append (separator).append (this.uuid).append (separator).append (this.url);
        this.sign = MD5.md5 (MD5.md5 (sb.toString ()) + MD5.md5 (key));
    }

    public String getVersion(){
        return version;
    }

    public void setVersion(String version){
        this.version = version;
    }

    public String getRandom(){
        return random;
    }

    public void setRandom(String random){
        this.random = random;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getSign(){
        return sign;
    }

    public void setSign(String sign){
        this.sign = sign;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getUuid(){
        return uuid;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    // 获取请求参数
    public List<NameValuePair> getReqHeaderParams(){
        List<NameValuePair> headerParams = new ArrayList<NameValuePair> ();
        headerParams.add (new BasicNameValuePair ("randomOnce",random));
        headerParams.add (new BasicNameValuePair ("version",version));
        headerParams.add (new BasicNameValuePair ("token",token));
        headerParams.add (new BasicNameValuePair ("signature",sign));
        headerParams.add (new BasicNameValuePair ("timestamp",timestamp));
        headerParams.add (new BasicNameValuePair ("uuid",uuid));
        headerParams.add (new BasicNameValuePair ("methodUrl",this.url));
        return headerParams;
    }

    @Override
    public String toString(){
        return "ReqHeaderBean{" + "version='" + version + '\'' + ", random='" + random + '\'' + ", timestamp='" + timestamp + '\'' + ", url='" + url + '\''
                + ", sign='" + sign + '\'' + ", token='" + token + '\'' + ", uuid='" + uuid + '\'' + '}';
    }
}
