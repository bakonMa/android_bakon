package com.junhetang.doctor.data.localdata;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import com.junhetang.doctor.application.DocApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author: ZhaoYun
 * @date: 2017/11/2
 * @project: customer-android-2th
 * @detail:
 */
public final class StorageOperator {

    private final DocApplication mApplication;

    public StorageOperator(DocApplication application){
        mApplication = application;
    }

    /**
     * 返回Android系统的System文件夹，System.getenv("ANDROID_ROOT");找不到的话就默认为 /system
     * @return
     */
    public File systemDir(){
        return Environment.getRootDirectory();
    }

    /**
     * 返回Android系统的Data文件夹，System.getenv("ANDROID_DATA");找不到的话就默认为 /data
     * @return
     */
    public File dataDir(){
        return Environment.getDataDirectory();
    }

    /**
     * 返回Android系统的下载缓存Cache文件夹，System.getenv("DOWNLOAD_CACHE");找不到的话就默认为 /cache
     * @return
     */
    public File cacheDir(){
        return Environment.getDownloadCacheDirectory();
    }

    /**
     * 返回Android中外部存储的根目录，根据不同的ROM可能会不同，返回的Dir不一定是存在的或可用的，所以在使用之前要用externalRootDirState()来先进行确认
     * @return
     */
    public File externalRootDir(){
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 返回Android中外部存储的挂载状态，这里只返回外部存储根目录的挂载状态，即算根目录挂载OK，但其中的一些子目录可能挂载状态是无效的
     * {@link #Environment.MEDIA_UNKNOWN},
     * {@link #Environment.MEDIA_REMOVED},
     * {@link #Environment.MEDIA_UNMOUNTED},
     * {@link #Environment.MEDIA_CHECKING},
     * {@link #Environment.MEDIA_NOFS},
     * {@link #Environment.MEDIA_MOUNTED},
     * {@link #Environment.MEDIA_MOUNTED_READ_ONLY},
     * {@link #Environment.MEDIA_SHARED},
     * {@link #Environment.MEDIA_BAD_REMOVAL},
     * {@link #Environment.MEDIA_UNMOUNTABLE}.
     * @return
     */
    public String externalRootDirState(){
        return Environment.getExternalStorageState();
    }

    /**
     * 返回 手机的外部存储是否是可移动的
     * @return
     */
    public boolean isExternalRootRemovable(){
        return Environment.isExternalStorageRemovable();
    }

    public boolean isExternalRootEmulated(){
        return Environment.isExternalStorageEmulated();
    }

    /**
     * 手机外部存储中指定路径的文件夹或文件是否已挂载
     * @param dirOrFilePath
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public String externalDirOrFileState(File dirOrFilePath){
        return Environment.getExternalStorageState(dirOrFilePath);
    }

    /**
     * 手机外部存储中指定路径的文件夹或文件是否是可移动的
     * @param dirOrFilePath
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean isExternalDirOrFileRemovable(File dirOrFilePath){
        return Environment.isExternalStorageRemovable(dirOrFilePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean isExternalDirOrFileEmulated(File dirOrFilePath){
        return Environment.isExternalStorageEmulated(dirOrFilePath);
    }

    /**
     * 返回对手机内所有App都可见的在外部存储中的一些公共资源目录
     * 注意，返回的路径不一定是存在的（或者说只是给出了名字），要使用这些路径的话，最好使用exists()配合mkdirs()
     * {@link #Environment.DIRECTORY_MUSIC},
     * {@link #Environment.DIRECTORY_PODCASTS},
     * {@link #Environment.DIRECTORY_RINGTONES},
     * {@link #Environment.DIRECTORY_ALARMS},
     * {@link #Environment.DIRECTORY_NOTIFICATIONS},
     * {@link #Environment.DIRECTORY_PICTURES},
     * {@link #Environment.DIRECTORY_MOVIES},
     * {@link #Environment.DIRECTORY_DOWNLOADS},
     * {@link #Environment.DIRECTORY_DCIM},
     * {@link #Environment.DIRECTORY_DOCUMENTS}
     * @param type
     * @return
     */
    public File externalPublicDir(String type){
        return Environment.getExternalStoragePublicDirectory(type);
    }

    /**
     * 返回App在internal中私有数据的根目录文件夹（随App删除），并且该RootDir肯定exists() == true
     * @return
     */
    public File internalRootDir(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return mApplication.getDataDir();
        }else {
            File internalCacheDir = internalCacheDir();
            return internalCacheDir.getParentFile();
        }
    }

    /**
     * 返回App的CacheDirectory（随App删除），并且该CacheDirectory肯定exists() == true
     * @return
     */
    public File internalCacheDir(){
        return mApplication.getCacheDir();
    }

    /**
     * 返回App的FilesDirectory（随App删除），该FilesDirectory的exists()可能为true也可能为false
     * @return
     */
    public File internalFilesDir(){
        return mApplication.getFilesDir();
    }

    /**
     * 返回App的CodeCacheDirectory（随App删除），并且该CodeCacheDirectory肯定exists() == true
     * @return
     */
    public File internalCodeCacheDir(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return mApplication.getCodeCacheDir();
        }else {
            return new File(internalRootDir() , "code_cache");
        }
    }

    /**
     * 返回App内存存储中自定义Name的文件夹，如果exists() == false，则会创建一个。
     * 命名规则为app_dirName
     * @param dirName
     * @return
     */
    public File internalCustomDir(String dirName){
        return mApplication.getDir(dirName , Context.MODE_PRIVATE);
    }

    /**
     * 返回App的FilesDirectory（随App删除）中所存在的文件名称集合（只包含文件SimpleName，带有后缀）
     * @return
     */
    public String[] internalFilesInFilesDir(){
        return mApplication.fileList();
    }

    /**
     * 删除App的FilesDirectory（随App删除）中所存在的文件名为fileName的文件
     * @param fileName
     * @return
     */
    public boolean internalDeleteFileInFilesDir(String fileName){
        return mApplication.deleteFile(fileName);
    }

    /**
     * 打开App的FilesDirectory（随App删除）中文件名为fileName的文件的FileInputStream
     * 若没有该文件夹或文件，或打开失败，都返回null
     * @param fileName
     * @return
     */
    public FileInputStream internalFilesDirFileInput(String fileName){
        File file = new File(internalFilesDir() , fileName);
        if (file.exists()){
            try {
                return mApplication.openFileInput(fileName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 打开App的FilesDirectory（随App删除）中文件名为fileName的文件的FileOutputStream
     * 若没有该文件夹或文件，会进行递归创建（没files文件夹就创建文件夹，没fileName文件就创建文件）
     * @param fileName
     * @param mode
     * @return
     */
    public FileOutputStream internalFilesDirFileOutput(String fileName , int mode){
        try {
            return mApplication.openFileOutput(fileName , mode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回App的SharedPreferencesDirectory（随App删除），该SharedPreferencesDirectory的exists()可能为true也可能为false
     * 会随着SharedPreferences的使用而自动创建
     * @return
     */
    public File sharedPreferencesDir(){
        return new File(internalRootDir() , "shared_prefs");
    }

    /**
     * 返回App内部存储的root目录下shared_prefs文件夹中的spName.xml文件
     * 若该文件夹或文件不存在，则返回null
     * @param spName
     * @return
     */
    public File sharedPreferencesFile(String spName){
        File sharedPreferencesDir = sharedPreferencesDir();
        if(!sharedPreferencesDir.exists()){
            return null;
        }
        File sharedPreferencesFile = new File(sharedPreferencesDir , spName + ".xml");
        if(!sharedPreferencesFile.exists()){
            return null;
        }
        return sharedPreferencesFile;
    }

    /**
     * 根据SPName（前缀）删除App内置存储中的该SharedPreferences文件
     * @param spName
     * @return
     */
    public boolean deleteInternalSharedPreferences(String spName){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return mApplication.deleteSharedPreferences(spName);
        }else {
            File sharedPreferencesFile = sharedPreferencesFile(spName);
            if(sharedPreferencesFile == null){
                return false;
            }
            try{
                return sharedPreferencesFile.delete();
            }catch (Exception e){
                return false;
            }
        }
    }

    /**
     * 从remoteSource的内部存储中移动一个sharedPreferences到本地App的内部存储中
     * @param remoteSource
     * @param remoteSPName
     * @return
     */
    public boolean moveRemoteInternalSPToLocalInternal(Context remoteSource , String remoteSPName){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return mApplication.moveSharedPreferencesFrom(remoteSource , remoteSPName);
        }
        return false;
    }

    /**
     * 返回App的DatabasesDirectory（随App删除），该DatabasesDirectory的exists()可能为true也可能为false
     * 会随着Database的使用而自动创建
     * @return
     */
    public File internalDatabasesDir(){
        return new File(internalRootDir() , "databases");
    }

    /**
     * 返回App内部存储的databases目录下的dbName文件（随着openOrCreateDatabase而产生）
     * 若dbName的Database不存在，则返回null
     * @param dbname 不用加文件后缀
     * @return
     */
    public File internalDatabaseFile(String dbname){
        return mApplication.getDatabasePath(dbname);
    }

    /**
     * 返回App内部存储中databases下的所有database文件名前缀
     * @return
     */
    public String[] internalDatabasesNames(){
        return mApplication.databaseList();
    }

    /**
     * 根据DBName删除App内置存储中的该DB文件
     * @param dbName
     * @return
     */
    public boolean deleteInternalDatabaseFile(String dbName){
        return mApplication.deleteDatabase(dbName);
    }

    /**
     * 从remoteSource的内部存储中移动一个database到本地App的内部存储中
     * @param remoteSource
     * @param remoteDBName
     * @return
     */
    public boolean moveRemoteInternalDBToLocalInternal(Context remoteSource , String remoteDBName){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return mApplication.moveDatabaseFrom(remoteSource , remoteDBName);
        }
        return false;
    }

}
