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
import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.utils.DateUtil;
import com.junhetang.doctor.utils.FileUtil;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.NetUtil;
import com.junhetang.doctor.utils.ToastUtil;

import java.io.File;
import java.util.HashMap;

/**
 * OSSManager 阿里云oss图片，文件上传，下载。。。
 * Create at 2018/7/10 上午10:45 by mayakun
 */
public final class OSSManager {

//    oss-upload accesskey
//    AccessKeyID：LTAIOIVy4MUt26E7
//    AccessKeySecret：bEePtgCWWyPrekmlOQvORJzU2ljfjh
//    EndPoint: oss-cn-shanghai-internal.aliyuncs.com
//    bucket: jhtpri
//    访问域名：https://osspub-pic.jhtcm.vip/
//    bucket: jhtpub

    private OSS oss;
    private String bucket_pub = "jhtpub";//其他路径
    private String bucket_pri = "jhtpri";//处方私密路径
    private String endpoint = "https://oss-cn-shanghai.aliyuncs.com";

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
        //pc端账户
        //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI4yR653kEJ42Y", "CnIaqrPVRHsYroZ6sI0YQARUBCCgzv");
        //分离出移动端端账户
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIOIVy4MUt26E7", "bEePtgCWWyPrekmlOQvORJzU2ljfjh");

        //设置网络参数
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        // oss为全局变量，endpoint是一个OSS区域地址
        oss = new OSSClient(DocApplication.getInstance(), endpoint, credentialProvider, conf);
    }

    /**
     * imageUpPath 图片的上传地址（具体地址的前缀后端给，这个是相对路径）（使用完整图片路径，接口中拼接）
     * localFile   图片的本地地址
     * type   0:头像 1：认证 2：处方
     * pub:
     * 头像：app/header/yyyymmdd/name.jpg
     * pri:
     * 认证：app/identify/yyyymmdd/name.jpg
     * 拍照开方：app/extraction/yyyymmdd/name.jpg
     */
    public OSSAsyncTask uploadImageAsync(int type, String localImagePath, OSSUploadCallback callback) {
        //上传图片时 网络提示  2G,3G网络 提示，其他不提示
        switch (NetUtil.getNetworkSpeedMode()) {
            case NetUtil.NETWORK_UNKNOW:
            case NetUtil.NETWORK_NONE:
                ToastUtil.showCenterToast("当前网络不可用，请检测网络");
                return null;
            case NetUtil.NETWORK_2G:
                ToastUtil.show("当前网络为2G，网速较慢");
                break;
            case NetUtil.NETWORK_3G:
                ToastUtil.show("当前网络为3G，网速较慢");
                break;
            case NetUtil.NETWORK_WIFI:
            case NetUtil.NETWORK_4G:
                break;
        }

        StringBuffer imageUpPath = new StringBuffer(BuildConfig.DEBUG ? "test/" : "app/");
        switch (type) {
            case 0:
                imageUpPath.append("header/");
                break;
            case 1:
                imageUpPath.append("identify/");
                break;
            case 2:
                imageUpPath.append("extraction/");
                break;
        }
        imageUpPath.append(DateUtil.getNowString(DateUtil.FORMAT_4))
                .append("/jht_").append(System.currentTimeMillis()).append(".jpg");

        if (TextUtils.isEmpty(localImagePath)) {
            LogUtil.d("uploadImageAsync", "本地图片路径空异常");
            ToastUtil.showCenterToast("图片不存在，请重新选择");
            //callback.uploadStatus(3, "图片不存在，请重新选择");
            return null;
        }
        File file = new File(localImagePath);
        if (!file.exists() || file.length() <= 0) {
            LogUtil.d("uploadImageAsync", "图片不存在，请重新选择");
            ToastUtil.showCenterToast("图片不存在，请重新选择");
            //callback.uploadStatus(3, "图片不存在，请重新选择");
            return null;
        }
        //压缩
        String uploadPath = FileUtil.zipImageFile(file, FileUtil.MAX_UPLOAD_SIZE_500K);
        LogUtil.d("uploadImageAsync", uploadPath);

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(type == 0 ? bucket_pub : bucket_pri, imageUpPath.toString(), uploadPath);
        put.setCRC64(OSSRequest.CRC64Config.YES);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                int progress = (int) (100 * currentSize / totalSize);
                //回调上传进度
                if (callback != null) {
                    callback.uploadStatus(1, progress);
                    LogUtil.d("onProgress", progress + "");
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
                        //删除压缩后的临时文件
                        FileUtil.deleteFile(uploadPath);
                        callback.uploadStatus(2, map);
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        // 请求异常
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                            LogUtil.d("onFailure", clientExcepion.toString());
                            ToastUtil.showCenterToast("网络异常，请检查网络");
                            //callback.uploadStatus(3, "网络异常，请检查网络");
                        }
                        if (serviceException != null) {
                            // 服务异常
                            Log.e("ErrorCode", serviceException.getErrorCode());
                            Log.e("RequestId", serviceException.getRequestId());
                            Log.e("HostId", serviceException.getHostId());
                            Log.e("RawMessage", serviceException.getRawMessage());
                            LogUtil.d("onFailure", serviceException.toString());
                            ToastUtil.showCenterToast("服务器异常，请重试");
                            //callback.uploadStatus(3, "服务器异常，请重试");
                        }
                    }
                });
        //立即显示loading
        callback.uploadStatus(1, 0);
        return task;
//        Observable.interval(0, 1, TimeUnit.SECONDS)
//                .take(10) //设置循环次数 10s
//                .map(new Func1<Long, Long>() {
//                    @Override
//                    public Long call(Long aLong) {
//                        return 10 - aLong;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onCompleted() {
//                        task.cancel();
//                        callback.uploadStatus(1, -1);//失败
////                        callback.uploadStatus(3, "网络异常，请检测网络");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        //接受到一条就是会操作一次UI
//                    }
//                });

    }

    //成功的回调接口
    public interface OSSUploadCallback {
        //1:上传中 2：上传完成 3：上传失败
        void uploadStatus(int type, Object obj);
    }
}
