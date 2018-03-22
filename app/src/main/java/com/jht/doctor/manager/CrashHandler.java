package com.jht.doctor.manager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.jht.doctor.BuildConfig;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.api.local.storage.StorageOperator;
import com.jht.doctor.ui.activity.welcome.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.jht.doctor.config.PathConfig.CRASH_DIR_NAME;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
public final class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final DocApplication mApplication;
    //系统默认的UncaughtException处理类
    private final Thread.UncaughtExceptionHandler mDefaultHandler;
    private final ActManager mActManager;
    private final StorageOperator mStorageOperator;
    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private final String TAG = "CrashHandler";

    public CrashHandler(DocApplication application, ActManager actManager, StorageOperator storageOperator) {
        mApplication = application;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        mActManager = actManager;
        mStorageOperator = storageOperator;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        boolean myTag = handleThrowable(ex);

        //如果发生了未能处理，并且系统的DefaultHandler不为空，交给DefaultHandler处理
        if (!myTag && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    private boolean handleThrowable(Throwable ex) {
        if (ex == null) {
            return false;
        }

        String savedFilePath = saveCrashIntoStorage(mApplication, ex);
        //debug下，保存信息到External中（方便查看），退出
        if (BuildConfig.DEBUG) {
            ex.printStackTrace();
            killApp();
        }
        //非debug下，保存信息到Internal中（到时候作为上传之用），重启应用
        else {
            restartApp(mApplication);
        }
        return true;
    }

    private void killApp() {
        mActManager.AppExit();
    }

    private void restartApp(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 将PackageInfo和Build保存到Map
     *
     * @param context
     * @return
     */
    private Map<String, String> collectDeviceInfo(Context context) {
        Map<String, String> deviceInfoMap = new HashMap<>();
        //保存PackageInfo
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                String packageName = packageInfo.packageName == null ? "null" : packageInfo.packageName;
                String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String versionCode = packageInfo.versionCode + "";
                String requestedPermissions = Arrays.toString(packageInfo.requestedPermissions == null ? new String[]{} : packageInfo.requestedPermissions);
                deviceInfoMap.put("packageName", packageName);
                deviceInfoMap.put("versionName", versionName);
                deviceInfoMap.put("versionCode", versionCode);
                deviceInfoMap.put("requestedPermissions", requestedPermissions);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //保存Build
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                deviceInfoMap.put(field.getName(), field.get(null) == null ? "null" : field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deviceInfoMap;
    }

    private StringBuffer mergeInfo(Map<String, String> deviceInfoMap, Throwable ex) {
        //DeviceInfo
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : deviceInfoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        //ThrowableStackTrace
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        return sb;
    }

    private String generalFileName() {
        long timestamp = System.currentTimeMillis();//时间戳
        String time = formatter.format(new Date(timestamp));//年月日时分秒
        return "crash-" + time + "-" + timestamp + ".log";
    }

    /**
     * 返回对错误信息的Internal(!DEBUG时)或External(DEBUG时)的存储路径
     *
     * @param context
     * @param ex
     * @return
     */
    private String saveCrashIntoStorage(Context context, Throwable ex) {
        StringBuffer sb = mergeInfo(collectDeviceInfo(context), ex);
        //Save To Internal/External
        FileOutputStream fos = null;
        File crashFile = null;
        if (!BuildConfig.DEBUG) {
            File internalCrashDir = mStorageOperator.internalCustomDir(CRASH_DIR_NAME);
            crashFile = new File(internalCrashDir, generalFileName());
        } else {
            if (mStorageOperator.externalRootDirState().equals(Environment.MEDIA_MOUNTED)) {
                File externalCrashDir = new File(mStorageOperator.externalPublicDir("Documents").getAbsolutePath() + File.separator + context.getPackageName(), CRASH_DIR_NAME);
                boolean dirExists = true;
                if (!externalCrashDir.exists()) {
                    dirExists = externalCrashDir.mkdirs();
                }
                if (dirExists) {
                    crashFile = new File(externalCrashDir, generalFileName());
                }
            }
        }

        if (crashFile != null) {
            try {
                fos = new FileOutputStream(crashFile);
                fos.write(sb.toString().getBytes());
                fos.close();
                return crashFile.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}