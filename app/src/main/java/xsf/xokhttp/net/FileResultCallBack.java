package xsf.xokhttp.net;

import okhttp3.ResponseBody;


/**
 * Author: hzxushangfei
 * Time: created at 2016/8/28.
 * Copyright 2016 Netease. All rights reserved.
 */
public abstract class FileResultCallBack<T> extends ResultCallBack<T> {

    public abstract BaseResponse<T> handleFile(ResponseBody body);
}

