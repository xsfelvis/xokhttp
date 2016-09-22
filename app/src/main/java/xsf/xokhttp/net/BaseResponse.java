package xsf.xokhttp.net;

import com.google.gson.annotations.SerializedName;

/**
 * 这里决定了只能解析的类型 必须有object对应的name才可以
 * Author: xsfelvis
 * Time: created at 2016/8/28.
 */
public class BaseResponse<T> {
    @SerializedName(value = "code", alternate = {"retcode", "operationResp", "error"})
    public String code;
    @SerializedName(value = "msg", alternate = {"detailMsg", "retdesc"})
    public String msg;
    public String retcodeEnum;
    public String businessResp;
    public String appRetcodeEnum;
    @SerializedName(value = "result", alternate = {"data"})
    public T result;

    public BaseResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse() {

    }

}
