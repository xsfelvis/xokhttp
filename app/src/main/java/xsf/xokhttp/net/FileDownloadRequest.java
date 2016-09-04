package xsf.xokhttp.net;

import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: hzxushangfei
 * Time: created at 2016/8/28.
 * Copyright 2016 Netease. All rights reserved.
 */
public class FileDownloadRequest extends BaseRequest{
    private ProgressListener progressListener;
    public FileDownloadRequest(String url) {
        super(url);
        setMethod(Method.GET);
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public String createUrl(String url) {
        return url;
    }

    @Override
    public Request.Builder getRequest() {
        return super.getRequest().header("Cache-Control", "max-age=0");//通过服务器验证缓存数据是否有效
    }

    @Override
    public String getSalt() {
        return "";
    }

    @Override
    public boolean isSuccess(BaseResponse response) {
        if (response.result != null && response.result instanceof File) {
            return true;
        }
        return false;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        if (progressListener == null) {
            return super.intercept(chain);
        }
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                .build();
    }
}
