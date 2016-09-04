/**
 * AppLog.java
 *
 * @author tianli
 * 
 * @date 2011-3-2
 * 
 * Copyright 2011 netease. All rights reserved. 
 */
package xsf.xokhttp.util;

import android.util.Log;

import xsf.xokhttp.BuildConfig;


public class AppLog {

	//http 请求日志
	public static final String HTTPTAG ="http";
	public static final String TAG = "ui";
	public static final String SQLTAG ="sql" ;
	public static final String TAG_IMAGE_UPLOAD = "image";

	private static String defaultTag = "mini";

	public static void e(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable t) {
		if (BuildConfig.DEBUG) {
			Log.e(tag, msg, t);
		}
	}

	public static void v(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.v(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable t) {
		if (BuildConfig.DEBUG) {
			Log.v(tag, msg, t);
		}
	}

	public static void w(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable t) {
		if (BuildConfig.DEBUG) {
			Log.w(tag, msg, t);
		}
	}

	public static void i(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable t) {
		if (BuildConfig.DEBUG) {
			Log.i(tag, msg, t);
		}
	}

	public static void d(Object obj, String msg) {
		d(obj.getClass().getCanonicalName(), msg);
	}

	public static void d(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable t) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg, t);
		}
	}

	/** 新加几个方便使用的方法，这样使用起来就跟System.out.println一样方便了 */
	public static void e(String msg) {
		e(defaultTag, msg);
	}

	/** 新加几个方便使用的方法，这样使用起来就跟System.out.println一样方便了 */
	public static void d(String msg) {
		d(defaultTag, msg);
	}
	
	public static void i(String msg){
		i(defaultTag, msg);
	}

}
