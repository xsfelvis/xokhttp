package xsf.xokhttp.util;

import android.content.Context;

import xsf.xokhttp.BuildConfig;
import xsf.xokhttp.net.BaseRequest;
import xsf.xokhttp.net.FileResultCallBack;
import xsf.xokhttp.net.HttpUtil;

/**
 * Author: xsfelvis
 * Time: created at 2016/9/4.
 */
public class FileHttpUtil {
    private static final boolean DEV = BuildConfig.FLAVOR.equals("dev");



    public static void sendRequest(BaseRequest baseRequest, final FileResultCallBack callBack) {
        if (baseRequest == null) {
            return;
        }
        HttpUtil.getInstance().sendRequest(null, baseRequest, callBack);
    }


    public static void cancelRequests(Context context) {
        HttpUtil.getInstance().cancelRequests(context);
    }
}
