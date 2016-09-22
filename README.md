# xokhttp
封装的okhtp，

- 使用gson+泛型直接解析，回调直接使用
- 支持普通网络请求（post/get）
- 支持文件下载

#Useage

构建一个下载工具类统一管理请求，比如
    `public class XOkHttpUtil {

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

`

然后在正式调用的时候XokhttpTestActviy中 直接调用，可以看出回调函数已经帮将json串解析好了

    `        XOkHttpUtil.getWeater(this, new ResultCallBack<WeatherNew>() {
            @Override
            public void onSucess(WeatherNew weather) {

                tvShow1.setText("当前城市：" + weather.currentCity +
                        "PM2.5:" + weather.pm25 +
                        "小贴士：" + weather.index.get(2).des +
                        "天气指数" + weather.weather_data.get(1).weather
                );

            }

            @Override
            public void onFail(BaseResponse response) {
                Toast.makeText(MVCActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
            }
        });`

#Notice

json串必须是有对象名，如
>{
>
>code：“200”
>
>msg：“sucess"
>
>data:{
>
    }

>}

在BaseResopnse也可以看出，做的注解处理，记得保持一直即可

    ` @SerializedName(value = "code", alternate = {"retcode", "operationResp", "error"})
    public String code;
    @SerializedName(value = "msg", alternate = {"detailMsg", "retdesc"})
    public String msg;
    @SerializedName(value = "result", alternate = {"data"})`

