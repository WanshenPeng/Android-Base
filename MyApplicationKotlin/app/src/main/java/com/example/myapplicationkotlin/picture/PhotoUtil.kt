package com.example.myapplicationkotlin.picture

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.example.myapplicationkotlin.BuildConfig
import com.example.myapplicationkotlin.R
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import top.zibin.luban.CompressionPredicate
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Author: Wanshenpeng
 * Date: 2023/1/31 17:49
 * Description: 拍照、选择图片、裁剪、图片压缩功能
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2023/1/31 1.0 首次创建
 */
object PhotoUtil {
    const val REQUEST_STORAGE_PERM = 10001
    const val REQUEST_CAMERA_PERM = 10002

    const val TAG = "PhotoUtil"


    /**
     * 通过[AppCompatActivity.startActivityForResult]启动相册相关的intent，需配合[AppCompatActivity.onActivityResult]使用
     *
     * @param activity
     * @param requestCode [AppCompatActivity.onActivityResult]请求代码
     */
    @AfterPermissionGranted(REQUEST_STORAGE_PERM)
    fun choosePicture(activity: AppCompatActivity, requestCode: Int) {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (hasPermission(activity, permissions)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (intent.resolveActivity(activity.packageManager) == null) {
                Log.i(TAG, "Not find resolveActivity for ACTION_PICK.")
                return
            }
            activity.startActivityForResult(intent, requestCode)
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(activity, REQUEST_STORAGE_PERM, *permissions)
                    .setRationale(R.string.permission_tip_photo)
                    .setPositiveButtonText(R.string.common_ok)
                    .setNegativeButtonText(R.string.cancel)
                    .setTheme(R.style.PermissionDialog)
                    .build()
            )
        }
    }

    /**
     * 通过[AppCompatActivity.startActivityForResult]启动相机相关的intent，需配合[AppCompatActivity.onActivityResult]使用
     *
     * @param activity
     * @param requestCode [AppCompatActivity.onActivityResult]请求代码
     *
     * @return 返回照片Uri，如果无权限或无相机应用则返回null
     */
    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    fun takePicture(activity: AppCompatActivity, requestCode: Int): Uri? {
        val permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //api 28，26,24 需要权限Manifest.permission.WRITE_EXTERNAL_STORAGE
        var photoUri: Uri? = null
        if (hasPermission(activity, permissions)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(activity.packageManager) == null) {
                Log.i(TAG, "Not find resolveActivity for ACTION_IMAGE_CAPTURE.")
                return null
            }
            photoUri = uriFromFileName(activity)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            activity.grantUriPermission(
                activity.packageName,
                photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            activity.startActivityForResult(intent, requestCode)
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(activity, REQUEST_CAMERA_PERM, *permissions)
                    .setRationale(R.string.permission_tip_photo)
                    .setPositiveButtonText(R.string.common_ok)
                    .setNegativeButtonText(R.string.cancel)
                    .setTheme(R.style.PermissionDialog)
                    .build()
            )
        }
        return photoUri
    }

    /**
     * 裁剪图片方法。
     *
     * 通过[AppCompatActivity.startActivityForResult]启动裁剪相关的intent，需配合[AppCompatActivity.onActivityResult]使用
     *
     * 如果直接退出裁剪界面，则会生成一个空文件，请在[AppCompatActivity.onActivityResult]中调用[deleteUri]方法删除空文件。
     * @param activity
     * @param resourceUri 需要裁剪的图片
     * @param outputUri 裁剪后输出的图片
     * @param requestCode [AppCompatActivity.onActivityResult]请求代码
     * @param outputX 裁剪后的X像素大小。默认为0，此时输出裁剪的图片大小
     * @param outputY 裁剪后的Y像素大小。默认为0，此时输出裁剪的图片大小
     *
     * @return 返回裁剪后输出图片的Uri。如果资源文件为null，则返回null
     */
    fun cropImage(
        activity: AppCompatActivity,
        requestCode: Int,
        resourceUri: Uri,
        outputUri: Uri? = uriFromFileName(activity),
        outputX: Int = 0,
        outputY: Int = 0
    ): Uri? {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(resourceUri, "image/*")

        val res = intent.resolveActivity(activity.packageManager)
        if (res == null) {
            Log.i(TAG, "Not find resolveActivity for com.android.camera.action.CROP")
            return null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            activity.grantUriPermission(
                res.packageName,
                outputUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
//        intent.setDataAndType(resourceUri, "image/*")
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
        intent.putExtra("scale", true)
        intent.putExtra("scaleUpIfNeeded", true)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("return-data", false)

        activity.startActivityForResult(intent, requestCode)
        return outputUri
    }

    private fun hasPermission(context: Context, permissions: Array<String>): Boolean {
        return EasyPermissions.hasPermissions(context, *permissions)
    }

    /**
     * 以插入形式在storage/emulated/0/Pictures/[[packagename]]目录下生成文件Uri，若存在重复文件，则新文件的文件名会增加后缀:（n）
     *
     * @param context
     * @param name 文件名，默认以当前时间作为文件名
     *
     * @return 生成的文件Uri
     */
    fun uriFromFileName(
        context: Context,
        name: String = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".jpg"
    ): Uri? {
        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, name)//图片名
                put(MediaStore.Images.Media.DESCRIPTION, name)//描述
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")//类型
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/" + BuildConfig.APPLICATION_ID
                )//保存路径

            }
            uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val mediaFile =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/" + BuildConfig.APPLICATION_ID + "/" + name)
            if (!mediaFile.parentFile?.exists()!!) {
                mediaFile.parentFile?.mkdirs()
            }
            uri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".file_provider",
                mediaFile
            )
        } else {
            val mediaFile =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/" + BuildConfig.APPLICATION_ID + "/" + name)
            if (!mediaFile.parentFile?.exists()!!) {
                mediaFile.parentFile?.mkdirs()
            }
            uri = Uri.fromFile(mediaFile)
        }

        return uri
    }

    /**
     * 根据Uri删除文件，[Uri.getScheme]必须为content或file类型
     *
     * @param context
     * @param uri 文件Uri
     */
    fun deleteUri(context: Context, uri: Uri?) {
        if (uri == null) {
            Log.i(TAG, "deleteUri: uri is null")
            return
        }
        when (uri.scheme) {
            "content" -> {
                context.contentResolver.delete(uri, null, null)
            }
            "file" -> {
                val file: File = uri.toFile()
                if (file.exists() && file.isFile) {
                    file.delete()
                }
            }
            else -> {
                Log.i(TAG, "uri scheme is not content or file")
            }
        }
    }

    /**
     * 图片压缩，采用Luban第三方库
     *
     * @param activity
     * @param resourceFilePath 源文件路径
     * @param onCompressListener 压缩回调接口
     * @param targetFilePath 输出文件目录，默认为storage/emulated/0/Pictures/[[packagename]]/CompressedPictures/
     * @param targetFileName 输出文件名，默认以当前时间作为文件名
     */
    fun imageCompressionWithLuban(
        activity: AppCompatActivity,
        resourceFilePath: String?,
        onCompressListener: OnCompressListener,
        targetFilePath: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/" + BuildConfig.APPLICATION_ID + "/CompressedPictures/",
        targetFileName: String = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".jpg",
    ) {
        if (resourceFilePath == null) {
            Log.i(TAG, "imageCompressionWithLuban: resourceFilePath is null")
            return
        }
        File(targetFilePath).mkdirs()
        Luban.with(activity).load(resourceFilePath).ignoreBy(2048)
            .setTargetDir(targetFilePath)
            .filter(CompressionPredicate {
                return@CompressionPredicate it.isNotEmpty() || it.lowercase(Locale.getDefault())
                    .endsWith(".gif")
            })
            .setCompressListener(onCompressListener)
            .setRenameListener { targetFileName }
            .launch()
    }

    /**
     * 根据Uri获取文件路径
     *
     * @param activity
     * @param uri 文件Uri
     *
     * @return 文件路径
     */
    fun getUriPath(activity: AppCompatActivity, uri: Uri?): String? {
        if (uri == null) {
            Log.i(TAG, "getUriPath: uri is null")
            return null
        }
        var filePath: String? = null
        val cursor = activity.contentResolver.query(uri, null, null, null, null)
        if (cursor?.moveToFirst() == true) {
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            filePath = cursor.getString(index)
        }

        cursor?.close()
        return filePath
    }

}