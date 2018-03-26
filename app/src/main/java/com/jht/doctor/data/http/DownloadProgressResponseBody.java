package com.jht.doctor.data.http;

import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.ui.bean.DownloadProgressBean;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by zhaoyun on 2018/1/12.
 */
public class DownloadProgressResponseBody extends ResponseBody {

    private final int downloadType;
    private final String downloadIdentifier;
    private final ResponseBody responseBody;

    private BufferedSource bufferedSource = null;

    public DownloadProgressResponseBody(int downloadType , String downloadIdentifier , ResponseBody responseBody){
        this.downloadType = downloadType;
        this.downloadIdentifier = downloadIdentifier;
        this.responseBody = responseBody;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null)
            bufferedSource = Okio.buffer(getforwardSource(responseBody.source()));
        return bufferedSource;
    }

    private Source getforwardSource(Source source){
        return new ForwardingSource(source) {
            long totalBytesRead = 0;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink , byteCount);
                if (bytesRead != -1){
                    totalBytesRead += bytesRead;
                }else {
                    totalBytesRead += 0;
                }

                if (downloadType == 0){
                    DownloadProgressBean downloadProgressBean = new DownloadProgressBean();
                    downloadProgressBean.downloadIdentifier = downloadIdentifier;
                    downloadProgressBean.bytesRead = totalBytesRead;
                    downloadProgressBean.contentLength = responseBody.contentLength();
                    downloadProgressBean.done = bytesRead == -1;
                    EventBusUtil.sendEvent(new Event<DownloadProgressBean>(EventConfig.REFRESH_APK_DOWNLOAD_PROGRESS , downloadProgressBean));
                }

                return bytesRead;
            }
        };
    }

}