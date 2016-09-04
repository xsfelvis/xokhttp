package xsf.xokhttp.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Callback;

/**
 * Author: hzxushangfei
 * Time: created at 2016/8/28.
 * Copyright 2016 Netease. All rights reserved.
 */
public interface Icallback<T> extends Callback {
    void handleResult(String result, T response);

    T handleResponse(String body, Gson gson, Type type) throws IOException;
}
