package xsf.xokhttp.net;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.RequestBody;
import okhttp3.Response;
import xsf.xokhttp.util.AppLog;
import xsf.xokhttp.util.DigestUtils;


/**
 * Author: xsfelvis
 * Time: created at 2016/8/28.
 */
public abstract class BaseRequest {
    public static final String NET_CONNECTION_ERROR = "1112";
    public static final String NET_RESOLVE_ERROR = "1111";
    public static final String REQUEST_PARAM_DIGEST = "sign";
    public static final String REQUEST_PARAM_MESSAGE = "msg";
    protected RequestParams getParams = new RequestParams();
    protected RequestParams postParams = new RequestParams();
    protected HashMap<String, Object> mRequestPairs = new HashMap<>();//将转成json并进行加密
    private String url;
    private Method method = Method.POST;
    protected final Gson gson = new Gson();
    private boolean isCache = false;

    protected okhttp3.Request.Builder request;

    public BaseRequest(String url) {
        this.url = createUrl(url);
        // AppLog.d("REQUEST_URL", this.url);
        request = new okhttp3.Request.Builder();
    }

    public okhttp3.Request.Builder getRequest() {
        if (method == Method.POST) {
            AppLog.d("BaseRequest", "post");
            postData();//此处默认使用post
        } else if (method == Method.GET) {
            AppLog.d("BaseRequest", "get");
            get();
        }
        //return request.tag(this).url(getUrl());
        return request.tag(this).url(getOriginUrl());
    }

    public BaseRequest setMethod(Method method) {
        this.method = method;
        return this;
    }

    protected BaseRequest postData() {
        String encode = getMessage();
        RequestBody formBody = new FormBody.Builder()
                .add(REQUEST_PARAM_MESSAGE, encode)
                //.add(REQUEST_PARAM_DIGEST, getSign(encode))
                .add(REQUEST_PARAM_DIGEST, encode)
                .build();
        request.post(formBody);
        return this;
    }

    protected BaseRequest get() {
        request.get();
        return this;
    }

    public void setAction(String action) {
        mRequestPairs.put("action", action);
    }

    public BaseRequest addParam(String key, Object value) {
        if (key != null && value != null) mRequestPairs.put(key, value);
        return this;
    }

    public BaseRequest addParam(HashMap map) {
        if (map != null && map.size() > 0) {
            mRequestPairs.putAll(map);
        }
        return this;
    }

    public BaseRequest addPostParam(String key, String value) {
        if (key == null)
            return this;
        if (value == null) {
            postParams.add(key, "");
        } else {
            postParams.add(key, value);
        }
        return this;
    }

    public BaseRequest addPostParam(Map<String, String> map) {
        if (map != null && map.size() > 0) postParams.add(map);
        return this;
    }

    public BaseRequest addGetParam(Map<String, String> map) {
        if (map != null && map.size() > 0) getParams.add(map);
        return this;
    }

    public BaseRequest addGetParam(String key, String value) {
        if (key == null)
            return this;
        if (value == null) {
            getParams.add(key, "");
        } else {
            getParams.add(key, value);
        }
        return this;
    }

    public String getOriginUrl() {
        return url;
    }

    public String getUrl() {
        if (!getParams.isEmpty()) {
            if (url.indexOf('?') > 0) {
                return url + "&" + getParams.toString();
            }
            return url + "?" + getParams.toString();
        } else
            return url;
    }

    protected String getSign(String encoded) {
        if (mRequestPairs.size() == 0) {
            return null;
        }
        return DigestUtils.digest(encoded, getSalt());
    }

    protected String getMessage() {
        if (mRequestPairs.size() == 0) {
            return null;
        }
        AppLog.d(AppLog.HTTPTAG, "sendRequest=======" + mRequestPairs.toString());
        String encodedStr = null;
        try {
            encodedStr = gson.toJson(mRequestPairs);
            //encodedStr = Base64.encode(gson.toJson(mRequestPairs).getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppLog.d(AppLog.HTTPTAG, "msg=======" + encodedStr);
        return encodedStr;
    }

    public abstract String createUrl(String url);

    public abstract String getSalt();

    public abstract boolean isSuccess(BaseResponse response);

    /**
     * 结果拦截，当activity被关闭或者context为空时拦截ui返回
     *
     * @param context
     * @param response
     * @return
     */
    public boolean interceptResponse(Context context, BaseResponse response) {
        if (context != null && (context instanceof Activity && ((Activity) context).isFinishing())) {
            return true;
        }
        return false;
    }

    public Icallback getCallback(Context context, Handler handler, ResultCallBack callBack) {
        return new HandleResultCallback(context, handler, callBack, this);
    }

    public BaseResponse getErrResponse(Exception e) {
        if (e != null && (e instanceof UnknownHostException || e instanceof ConnectException)) {
            return new BaseResponse(NET_CONNECTION_ERROR, "网络连接失败");
        } else {
            return new BaseResponse(NET_RESOLVE_ERROR, "网络繁忙，请稍后再试");
        }
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }

    public HashMap<String, Object> getmRequestPairs() {
        return mRequestPairs;
    }

    public enum Method {
        GET,
        POST
    }

    /**
     * 服务接口类型
     */
    public enum HttpType {
        HTTP,//普通网络
        FILEDOWM,  //文件下载

    }

    /**
     * 根据接口类型获取请求request
     *
     * @param url
     * @param httpType
     * @return
     */
    public static BaseRequest createRequest(String url, HttpType httpType) {
        if (httpType == HttpType.HTTP) {
            return new HttpRequest(url);
        } else if (httpType == HttpType.FILEDOWM) {
            return new FileDownloadRequest(url);
        }
        return null;
    }


    public void setIsCache(boolean isCache) {
        this.isCache = isCache;
    }

    public boolean isCache() {
        return isCache;
    }
}
