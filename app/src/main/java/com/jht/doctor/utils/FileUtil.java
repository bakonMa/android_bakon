package com.jht.doctor.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtil {

    /**
     * 复制文件到指定目录
     *
     * @param is
     * @param dirPath
     * @param fileName
     */
    public static File inputStreamToFile(InputStream is, String dirPath, String fileName) {
        try {
            if (is != null) {
                File file = new File(createDirByPath(dirPath), fileName);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024 * 100];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.flush();
                fos.close();
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 创建目录
     *
     * @param path
     * @return
     */
    public static File createDirByPath(String path) {
        File file = new File(path);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public static File createDirFile(String path) {
        int pos = path.lastIndexOf("/");
        String dirpath = path.substring(0, pos + 1);
        if (!dirpath.startsWith("/")) {
            dirpath = "/" + dirpath;
        }
        File f = new File(dirpath);
        if (!f.exists()) {
            f.mkdirs();
        }
        return new File(path);
    }

    /**
     * 通过文件名，判断指定目录下是否有改文件
     *
     * @param filename
     */
    public static File isExitByFileName(String path, String filename) {
        File isExitFile = null;
        File dir = new File(path);
        File[] listFiles = dir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.getName().equals(filename)) {
                    isExitFile = file;
                    break;
                }
            }
        }
        return isExitFile;
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.getAbsoluteFile().delete();
        }
    }

    /***
     * 删除指定目录下得所有文件
     * @param dirPath
     */
    public static boolean clearAllFile(String dirPath) {
        File file = new File(dirPath);
        if (file != null && file.exists()) {
            if (file.isFile()) {
                return file.delete();
            } else if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                for (int i = 0; listFiles != null && i < listFiles.length; i++) {
                    if (listFiles[i].isFile()) {
                        if (!listFiles[i].delete()) {
                            LogUtil.d("Delete file failed!" + dirPath);
                            return false;
                        }
                    } else {
                        if (!clearAllFile(listFiles[i].getPath())) {
                            return false;
                        }
                    }
                }
            }
        }
        LogUtil.d("Delete file----file is not exists----" + dirPath);
        return true;
    }


}


