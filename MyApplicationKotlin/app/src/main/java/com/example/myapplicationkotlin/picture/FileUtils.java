package com.example.myapplicationkotlin.picture;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.example.myapplicationkotlin.NewApplication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2018/10/19. 10:01
 * Author: base
 * Description:
 * Version:
 */
public class FileUtils {
    public static final String ROOT_DIR = "Android/data/"
            + NewApplication.Companion.getInstance().getPackageName();
    public static final String DOWNLOAD_DIR = "download";
    public static final String CACHE_DIR = "cache";
    public static final String HTTP_DIR = "http";
    public static final String ICON_DIR = "icon";
    public static final String APK_DIR = "apk";
    public static final String IMG_DIR = "img";

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 判断SD卡是否挂载可用
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()) || !Environment.isExternalStorageRemovable();
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 路径名
     * @return
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 获取下载目录
     */
    public static String getDownloadDir() {
        return getPrivateDir(DOWNLOAD_DIR);
    }

    /**
     * 获取APK下载目录
     */
    public static String getDownloadAPKDir() {
        return getPrivateDir(DOWNLOAD_DIR + File.separator + APK_DIR, true);
    }

    /**
     * 获取http cache目录
     */
    public static String getHttpCacheDir() {
        return getPrivateDir(CACHE_DIR + File.separator + HTTP_DIR);
    }

    /**
     * 获取APK下载目录
     */
    public static String getDownloadImgDir() {
        return getPrivateDir(DOWNLOAD_DIR + File.separator + IMG_DIR);
    }

    /**
     * 获取缓存目录
     */
    public static String getCacheDir() {
        return getPrivateDir(CACHE_DIR);
    }

    /**
     * 获取icon目录
     */
    public static String getIconDir() {
        return getPrivateDir(ICON_DIR);
    }

    /**
     * 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录
     */
    public static String getPrivateDir(String name) {
        return getPrivateDir(name, false);
    }

    public static String getPrivateDir(String name, boolean isFirstSDCard) {
        StringBuilder sb = new StringBuilder();
        if (isFirstSDCard && isSDCardAvailable()) {
            sb.append(getExternalStoragePath());
        } else {
            sb.append(getCachePath());
        }
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (createDirs(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取应用公共根目录
     *
     * @param name
     * @return
     */
    public static String getPublicDir(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(getSDCardPath());
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (createDirs(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取外部存储应用私有目录路径
     *
     * @return /storage/emulated/0/Android/data/应用包名/
     */
    public static String getExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(getSDCardPath());
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }

    /**
     * 获取外部存储目录路径
     *
     * @return /storage/emulated/0/
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 可以在外部存储空间中读取和写入应用专属文件
     *
     * @return
     */
    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED;
    }

    /**
     * 您只能读取这些文件
     *
     * @return
     */
    private boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ||
                Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED_READ_ONLY;
    }

    /**
     * 获取应用内部存储路径的cache目录(系统默认创建cache)
     *
     * @return data/data/应用包名/cache/
     */
    public static String getCachePath() {
        File f = NewApplication.Companion.getInstance().getCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + File.separator;
        }
    }

    /**
     * 获取应用外部存储的私有目录的cache目录(如没有将自动创建)
     *
     * @return /storage/emulated/0/Android/data/应用包名/cache/
     */
    public static String getExternalCachePath() {
        File f = NewApplication.Companion.getInstance().getExternalCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + File.separator;
        }
    }

    /**
     * 获取app 缓存cache路径(外部存储/内部存储)
     *
     * @return (外部存储 / 内部存储私有目录)/Android/data/应用包名/cache/
     */
    public static String getAppCachePath(boolean isFirstSDCard) {
        String cachePath;
        if (isFirstSDCard && isSDCardAvailable()) {
            //外部存储可用
            cachePath = getExternalCachePath();
        } else {
            //外部存储不可用
            cachePath = getCachePath();
        }
        return cachePath;
    }

    public static String getAppCachePath(String name, boolean isFirstSDCard) {
        StringBuilder sb = new StringBuilder();
        sb.append(getAppCachePath(isFirstSDCard));
        sb.append(File.separator);
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (createDirs(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取应用外部存储的私有目录files下的某个目录（如没有将自动创建）
     *
     * @param dirName 文件夹名称
     * @return /storage/emulated/0/Android/data/应用包名/files/dirName
     */
    public static String getExternalFilesPath(String dirName) {
        File f = NewApplication.Companion.getInstance().getExternalFilesDir(dirName);
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + File.separator;
        }
    }

    /**
     * 获取应用内部存储路径的files目录(可以手动操作创建，除此之外所有文件夹都不能手动创建)
     *
     * @return data/data/应用包名/files/
     */
    public static String getFilesPath() {
        File f = NewApplication.Companion.getInstance().getFilesDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + File.separator;
        }
    }

    /**
     * 创建并获取公共目录下的文件路径
     *
     * @param fileName     指文件名，不包含路径
     * @param relativePath 包含某个媒体下的子路径
     * @return
     */
    private static Uri insertFileIntoMediaStore(String fileName, String relativePath) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return null;
        }
        ContentResolver resolver = NewApplication.Companion.getInstance().getContentResolver();
        //设置文件参数到ContentValues中
        ContentValues values = new ContentValues();
        //设置文件名
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        //设置文件描述，这里以文件名为例子
//        values.put(MediaStore.Downloads.BUCKET_DISPLAY_NAME, fileName);
        //设置文件类型
        values.put(MediaStore.Downloads.MIME_TYPE, "application/vnd.android.package-archive");
        //注意RELATIVE_PATH需要targetVersion=29
        //故该方法只可在Android10的手机上执行
        values.put(MediaStore.Downloads.RELATIVE_PATH, relativePath);
        //EXTERNAL_CONTENT_URI代表外部存储器
        Uri external = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        //insertUri表示文件保存的uri路径
        Uri insertUri = resolver.insert(external, values);
        return insertUri;
    }

    /**
     * 保存多媒体文件到公共集合目录
     *
     * @param uri：多媒体数据库的Uri
     * @param context
     * @param mimeType：需要保存文件的mimeType
     * @param displayName：显示的文件名字
     * @param description：文件描述信息
     * @param saveFileName：需要保存的文件名字
     * @param saveSecondaryDir：保存的二级目录
     * @param savePrimaryDir：保存的一级目录
     * @return 返回插入数据对应的uri
     */
    public static Uri insertMediaFile(Uri uri,
                                      Context context,
                                      String mimeType,
                                      String displayName,
                                      String description,
                                      String saveFileName,
                                      String saveSecondaryDir,
                                      String savePrimaryDir) {
        ContentValues values = new ContentValues();
        long date = System.currentTimeMillis();
        values.put(MediaStore.Images.Media.DATE_ADDED, date);
        values.put(MediaStore.Images.Media.DATE_TAKEN, date);
        values.put(MediaStore.Images.Media.DATE_MODIFIED, date);
//        values.put(MediaStore.Images.Media.IS_PRIVATE, 0);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
        values.put(MediaStore.Images.Media.TITLE, displayName);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, String.format("%s%s%s", savePrimaryDir, File.separator, saveSecondaryDir));
        } else {
            String publicDir = getPublicDir(String.format("%s%s%s", savePrimaryDir, File.separator, saveSecondaryDir));
            File tempFile = new File(publicDir, saveFileName);
            if (tempFile != null)
                values.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
        }
        ContentResolver cr = context.getContentResolver();
        return cr.insert(uri, values);
    }

    /**
     * 保存多媒体文件到公共集合目录
     * insertMediaFile(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
     * this,
     * "image/jpeg",
     * "insert_test_img",
     * "test img save use insert",
     * "if_apple_2003193.png",
     * "test",
     * Environment.DIRECTORY_DCIM)
     *
     * @param uri：多媒体数据库的Uri
     * @param context
     * @param mimeType：需要保存文件的mimeType
     * @param displayName：显示的文件名字
     * @param description：文件描述信息
     * @param saveFileName：需要保存的文件名字
     * @param saveSecondaryDir：保存的二级目录
     * @param savePrimaryDir：保存的一级目录
     * @return 返回插入数据对应的uri
     */
    public static Uri insertFile(Uri uri,
                                 Context context,
                                 String mimeType,
                                 String displayName,
                                 String description,
                                 String saveFileName,
                                 String saveSecondaryDir,
                                 String savePrimaryDir) {
        ContentValues values = new ContentValues();
        long date = System.currentTimeMillis();
        values.put(MediaStore.Downloads.DATE_ADDED, date);
        values.put(MediaStore.Downloads.DATE_TAKEN, date);
        values.put(MediaStore.Downloads.DATE_MODIFIED, date);
//        values.put(MediaStore.Downloads.IS_PRIVATE, 0);
        values.put(MediaStore.Downloads.DISPLAY_NAME, displayName);
//        values.put(MediaStore.Downloads.DESCRIPTION, description);
        values.put(MediaStore.Downloads.TITLE, NewApplication.Companion.getInstance().getPackageName());
        values.put(MediaStore.Downloads.MIME_TYPE, mimeType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Downloads.RELATIVE_PATH, savePrimaryDir + File.separator + saveSecondaryDir);
        } else {
            String publicDir = getPublicDir(savePrimaryDir + File.separator + saveSecondaryDir);
            File tempFile = new File(publicDir, saveFileName);
            if (tempFile != null)
                values.put(MediaStore.Downloads.DATA, tempFile.getAbsolutePath());
        }
        ContentResolver cr = context.getContentResolver();
        return cr.insert(uri, values);
    }

    /**
     * 获取APK信息
     *
     * @param apkPath
     * @return
     */
    public static PackageInfo getApkInfo(String apkPath) {
        PackageManager pm = NewApplication.Companion.getInstance().getPackageManager();
        return pm.getPackageArchiveInfo(apkPath, PackageManager.GET_SIGNATURES);
    }

    /**
     * 判断APK是否损坏
     *
     * @param apkPath
     * @return true 已损坏 ，false 未损坏
     */
    public static boolean isApkFileBroken(String apkPath) {
        PackageInfo info = getApkInfo(apkPath);
        if (info == null || info.applicationInfo == null) {
            return true;
        }
        return false;
    }


    /**
     * 批量更改文件后缀
     *
     * @param dir
     * @param oldSuffix
     * @param newSuffix
     */
    public static void reNameSuffix(File dir, String oldSuffix, String newSuffix) {
        if (dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                reNameSuffix(listFiles[i], oldSuffix, newSuffix);
            }
        } else {
            dir.renameTo(new File(dir.getPath().replace(oldSuffix, newSuffix)));
        }
    }

    /**
     * 创建文件夹
     */
    public static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 创建文件夹
     */
    public static boolean createDirs(File file) {
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void delFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void delFile(Uri filePath) {
        NewApplication.Companion.getInstance().getContentResolver().delete(filePath, null, null);
    }

    /**
     * 删除文件夹
     *
     * @param dirPath
     */
    public static void deleteDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(dirPath); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     */
    public static long getFolderSize(File file) {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size / 1048576;
    }

    /**
     * 获取指定文件大小
     *
     * @return 1
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardAvailable()) {
            StatFs stat = new StatFs(getSDCardPath());
            if (Build.VERSION.SDK_INT >= 18) {
                // 获取空闲的数据块的数量
                long availableBlocks = stat.getAvailableBlocksLong() - 4;
                // 获取单个数据块的大小（byte）
                long freeBlocks = stat.getAvailableBlocksLong();
                return freeBlocks * availableBlocks;
            }
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocksLong() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocksLong();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocksLong() - 4;
        return stat.getBlockSizeLong() * availableBlocks;
    }

    /**
     * 判断文件是否存在，如存在则名称自动更改(名称后面+（0..n）)
     *
     * @param fileName 文件名称（包括后缀）
     * @param path     文件路劲
     * @return
     */
    public static File checkDownFile(String fileName, String path) {
        File file = new File(path, fileName);
        //之前下载过,需要重新来一个文件
        int i = 1;
        while (file.exists()) {
            int dotIndex = fileName.lastIndexOf(".");
            String fileNameOther;
            if (dotIndex == -1) {
                fileNameOther = fileName + "(" + i + ")";
            } else {
                int increaseNumIndex = fileName.lastIndexOf("(");
                if (increaseNumIndex != -1) {
                    fileNameOther = fileName.substring(0, increaseNumIndex)
                            + "(" + i + ")" + fileName.substring(dotIndex);
                } else
                    fileNameOther = fileName.substring(0, dotIndex)
                            + "(" + i + ")" + fileName.substring(dotIndex);
            }
            file = new File(path, fileNameOther);
            fileName = file.getName();
            i++;
        }
        return file;
    }



    /**
     * 复制文件，可以选择是否删除源文件
     */
    public static boolean copyFile(File srcFile, File destFile,
                                   boolean deleteSrc) {
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = in.read(buffer)) > 0) {
                out.write(buffer, 0, i);
                out.flush();
            }
            if (deleteSrc) {
                srcFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 从一个数量流里读取数据,返回以byte数组形式的数据。 </br></br>
     * 需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，
     * 但如果是用于网络操作，就经常会遇到一些麻烦(available()方法的问题)。所以如果是网络流不应该使用这个方法。
     *
     * @param in 要读取的输入流
     * @return
     */
    public static byte[] readInputStream(InputStream in) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            byte[] b = new byte[in.available()];
            int length = 0;
            while ((length = in.read(b)) != -1) {
                os.write(b, 0, length);
            }

            b = os.toByteArray();

            in.close();
            in = null;

            os.close();
            os = null;

            return b;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 把字符串数据写入文件
     *
     * @param content 需要写入的字符串
     * @param path    文件路径名称
     * @param append  是否以添加的模式写入
     * @return 是否写入成功
     */
    public static boolean writeFile(byte[] content, String path, boolean append) {
        boolean res = false;
        File f = new File(path);
        RandomAccessFile raf = null;
        try {
            if (f.exists()) {
                if (!append) {
                    f.delete();
                    f.createNewFile();
                }
            } else {
                f.createNewFile();
            }
            if (f.canWrite()) {
                raf = new RandomAccessFile(f, "rw");
                raf.seek(raf.length());
                raf.write(content);
                res = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }






    /**
     * 通过Uri获取文件路径 兼容Android 10
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getContentUriFilePath(final Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }

        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getFileFromContentUri(context, uri);
        }

        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * 通过uri 读取文件，输出Bitmap
     *
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap readUriToBitmap(Context context, Uri uri) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        FileDescriptor fileDescriptor = null;
        Bitmap tagBitmap = null;
        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null && parcelFileDescriptor.getFileDescriptor() != null) {
                fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                //转换uri为bitmap类型
                tagBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                // 你可以做的~~
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (parcelFileDescriptor != null) {
                    parcelFileDescriptor.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tagBitmap;
    }

    /**
     * 通过uri 读取文件，输出byte[]
     *
     * @param context
     * @param uri
     * @return
     */
    public static byte[] readUriToByte(Context context, Uri uri) {
        InputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] fileByte = null;
        try {
            fileInputStream = context.getContentResolver()
                    .openInputStream(uri);
            if (fileInputStream != null) {
                byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buff = new byte[1024 * 4]; //buff用于存放循环读取的临时数据
                int rc = 0;
                while ((rc = fileInputStream.read(buff, 0, 100)) > 0) {
                    byteArrayOutputStream.write(buff, 0, rc);
                }
                fileByte = byteArrayOutputStream.toByteArray(); //fileByte为转换之后的结果
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return fileByte;
    }


    /**
     * Android 10 以上适配 另一种写法
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFileFromContentUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, filePathColumn, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            try {
                int index =cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(index);
                return filePath;
            } catch (Exception e) {
            } finally {
                cursor.close();
            }
        }
        return "";
    }



    /**
     * *
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static final long ONE_KB = 1024;
    public static final BigDecimal ONE_KB_BI = BigDecimal.valueOf(ONE_KB);
    public static final BigDecimal ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
    public static final BigDecimal ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
    public static final BigDecimal ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
    public static final BigDecimal ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
    public static final BigDecimal ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);

    /**
     * 获取字节数大小
     *
     * @param size example: 2.00KB , 10.00MB
     * @return
     */
    public static String byteCountToDisplaySize(BigDecimal size) {
        String displaySize;
        if (size.divide(ONE_EB_BI).compareTo(BigDecimal.ONE) > 0) {
            displaySize = size.divide(ONE_EB_BI, 2, BigDecimal.ROUND_HALF_UP) + " EB";
        } else if (size.divide(ONE_PB_BI).compareTo(BigDecimal.ONE) > 0) {
            displaySize = size.divide(ONE_PB_BI, 2, BigDecimal.ROUND_HALF_UP) + " PB";
        } else if (size.divide(ONE_TB_BI).compareTo(BigDecimal.ONE) > 0) {
            displaySize = size.divide(ONE_TB_BI, 2, BigDecimal.ROUND_HALF_UP) + " TB";
        } else if (size.divide(ONE_GB_BI).compareTo(BigDecimal.ONE) > 0) {
            displaySize = size.divide(ONE_GB_BI, 2, BigDecimal.ROUND_HALF_UP) + " GB";
        } else if (size.divide(ONE_MB_BI).compareTo(BigDecimal.ONE) > 0) {
            displaySize = size.divide(ONE_MB_BI, 2, BigDecimal.ROUND_HALF_UP) + " MB";
        } else if (size.divide(ONE_KB_BI).compareTo(BigDecimal.ONE) > 0) {
            displaySize = size.divide(ONE_KB_BI, 2, BigDecimal.ROUND_HALF_UP) + " KB";
        } else {
            displaySize = size + " B";
        }
        return displaySize;
    }

    /**
     * 获取指定位置的指定类型的文件
     *
     * @param path 文件夹路径
     * @param type 文件类型（如“*.jpg;*.png;*.gif”）
     * @return
     */

    public static void getFileList(String path, String type, OnFileListCallback onFileListCallback) {


        new AsyncTask<String, String, String>() {

            ArrayList<BPFile> list = new ArrayList<BPFile>();

            @Override

            protected void onPostExecute(String result) {
                onFileListCallback.onSearchFileListInfo(list);
            }

            @Override
            protected String doInBackground(String... params) {
                String path = params[1].substring(params[1]
                        .lastIndexOf(".") + 1);
                File file = new File(params[0]);
                scanSDCard(file, path, list);
                return null;
            }


        }.execute(path, type, "");

    }


    /**
     * 扫描完成后的回调，获取文件列表必须实现
     *
     * @author cola
     **/

    public interface OnFileListCallback {

        /**
         * 返回查询的文件列表
         *
         * @param list 文件列表
         */

        void onSearchFileListInfo(List<BPFile> list);

    }

    /**
     * 获取指定位置的指定类型的文件
     *
     * @param file 文件夹
     * @param ext  文件类型（如“jpg、png、gif”）
     * @return
     */
    public static List<BPFile> scanSDCard(File file, String ext) {
        return scanSDCard(file, ext, new ArrayList<BPFile>());
    }

    private static List<BPFile> scanSDCard(File file, String ext, ArrayList<BPFile> list) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File tmp = files[i];
                    if (tmp.isFile()) {
                        String fileName = tmp.getName();
                        String filePath = tmp.getName();
                        if (fileName.indexOf(".") >= 0) {
                            fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
                            if (ext != null && ext.equalsIgnoreCase(fileName)) {

                                BPFile info = new BPFile();
                                info.name = filePath;
                                info.path = tmp.getAbsolutePath();
                                try {
                                    info.size = byteCountToDisplaySize(BigDecimal.valueOf(getFileSize(tmp)));
                                } catch (Exception e) {
                                    info.size = "error";
                                }
                                info.time = tmp.lastModified();
                                list.add(info);
                            }
                        }
                    } else
                        scanSDCard(tmp, ext, list);
                }
            }
        } else {
            if (file.isFile()) {
                String fileName = file.getName();
                String filePath = file.getName();
                if (fileName.indexOf(".") >= 0) {
                    fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (ext != null && ext.equalsIgnoreCase(fileName)) {
                        BPFile info = new BPFile();
                        info.name = filePath;
                        info.path = file.getAbsolutePath();
                        try {
                            info.size = byteCountToDisplaySize(BigDecimal.valueOf(getFileSize(file)));
                        } catch (Exception e) {
                            info.size = "error";
                        }
                        info.time = file.lastModified();
                        list.add(info);
                    }
                }
            }
        }
        return list;
    }

    public static class BPFile implements Parcelable {
        public String id;
        public String name;
        public long time;
        public String size;
        public String path;
        public boolean isChecked;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeLong(this.time);
            dest.writeString(this.size);
            dest.writeString(this.path);
            dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        }

        public BPFile() {
        }

        protected BPFile(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.time = in.readLong();
            this.size = in.readString();
            this.path = in.readString();
            this.isChecked = in.readByte() != 0;
        }

        public static final Parcelable.Creator<BPFile> CREATOR = new Parcelable.Creator<BPFile>() {
            @Override
            public BPFile createFromParcel(Parcel source) {
                return new BPFile(source);
            }

            @Override
            public BPFile[] newArray(int size) {
                return new BPFile[size];
            }
        };
    }
}
