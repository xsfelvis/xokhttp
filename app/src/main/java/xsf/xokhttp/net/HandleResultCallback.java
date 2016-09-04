package xsf.xokhttp.net;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Author: hzxushangfei
 * Time: created at 2016/8/28.
 * Copyright 2016 Netease. All rights reserved.
 */
public class HandleResultCallback implements Icallback<BaseResponse> {
    public final Handler handler;
    protected ResultCallBack callBack;
    protected BaseRequest request;
    protected Context context;

    public HandleResultCallback(Context context, Handler handler, ResultCallBack callBack, BaseRequest request) {
        this.callBack = callBack;
        this.request = request;
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void handleResult(String result, final BaseResponse response) {
        if (request.isSuccess(response)) {
            callBack.saveResult(result);
            if (request.interceptResponse(context, response)) {
                return;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onSucess(response.result);
                }
            });
        } else {
            if (request.interceptResponse(context, response)) {
                return;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //跟ResultCallBack的onFail回调挂钩
                    callBack.onFail(response);
                }
            });
        }

    }

    @Override
    public BaseResponse handleResponse(String body, Gson gson, Type type) throws IOException {
        TypeAdapter<BaseResponse> adapter = gson.getAdapter(BaseResponse.class);
        BaseResponse response = adapter.fromJson(body);
        if (type == Void.class) {
            return response;
        }
        boolean isResponse;
        if (type instanceof ParameterizedType) {
            //如果是泛型类
            isResponse = BaseResponse.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType());
        } else {
            isResponse = BaseResponse.class.isAssignableFrom((Class<?>) type);
        }
        if (isResponse) {
            response.result = gson.getAdapter(TypeToken.get(type)).fromJson(body);
        } else {
            response.result = gson.fromJson(gson.toJson(response.result), type);
        }
        return response;
    }


    @Override
    public void onFailure(Call call, IOException e) {
        final BaseResponse result = request.getErrResponse(e);
        if (request.interceptResponse(context, result)) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFail(result);
            }
        });

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            parseResponse(call, response);
        } catch (IOException e) {
            onFailure(call, e);
        }
    }


    public void parseResponse(Call call, Response rawResponse) throws IOException {
        int code = rawResponse.code();
        if (code >= 200 && code < 300) {
            //response转换成实际的类型
            callBack.handResult(this, rawResponse.body());
        } else {
            onFailure(call, new IOException());
            rawResponse.body().close();
        }
    }


}
