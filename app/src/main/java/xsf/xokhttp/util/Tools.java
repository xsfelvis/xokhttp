/**
 * Tools.java
 *
 * @author tianli
 * @date 2011-3-8
 * <p>
 * Copyright 2011 netease. All rights reserved.
 */
package xsf.xokhttp.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import xsf.xokhttp.MyApplication;


/**
 * @author xsfelvis
 */
public class Tools {
    public final static String TAG = Tools.class.getName();


    public static int getSreenWidth() {
        WindowManager wm = (WindowManager) MyApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getSreenHeight() {
        WindowManager wm = (WindowManager) MyApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static void printLog(HashMap<String, String> params) {
        if (params == null)
            return;

        Iterator<String> iterator = params.keySet().iterator();
        boolean isFirst = true;
        StringBuffer buffer = new StringBuffer();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String val = params.get(key);
            if (!isFirst)
                buffer.append("&" + key + "=" + val);
            else {
                buffer.append(key + "=" + val);
                isFirst = false;
            }
        }
        AppLog.v("httpRequest", "request parameter is " + buffer.toString());
    }


    public static File getDiskCacheDir() {
        Context context = MyApplication.getApplication();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            return context.getExternalCacheDir();
        } else {
            return context.getCacheDir();
        }
    }

    /**
     * 下载
     *
     * @param inputStream
     * @param output
     * @throws IOException
     */
    public static void copy(InputStream inputStream, File output) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(output);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }


    /**
     * 隐藏系统输入法
     */
    public static void hideIME(Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) context).getCurrentFocus();
        if (v != null && v.getWindowToken() != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示输入法
     *
     * @param target
     */
    public static void showIME(final View target) {
        target.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(target, 0);
            }
        }, 300);
    }


    /**
     * 根据ID查询联系人的所有电话号码
     *
     * @param context
     * @param id
     * @return
     */
    public static List<String> readAllPhoneNoById(Context context, String id) {
        if (TextUtils.isEmpty(id)) {
            return null;
        }
        List<String> phones = null;
        Cursor phoneCursor = null;
        try {
            phoneCursor = context.getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = " + id, null, null);
            String phoneNumber;
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    phoneNumber = phoneCursor
                            .getString(phoneCursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (phones == null) {
                        phones = new ArrayList<>();
                    }
                    if (!TextUtils.isEmpty(phoneNumber)) {
                        phones.add(phoneNumber);
                    }
                }
            }
        } catch (Exception e) {
            AppLog.d(e.getMessage());
        } finally {
            if (phoneCursor != null && !phoneCursor.isClosed()) {
                phoneCursor.close();
            }
        }

        return phones;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isGpsOpen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    /**
     * 判断是否存在外部存储设备
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    public static float stringToPercentNumber(String number) {
        float result = 0;
        try {
            result = Float.parseFloat(number) * 100;
        } catch (Exception e) {
            if (e.getMessage() != null) {
                AppLog.e(e.getMessage());
            }
        }
        return result;
    }

    public static boolean isEmpty(CharSequence string) {
        return string == null || string.length() == 0;
    }

    public static boolean isEmpty(String string) {
        return string == null || "".equals(string);
    }




}
