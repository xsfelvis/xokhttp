package xsf.xokhttp.net;

import okhttp3.ResponseBody;


/**
 * Author: xsfelvis
 * Time: created at 2016/8/28.
 */
public abstract class FileResultCallBack<T> extends ResultCallBack<T> {

    public abstract BaseResponse<T> handleFile(ResponseBody body);
}

