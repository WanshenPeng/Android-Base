package com.example.myapplicationkotlin.picture

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapplicationkotlin.BuildConfig
import com.example.myapplicationkotlin.R
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import top.zibin.luban.CompressionPredicate
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CamaraActivity : AppCompatActivity() {
    private var photoUri: Uri? = null

    lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        findViewById<Button>(R.id.btn_capture).setOnClickListener {
//            if (hasCamera()) {
//                takePicture()
//            } else {
//                Toast.makeText(this, "no camera", Toast.LENGTH_SHORT).show()
//            }
            takePicture()
        }

        findViewById<Button>(R.id.btn_gallery).setOnClickListener {
            choosePicture()
        }

        findViewById<Button>(R.id.btn_file).setOnClickListener {
            pickFile()
        }

        imageView = findViewById(R.id.img_capture)

        findViewById<Button>(R.id.btn_photo_util_activity).setOnClickListener {
            val intent = Intent(this, PhotoUtilActivity::class.java)
            startActivity(intent)
        }
    }

    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    private fun takePicture() {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (hasPermission(permissions)) {
            photoUri = uriFromFileName(this, SNAP_IMAGE_NAME)
            Log.i(TAG, "takePicture photoUri: ${photoUri?.path}")
            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            grantUriPermission(packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(
                    this,
                    REQUEST_CAMERA_PERM,
                    *permissions
                )
                    .setRationale(R.string.permission_tip_photo)
                    .setPositiveButtonText(R.string.common_ok)
                    .setNegativeButtonText(R.string.cancel)
                    .setTheme(R.style.PermissionDialog)
                    .build()
            )
        }
    }

    @AfterPermissionGranted(REQUEST_STORAGE_PERM)
    private fun choosePicture() {
        Log.i(TAG, "choose")
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (hasPermission(permissions)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            this.startActivityForResult(intent, REQUEST_IMAGE_CHOOSE)
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, REQUEST_STORAGE_PERM, *permissions)
                    .setRationale(R.string.permission_tip_photo)
                    .setPositiveButtonText(R.string.common_ok)
                    .setNegativeButtonText(R.string.cancel)
                    .setTheme(R.style.PermissionDialog)
                    .build()
            )
        }
    }

    private fun hasPermission(permissions: Array<String>): Boolean {
        return EasyPermissions.hasPermissions(this, *permissions)
    }

    var targetUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        Log.i(TAG, "onActivityResult")
//        if (resultCode != RESULT_OK) {
//            if (requestCode == REQUEST_IMAGE_CROP) {
//                targetUri?.let { PhotoUtil.deleteUri(this, it) }
//            }
//            return
//        }

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                targetUri = uriFromFileName(this, "asdqwe.jpg")
//                cropImage(photoUri, targetUri, 200, 200)
                Log.i(TAG, "photoUri?.path: ${photoUri?.path}")
                Log.i(TAG, "intent.data: ${intent?.data?.path}")
            }

            REQUEST_IMAGE_CHOOSE -> {
                Log.i(TAG, "intent.data: ${intent?.data}")
                Log.i(TAG, "intent.data.path: ${intent?.data?.path}")
                Log.i(TAG, "intent.data.scheme: ${intent?.data?.scheme}")

                val filePath = intent?.data?.let { getUriPath(it) }
                val mode = 1
                when (mode) {
                    1 -> { // Luban压缩
                        val targetPath =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/Luban/"
                        filePath?.let { it ->
                            imageCompressionWithLuban(intent.data!!, it, object : OnCompressListener {
                                override fun onStart() {
                                    Log.i(TAG, "imageCompressionWithLuban start")
                                }

                                override fun onSuccess(p0: File?) {
                                    Log.i(TAG, "imageCompressionWithLuban success")
                                    Log.i(TAG, "p0.absolutePath: ${p0?.absolutePath}")
                                    Log.i(TAG, "p0.Path: ${p0?.path}")
                                    Log.i(TAG, "p0.name: ${p0?.name}")

                                    p0?.length()?.let { length ->
                                        if (length > 1048576) { //大于2MB
                                            Toast.makeText(
                                                this@CamaraActivity,
                                                "compressed picture is too large!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return
                                        }
                                    }
                                    val bitmap = BitmapFactory.decodeFile(p0?.absolutePath)
                                    imageView.setImageBitmap(bitmap)
                                }

                                override fun onError(p0: Throwable?) {
                                    Log.i(TAG, "imageCompressionWithLuban error: ${p0.toString()}")
                                }
                            }, targetPath)
                        }
                    }
                    2 -> {
                        if (filePath != null) { // Compressor压缩
                            imageCompressionWithCompressor(filePath)
                        }
                    }
                    else -> {}
                }

            }

            REQUEST_IMAGE_CROP -> {
                Log.i(TAG, "intent.data: ${intent?.data}")

                if (intent != null && intent.hasExtra("data")) {
                    val bitmap: Bitmap? = intent.getParcelableExtra("data")
                    imageView.setImageBitmap(bitmap)
                } else {
                    if (intent?.data != null) {
                        Glide.with(this)
                            .asBitmap()
                            .load(intent.data)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(imageView)
                    }
                }
            }

            REQUEST_PICK_FILE -> {
                intent?.data?.let { fileUri ->
                    Log.i(TAG, "uri.toString: ${fileUri.toString()}")
                    Log.i(TAG, "uri.path: ${fileUri.path}")
//                    var filePath: String? = null
//                    if (fileUri.scheme == null) {
//                        filePath = fileUri.path
//                    } else if (ContentResolver.SCHEME_FILE == fileUri.scheme) {
//                        filePath = fileUri.path
//                    } else if (ContentResolver.SCHEME_CONTENT == fileUri.scheme) {
//                        this.contentResolver.query(
//                            fileUri,
//                            arrayOf(MediaStore.Images.ImageColumns.DATA),
//                            null,
//                            null,
//                            null
//                        )?.let { cursor ->
//                            if (cursor.moveToFirst()){
//                                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
//                                Log.i(TAG, "index: $index")
//                                if (index > -1) {
//                                    filePath = cursor.getString(index)
//                                }
//                            }
//                            cursor.close()
//                        }
//                    }
//                    Toast.makeText(this, "filePath: $filePath", Toast.LENGTH_SHORT).show()

                }

            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }

    private fun queryUriDetail(uri: Uri) {
        // 如果需要选取的图片的详细信息（图片大小、路径、所在相册名称、修改时间、MIME、宽高、文件名等）,
        // 则需要通过 content.getContentResolver().query(uri, ...) 查询（直接查询所有字段）
        val cursor = contentResolver.query(uri, null, null, null, null)

        // 一般查询出来的只有一条记录
        if (cursor?.moveToFirst() == true) {
            // 查看查询结果数据的的所有列, 不同系统版本列名数量和类型可能不相同, 参考:
            // [_id, _data, _size, _display_name, mime_type, title, date_added, date_modified,
            // description, picasa_id, isprivate, latitude, longitude, datetaken, orientation,
            // mini_thumb_magic, bucket_id, bucket_display_name, width, height]
            Log.d(TAG, "columnNames: " + Arrays.toString(cursor.columnNames))

            // 获取图片的 大小、文件名、路径
            // val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE))
            // val filename = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME))
            // val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA))

            // 输出所有列对应的值
            for (column in cursor.columnNames) {
                val index = cursor.getColumnIndex(column)
                val valueDesc = when (cursor.getType(index)) {
                    Cursor.FIELD_TYPE_NULL -> "$column: NULL"
                    Cursor.FIELD_TYPE_INTEGER -> "$column: " + cursor.getInt(index)
                    Cursor.FIELD_TYPE_FLOAT -> "$column: " + cursor.getFloat(index)
                    Cursor.FIELD_TYPE_STRING -> "$column: " + cursor.getString(index)
                    Cursor.FIELD_TYPE_BLOB -> "$column: BLOB"
                    else -> "$column: Unknown"
                }
                Log.d(TAG, valueDesc)
            }
        }

        cursor?.close()
    }

    private fun getUriPath(uri: Uri): String? {
        var filePath: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor?.moveToFirst() == true) {
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            filePath = cursor.getString(index)
        }
        return filePath
    }

    private fun cropImage(resourceUri: Uri?, outputUri: Uri?, outputX: Int = 0, outputY: Int = 0) {
        if (resourceUri == null || outputUri == null) {
            Log.i(TAG, "Uri is null!")
            return
        }
        val intent = Intent("com.android.camera.action.CROP")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)

        intent.setDataAndType(resourceUri, "image/*")
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
        intent.putExtra("scale", true)
        intent.putExtra("scaleUpIfNeeded", true)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("return-data", false)
        intent.putExtra("circleCrop", "true")


        startActivityForResult(intent, REQUEST_IMAGE_CROP)
    }


    /**
     * @param resourceFilePath 源文件路径，例：/storage/emulated/0/Pictures/abc.jpg
     * @param onCompressListener 压缩回调接口
     * @param targetFilePath 输出文件路径,
     * 默认输出至/storage/emulated/0/Android/data/{packagename}/cache/luban_disk_/cache/,
     * 例：/storage/emulated/0/Pictures/
     * @param targetFileName 输出文件名，例：abc.jpg
     */
    private fun imageCompressionWithLuban(
        uri: Uri,
        resourceFilePath: String,
        onCompressListener: OnCompressListener,
        targetFilePath: String? = null,
        targetFileName: String = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".jpeg",
    ) {
        if (targetFilePath != null) {
            File(targetFilePath).mkdirs()
        }
        Luban.with(this).load(resourceFilePath).ignoreBy(2048)
            .setTargetDir(targetFilePath)
            .filter(CompressionPredicate {
                return@CompressionPredicate it.isNotEmpty() || it.lowercase(Locale.getDefault())
                    .endsWith(".gif")
            })
            .setCompressListener(onCompressListener)
            .setRenameListener { targetFileName }
            .launch()
    }

    private fun imageCompressionWithCompressor(resourcePath: String) {
        Log.i(TAG, "imageCompressionWithCompressor start")
        val resourceFile = File(resourcePath)
        val time = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        val targetFile =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/Compressor/" + time + ".webp")
        lifecycleScope.launch {
            val compressedImageFile = Compressor.compress(this@CamaraActivity, resourceFile) {
                quality(80)
                format(Bitmap.CompressFormat.WEBP)
                size(2_097_152)
                destination(targetFile)
            }
            Log.i(TAG, "imageCompressionWithCompressor end")
            Log.i(TAG, "file.length: ${compressedImageFile.length()}")
            val bitmap = BitmapFactory.decodeFile(compressedImageFile.absolutePath)
            Log.i(TAG, "compressedImageFile.absolutePath: ${compressedImageFile.absolutePath}")
            imageView.setImageBitmap(bitmap)
        }
    }

    // 打开系统的文件选择器
    fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
//        intent.type = "image/*"
//        intent.type = "audio/*"
//        intent.type = "video/*"
//        intent.type = "video/*;image/*"
        this.startActivityForResult(intent, REQUEST_PICK_FILE)
    }


    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 10001
        private const val REQUEST_IMAGE_CHOOSE = 10002
        private const val REQUEST_IMAGE_CROP = 10003
        private const val REQUEST_CAMERA_PERM = 10004
        private const val REQUEST_STORAGE_PERM = 10005
        private const val REQUEST_PICK_FILE = 10006
        private const val SNAP_IMAGE_NAME = "snap123.jpg"
        private const val TAG = "CamaraActivity"


        private fun uriFromFileName(context: Context, name: String): Uri? {
            var uri: Uri? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, name)//图片名
                    put(MediaStore.Images.Media.DESCRIPTION, name)//描述
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")//类型
                    put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/" + BuildConfig.APPLICATION_ID
                    )//保存路径
                }
                uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
            } else {
                val mediaFile =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/" + BuildConfig.APPLICATION_ID + "/" + name)
                uri = Uri.fromFile(mediaFile)
            }

            return uri
        }
    }
}