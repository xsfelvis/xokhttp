package xsf.xokhttp.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Callback;

/**
 * Author: xsfelvis
 * Time: created at 2016/8/28.
 */
public interface Icallback<T> extends Callback {
    void handleResult(String result, T response);

    T handleResponse(String body, Gson gson, Type type) throws IOException;
}
