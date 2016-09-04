package xsf.xokhttp;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import xsf.xokhttp.base.BaseActvity;
import xsf.xokhttp.bean.WeatherNew;
import xsf.xokhttp.bean.weather.Weather;
import xsf.xokhttp.net.BaseResponse;
import xsf.xokhttp.net.FileDownloadRequest;
import xsf.xokhttp.net.FileResultCallBack;
import xsf.xokhttp.net.ResultCallBack;
import xsf.xokhttp.util.AppLog;
import xsf.xokhttp.util.FileHttpUtil;
import xsf.xokhttp.util.GsonUtil;
import xsf.xokhttp.util.HttpUtilOld;
import xsf.xokhttp.util.Tools;
import xsf.xokhttp.util.XOkHttpUtil;

public class XokhttpTestActivity extends BaseActvity {
    private TextView tvShow;
    private TextView tvShow1;
    private Weather weather = new Weather();
    private static final String DIR = "apatch";
    private static final String APATCH_PATH = "/out.apatch";
    private File mPatchDir;


    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_mvc;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle("MVC");
        tvShow = IfindViewById(R.id.tvShowInfo);
        tvShow1 = IfindViewById(R.id.tvShowInfo1);
        IfindViewById(R.id.btn_getInfo).setOnClickListener(this);
        IfindViewById(R.id.btn_getInfo1).setOnClickListener(this);
        IfindViewById(R.id.btn_getfile).setOnClickListener(this);
        mPatchDir = new File(getFilesDir(), DIR);
        if (!mPatchDir.exists()) {
            mPatchDir.mkdirs();
        }
        AppLog.d("FILE1", mPatchDir.getAbsolutePath());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getInfo:
                showWeatherInfo();
                break;
            case R.id.btn_getInfo1:
                showWeatherInfo1();
                break;
            case R.id.btn_getfile:
                getFile();
                break;
        }
    }

    /**
     * 下载文件
     */
    private void getFile() {
        FileDownloadRequest downloadRequest = new FileDownloadRequest(Url.WEATHER_URL_TEST);
        FileHttpUtil.sendRequest(downloadRequest, new FileResultCallBack<File>() {
            @Override
            public BaseResponse<File> handleFile(ResponseBody body) {
                BaseResponse response = new BaseResponse("0000", "下载成功");
                try {
                    if (mPatchDir.exists()) {
                        File dest = new File(mPatchDir, APATCH_PATH);
                        AppLog.d("File2", dest.getAbsolutePath());
                        Tools.copy(body.byteStream(), dest);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                response.result = mPatchDir;
                return response;
            }

            @Override
            public void onSucess(File file) {
                AppLog.d("FILE3", file.getAbsolutePath());
                Toast.makeText(XokhttpTestActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(BaseResponse response) {
                Toast.makeText(XokhttpTestActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 新的结合Gson泛型的下载方式
     */
    private void showWeatherInfo1() {


        XOkHttpUtil.getWeater(this, new ResultCallBack<WeatherNew>() {
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
                Toast.makeText(XokhttpTestActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 旧的网络下载方式
     */
    private void showWeatherInfo() {
        HttpUtilOld.getInstance().get(Url.WEATHER_URL, new HttpUtilOld.HttpCallBack() {
            @Override
            public void onLoading() {

            }
            @Override
            public void onSuccess(String result) {
                weather = (Weather) GsonUtil.jsonToObject(result, Weather.class);
                tvShow.setText("weather.status: " + weather.status + "\n"
                        + "city: " + weather.results.get(0).currentCity + "\n"
                        + "天气描述 :" + weather.results.get(0).index.get(3).des + "\n"
                        + "温度：" + weather.results.get(0).weather_data.get(2).temperature + "\n"
                );
            }

            @Override
            public void onError(Exception e) {

            }
        });


    }


}
