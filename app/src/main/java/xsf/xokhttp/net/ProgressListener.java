package xsf.xokhttp.net;

/**
 * Created by hzzhangzhizhi on 2015/11/26.
 */
public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
