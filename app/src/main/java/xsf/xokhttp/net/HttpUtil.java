package xsf.xokhttp.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import xsf.xokhttp.BuildConfig;
import xsf.xokhttp.MyApplication;
import xsf.xokhttp.R;
import xsf.xokhttp.util.Tools;


/**
 * Author: xsfelvis
 * Time: created at 2016/8/28.
 */
public class HttpUtil {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private final Handler mHandler;
    private static volatile HttpUtil mInstance;
    private OkHttpClient mOkHttpClient;
    protected final Map<Context, List<Call>> requestMap = Collections.synchronizedMap(new WeakHashMap<Context, List<Call>>());

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HttpUtil() {
        mHandler = new Handler(Looper.getMainLooper());
        if (mOkHttpClient == null) {
            if (BuildConfig.DEBUG) {
                mOkHttpClient = getUnsafeOkHttpClient();
            } else {
                mOkHttpClient = new OkHttpClient();
            }

            mOkHttpClient = mOkHttpClient.newBuilder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    if (chain.request().tag() instanceof BaseRequest) {
                        return ((BaseRequest) chain.request().tag()).intercept(chain);
                    }
                    return chain.proceed(chain.request());
                }
            }).build();
        }
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static HttpUtil getInstance() {
        if (mInstance == null)
            synchronized (HttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtil();
                }
            }
        return mInstance;
    }

    public void sendRequest(final Context context, BaseRequest request, final ResultCallBack callBack) {
        sendRequest(context, request, request.getCallback(context, mHandler, callBack));
    }

    protected void sendRequest(final Context context, BaseRequest request, final Icallback callBack) {
        if (request == null) {
            return;
        }
        final Call call = mOkHttpClient.newCall(request.getRequest().build());
        addToRequestMap(context, call);
        call.enqueue(callBack);
    }

    protected void addToRequestMap(Context context, Call call) {
        if (context != null) {
            // Add request to request map
            List<Call> requestList = requestMap.get(context);
            synchronized (requestMap) {
                if (requestList == null) {
                    requestList = Collections.synchronizedList(new LinkedList<Call>());
                    requestMap.put(context, requestList);
                }
            }
            requestList.add(call);
            Iterator<Call> iterator = requestList.iterator();
            while (iterator.next().isCanceled()) {
                iterator.remove();
            }
        }
    }

    protected void removeCall(Context context, Call call) {
        List<Call> requestList = requestMap.get(context);
        if (requestList != null) {
            requestList.remove(call);
        }
    }

    //取消同一个页面的所有请求
    public void cancelRequests(final Context context) {
        if (context == null) {
            Log.e("HTTPUTIL", "Passed null Context to cancelRequests");
            return;
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<Call> requestList = requestMap.get(context);
                if (requestList != null) {
                    requestMap.remove(context);
                    for (Call call : requestList) {
                        call.cancel();
                    }
                }
            }
        };
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(r).start();
        } else {
            r.run();
        }
    }

    //取消app所有请求
    public void cancelAllRequests() {
        for (List<Call> requestList : requestMap.values()) {
            if (requestList != null) {
                for (Call call : requestList) {
                    call.cancel();
                }
            }
        }
        requestMap.clear();
    }


    public static HashMap<String, String> parse(String src) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (!Tools.isEmpty(src)) {
            String[] kv = src.split("&");
            for (int i = 0; i < kv.length; i++) {
                String[] temp = kv[i].split("=");
                map.put(temp[0], temp[1]);
            }
        }
        return map;
    }

    public static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static ResponseBody createBody(String result) {
        return ResponseBody.create(MediaType.parse("application/x-www-form-urlencoded"), result);
    }


    /**
     * 加载图片
     *
     * @param url the url of image
     * @param iv  ImageView
     */
    public void loadImage(String url, ImageView iv) {
        loadImage(url, iv, false);
    }

    public void loadImage(String url, ImageView iv, boolean isCenterCrop) {
        loadImageWithHolder(url, iv, R.mipmap.ic_default, isCenterCrop);
    }

    /**
     * 加载图片
     *
     * @param url              the url of image
     * @param iv               ImageView
     * @param placeholderResID default image
     */
    public void loadImageWithHolder(String url, ImageView iv, int placeholderResID, boolean isCenterCrop) {
        Picasso.with(MyApplication.getApplication()).load(url).placeholder(placeholderResID).fit().into(iv);
        RequestCreator creator = Picasso.with(MyApplication.getApplication()).load(url).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_image_loadfail);
        if (isCenterCrop) {
            creator.centerCrop();
        }
        creator.fit().into(iv);
    }


}
