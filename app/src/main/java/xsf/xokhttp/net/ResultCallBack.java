package xsf.xokhttp.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import xsf.xokhttp.util.AppLog;
import xsf.xokhttp.util.Tools;


/**
 * Author: xsfelvis
 * Time: created at 2016/8/28.
 */
public abstract class ResultCallBack<T> {
    public Gson gson = new GsonBuilder().create();

    public ResultCallBack() {

    }

    public abstract void onSucess(T t);

    public abstract void onFail(BaseResponse response);

    public void handResult(HandleResultCallback handleResultCallback, ResponseBody body) {
        Type type = this.getClass().getGenericSuperclass();
        Type oneType = Void.class;
        if (type instanceof ParameterizedType && ((ParameterizedType) type).getActualTypeArguments().length >= 1) {
            oneType = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (File.class == oneType) {
                handleResultCallback.handleResult("", handleFile(body));
                return;
            }
        }
        //正常网络下载
        try {
            String content = body.string();
            AppLog.d(AppLog.HTTPTAG, content);
            handleResultCallback.handleResult(content, handleResultCallback.handleResponse(content, gson, oneType));
        } catch (IOException e) {
            AppLog.d(AppLog.HTTPTAG, e.getLocalizedMessage());
            handleResultCallback.handleResult("", new BaseResponse(BaseRequest.NET_RESOLVE_ERROR, "未知异常"));

        } finally {
            body.close();
        }

    }

    public BaseResponse<T> handleFile(ResponseBody body) {
        File file = new File(Tools.getDiskCacheDir(), "tmp.apk");
        try {
            Tools.copy(body.byteStream(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BaseResponse response = new BaseResponse("0000", "下载成功");
        response.result = file;
        return response;

    }

    public void saveResult(String result) {

    }
}
