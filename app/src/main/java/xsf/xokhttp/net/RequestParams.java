package xsf.xokhttp.net;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Author: xsfelvis
 * Time: created at 2016/8/28.
 */
public class RequestParams {
    private StringBuffer params = new StringBuffer();
    private HashMap<String,String> paramsMap = new HashMap<>();
    public final static String APPLICATION_OCTET_STREAM =
            "application/octet-stream";

    public synchronized void add(String name, String value) {
        paramsMap.put(name,value);
    }

    public synchronized void add(Map<String, String> map) {
        paramsMap.putAll(map);
    }

    public boolean isEmpty(){
        return paramsMap.size() == 0;
    }

    private synchronized void encode(String name, String value) {
        try {
            params.append(URLEncoder.encode(name, "UTF-8"));
            params.append("=");
            params.append(URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("do not support UTF-8");
        }
    }

    public void clear(){params = new StringBuffer();};

    public String getParams() {
        clear();
        Set<String> mapSet =  paramsMap.keySet();	//获取所有的key值 为set的集合
        Iterator<String> itor =  mapSet.iterator();//获取key的Iterator便利
        while(itor.hasNext()){//存在下一个值
            String key = itor.next();//当前key值
            if(TextUtils.isEmpty(params)){
                encode(key, paramsMap.get(key));
            }else{
                params.append('&');
                encode(key, paramsMap.get(key));
            }
        }
        return params.toString();
    }

    public String toString() {
        return getParams();
    }

}
