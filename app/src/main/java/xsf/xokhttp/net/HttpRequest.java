package xsf.xokhttp.net;

/**
 * Author: hzxushangfei
 * Time: created at 2016/8/29.
 * Copyright 2016 Netease. All rights reserved.
 */
public class HttpRequest extends BaseRequest {
    final public static String RET_CODE_SUCCESS = "0000";

    public HttpRequest(String url) {
        super(url);
        //addCommParams();
    }

    @Override
    public String createUrl(String url) {
        return url;
        //return URL_HOST + StringUtils.defaultString(urlSuffix);
    }

    @Override
    public String getSalt() {
        return null;
    }

    @Override
    public boolean isSuccess(BaseResponse response) {
        return true;
        //return TextUtils.equals(RET_CODE_SUCCESS, response.code);;
    }


   /* protected void addCommParams() {
        addParam(LCUrl.CommonKeys.device_id_keys, DeviceInfo.getInstance().getDeviceId());
        addParam(LCUrl.CommonKeys.mobile_os_type_keys, "2");//2 is android 1 is ios
        addParam(LCUrl.CommonKeys.client_version_keys, BuildConfig.VERSION_NAME);
        addParam(LCUrl.CommonKeys.CHANNEL, AppConfig.channel);
        SimpleDateFormat sdf = new SimpleDateFormat(REQUEST_TIME_PATTERN);
        addParam(LCUrl.CommonKeys.request_time_Keys, sdf.format(new Date()));
        String loginId = Session.getInstance().getLoginId();
        String token = Session.getInstance().getLoginToken();

        if (!Tools.isEmpty(loginId))
            addParam(LCUrl.CommonKeys.LOGIN_ID, loginId);
        if (!Tools.isEmpty(token))
            addParam(LCUrl.CommonKeys.LOGIN_TOKEN, token);
    }*/
}
