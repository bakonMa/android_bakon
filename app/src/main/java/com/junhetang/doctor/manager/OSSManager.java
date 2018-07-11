package com.junhetang.doctor.manager;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.utils.LogUtil;

import java.io.File;
import java.util.HashMap;

/**
 * OSSManager 阿里云oss图片，文件上传，下载。。。
 * Create at 2018/7/10 上午10:45 by mayakun
 */
public final class OSSManager {

//    oss-upload accesskey
//    AccessKeyID：LTAI4yR653kEJ42Y
//    AccessKeySecret：CnIaqrPVRHsYroZ6sI0YQARUBCCgzv
//    EndPoint: oss-cn-shanghai-internal.aliyuncs.com
//    bucket: jhtpri
//    访问域名：https://osspub-pic.jhtcm.vip/
//    bucket: jhtpub
//    访问域名：https://osspub-pic.jhtcm.vip/
//    测试目录：/test

    private OSS oss;
    private String bucket = "jhtpub";
    private String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
    private String path = "https://osspub-pic.jhtcm.vip/";//地址(后台告诉)

    public static OSSManager getInstance() {
        return OssInstance.instance;
    }

    private static class OssInstance {
        private static final OSSManager instance = new OSSManager();
    }

    public OSSManager() {
        initOSSClient();
    }

    private void initOSSClient() {
//        OssTokenBean ossTokenBean = new OssTokenBean();
//        ossTokenBean.AccessKeyId = "LTAI4yR653kEJ42Y";
//        ossTokenBean.AccessKeySecret = "CnIaqrPVRHsYroZ6sI0YQARUBCCgzv";
//        ossTokenBean.Expiration = "9000";
//        OSSCredentialProvider credentialProvider = new STSGetter(ossTokenBean);
//        try {
//            credentialProvider.getFederationToken();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI4yR653kEJ42Y", "CnIaqrPVRHsYroZ6sI0YQARUBCCgzv");

        //设置网络参数
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(DocApplication.getInstance(), endpoint, credentialProvider, conf);
    }

    /**
     * imageUpPath 图片的上传地址（具体地址的前缀后端给，这个是拼起来的一个路径）
     * localFile   图片的本地地址
     */
    public void uploadImageAsync(String imageUpPath, String localImagePath, OSSUploadCallback callback) {
        if (TextUtils.isEmpty(imageUpPath)) {
            imageUpPath = "test/jht_" + System.currentTimeMillis() + ".jpg";
        }
        if (TextUtils.isEmpty(localImagePath)) {
            LogUtil.w("uploadimg", "本地图片路径空异常");
            return;
        }
        File file = new File(localImagePath);
        if (!file.exists()) {
            LogUtil.w("uploadimg", "FileNotExist");
            LogUtil.d("uploadimg", localImagePath);
            return;
        }
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, imageUpPath, localImagePath);
        put.setCRC64(OSSRequest.CRC64Config.YES);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                int progress = (int) (100 * currentSize / totalSize);
                //回调上传进度
                if (callback != null) {
                    callback.uploadStatus(1, progress);
                    LogUtil.d("progress", progress + "");
                }
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put,
                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, final PutObjectResult result) {
                        HashMap map = new HashMap();
                        map.put("result", request.getObjectKey());//线上文件路径
                        map.put("localImagePath", localImagePath);//本地路径
                        callback.uploadStatus(2, map);
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        String info = "";
                        // 请求异常
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                            info = clientExcepion.toString();
                            LogUtil.d(info);
                            callback.uploadStatus(3, "网络异常，请检查网络");
                        }
                        if (serviceException != null) {
                            // 服务异常
                            Log.e("ErrorCode", serviceException.getErrorCode());
                            Log.e("RequestId", serviceException.getRequestId());
                            Log.e("HostId", serviceException.getHostId());
                            Log.e("RawMessage", serviceException.getRawMessage());
                            info = serviceException.toString();
                            LogUtil.d(info);
                            callback.uploadStatus(3, "服务器异常，请稍后重试");
                        }
                    }
                });
    }

    //成功的回调接口
    public interface OSSUploadCallback {
        //1:上传中 2：上传完成 3：上传失败
        void uploadStatus(int type, Object obj);
    }
}
