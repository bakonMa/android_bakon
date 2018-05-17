package com.junhetang.doctor.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.R;
import com.junhetang.doctor.greendao.gen.DaoMaster;
import com.junhetang.doctor.greendao.gen.DaoSession;
import com.junhetang.doctor.utils.FileUtil;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.utils.ZipUtil;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by mayakun on 2017/11/30
 */

public class GreenDaoManager {

    private static final String TAG = "greenDaoManager";
    //在手机里存放数据库的位置
    private static String dbPath = "/data/data/" + DocApplication.getInstance().getPackageName() + "/databases/";
    private static String dbName = "city.db";
    private static String zipName = "city.zip";
    //dbManager单例(多线程中要被共享的使用volatile关键字修饰)
    private volatile static GreenDaoManager greenDaoManager;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private GreenDaoManager() {
        LogUtil.d(TAG, "dbpath=" + dbPath);
        //copy db
        importDB();
        initDB();
    }

    /**
     * 对外唯一，单例
     *
     * @return
     */
    public static GreenDaoManager getInstance() {
        if (greenDaoManager == null) {
            synchronized (GreenDaoManager.class) {
                if (greenDaoManager == null) {
                    greenDaoManager = new GreenDaoManager();
                }
            }
        }
        return greenDaoManager;
    }

    /**
     * 初始化数据
     */
    private void initDB() {
        mDaoMaster = getDaoMaster();
        mDaoSession = mDaoMaster.newSession();
        //debug的时候log打开
        QueryBuilder.LOG_SQL = BuildConfig.LOG_DEBUG;
        QueryBuilder.LOG_VALUES = BuildConfig.LOG_DEBUG;
    }

    /**
     * 创建数据库
     *
     * @return mDaoMaster
     */
    public DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            //http://blog.csdn.net/qq_32583189/article/details/52128620
            //implementation 'net.zetetic:android-database-sqlcipher:3.5.7@aar'
            //DBOpenHelper.getEncryptedWritableDb("pwd"); 使用加密的db
            DBOpenHelper dbOpenHelper = new DBOpenHelper(DocApplication.getInstance(), dbName, null);
            //使用加密的db
//            mDaoMaster = new DaoMaster(dbOpenHelper.getEncryptedWritableDb(""));
            mDaoMaster = new DaoMaster(dbOpenHelper.getWritableDb());
            mDaoMaster.createAllTables(mDaoMaster.getDatabase(), true);
        }
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoSession = mDaoMaster.newSession();
            }
        }
        return mDaoSession;
    }

    /**
     * 导入db到系统目录
     * 第一次安装的时候才会执行
     */
    public void importDB() {
        File dbFile = new File(dbPath + dbName);
        if (dbFile.exists()) {
            return;
        }
        // 判断是否是第一次安装
        File dbDir = new File(dbPath);
        if (!dbDir.exists()) {
            // 路径不存在，创建
            dbDir.mkdirs();
        }
        //第一次安装，创建数据库存储路径，并拷贝解压数据库到系统目录
        LogUtil.d("创建数据库存储路径！");
        InputStream is = DocApplication.getInstance().getResources().openRawResource(R.raw.city);
        File copyFile = FileUtil.inputStreamToFile(is, dbPath, zipName);
        if (copyFile != null && copyFile.exists()) {
            ZipUtil.unZip(copyFile.getAbsolutePath(), dbPath);
            copyFile.delete();
            LogUtil.d("数据库解压成功！");
        }
    }


    /**
     * copy raw数据库到安装目录
     *
     * @param refresh,是否覆盖
     */
    private void copyDB(boolean refresh) {
        LogUtil.d(TAG, "copy raw数据库到安装目录" + dbPath);
        File f = new File(dbPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        File dbFile = new File(dbPath + dbName);
        //是否覆盖
        if (dbFile != null && refresh) {
            LogUtil.d(TAG, "db覆盖");
            dbFile.delete();
        }
        //不存在
        if (!dbFile.exists()) {
            InputStream is = DocApplication.getInstance().getResources().openRawResource(R.raw.city);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));

            ZipEntry entry;
            try {
                while ((entry = zis.getNextEntry()) != null) {
                    int size;
                    byte[] buffer = new byte[1024 * 2];

                    OutputStream fos = new FileOutputStream(dbPath + entry.getName());
                    BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
                    while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, size);
                    }
                    bos.flush();
                    bos.close();
                }

                zis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 数据库升级处理
     */
    class DBOpenHelper extends DaoMaster.OpenHelper {

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        //数据库升级处理
        //http://blog.csdn.net/qq_32583189/article/details/52128620
        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            if (oldVersion == newVersion) {
                Log.d(TAG, "数据库是最新版本" + oldVersion + "，不需要升级");
                return;
            }
            LogUtil.d(TAG, "数据库从版本" + oldVersion + "升级到版本" + newVersion);
            for (int i = oldVersion; i <= newVersion; i++) {
                switch (i) {
                    case 1:
                        break;
//                    case 2:
//                        DaoMaster.createAllTables(db, true);
//                        break;
//                    case 3: //db版本升级(2—>3)
//                        db.execSQL("insert into city values (3751,1047,'经开区','J',null)");
//                        break;
                    default:
                        break;
                }
            }
        }
    }

}
