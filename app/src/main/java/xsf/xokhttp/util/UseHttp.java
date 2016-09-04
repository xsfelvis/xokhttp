package xsf.xokhttp.util;

import android.content.Context;

import java.io.File;

import xsf.xokhttp.Url;
import xsf.xokhttp.bean.WeatherNew;
import xsf.xokhttp.net.BaseRequest;
import xsf.xokhttp.net.BaseResponse;
import xsf.xokhttp.net.FileDownloadRequest;
import xsf.xokhttp.net.HttpUtil;
import xsf.xokhttp.net.ResultCallBack;

/**
 * Author: hzxushangfei
 * Time: created at 2016/9/4.
 * Copyright 2016 Netease. All rights reserved.
 */
public class UseHttp {

    private static void sendRequest(final Context context, BaseRequest requestData, final ResultCallBack callBack) {
        if (requestData == null) {
            return;
        }
        HttpUtil.getInstance().sendRequest(context, requestData, callBack);
    }

    private static BaseRequest createRequest(String url) {
        return BaseRequest.createRequest(url, BaseRequest.HttpType.HTTP);
    }


    /**
     * 下载网络数据
     * @param context
     * @param resultCallBack
     */
    public static void getWeater(final Context context, final ResultCallBack<WeatherNew> resultCallBack) {
        BaseRequest request = createRequest(Url.WEATHER_URL_LOACL);
        request.setAction(Url.WEATHER_URL_LOACL);
        request.setMethod(BaseRequest.Method.GET);
        sendRequest(context, request, new ResultCallBack<WeatherNew>() {

            @Override
            public void onSucess(WeatherNew weather) {
                if (resultCallBack != null) {
                    resultCallBack.onSucess(weather);
                }
            }

            @Override
            public void onFail(BaseResponse response) {
                if (resultCallBack != null) {
                    resultCallBack.onFail(response);
                }

            }
        });
    }

    public static void getFile(final  Context context, final ResultCallBack<File> resultCallBack){
        FileDownloadRequest downloadRequest = new FileDownloadRequest(Url.WEATHER_URL_TEST);

    }


}
