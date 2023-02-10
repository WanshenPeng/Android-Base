package com.example.myapplicationkotlin.picture

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapplicationkotlin.R
import pub.devrel.easypermissions.EasyPermissions
import top.zibin.luban.OnCompressListener
import java.io.File


class PhotoUtilActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    private var takePictureResultUri: Uri? = null
    private var cropResultUri: Uri? = null

    lateinit var photoUtil: PhotoUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_util)

        findViewById<Button>(R.id.btn_camera).setOnClickListener {
            takePicture()
        }
        findViewById<Button>(R.id.btn_take_photo_crop).setOnClickListener {
            takePictureAndCrop()
        }
        findViewById<Button>(R.id.btn_gallery).setOnClickListener {
            choosePicture()
        }
        findViewById<Button>(R.id.btn_choose_photo_crop).setOnClickListener {
            choosePictureAndCrop()
        }
        findViewById<Button>(R.id.btn_compress).setOnClickListener {
            choosePictureAndCompress()
        }
        imageView = findViewById(R.id.img_show)

        photoUtil = PhotoUtil(activityResultRegistry)
        lifecycle.addObserver(photoUtil)
    }

    fun takePicture() {
        takePictureResultUri = photoUtil.takePicture(this, object : PhotoUtil.DoOnFinishListener {
            override fun doOnFinish(result: ActivityResult) {
                if (result.resultCode != RESULT_OK){
                    photoUtil.deleteUri(this@PhotoUtilActivity, takePictureResultUri)
                    return
                }
                loadPicture(takePictureResultUri)
            }
        })
    }

    fun takePictureAndCrop() {
        takePictureResultUri = photoUtil.takePicture(this, object : PhotoUtil.DoOnFinishListener {
            override fun doOnFinish(result: ActivityResult) {
                if (result.resultCode != RESULT_OK){
                    photoUtil.deleteUri(this@PhotoUtilActivity, takePictureResultUri)
                    return
                }
                if (takePictureResultUri == null) {
                    return
                }
                cropResultUri = photoUtil.cropImage(
                    this@PhotoUtilActivity,
                    takePictureResultUri!!,
                    object : PhotoUtil.DoOnFinishListener {
                        override fun doOnFinish(result: ActivityResult) {
                            if (result.resultCode != RESULT_OK){
                                photoUtil.deleteUri(this@PhotoUtilActivity, cropResultUri)
                                return
                            }
                            loadPicture(cropResultUri)
                        }
                    })
            }
        })
    }

    fun choosePicture() {
        photoUtil.choosePicture(this, object : PhotoUtil.DoOnFinishListener {
            override fun doOnFinish(result: ActivityResult) {
                loadPicture(result.data?.data)
            }
        })
    }

    fun choosePictureAndCrop() {
        photoUtil.choosePicture(this, object : PhotoUtil.DoOnFinishListener {
            override fun doOnFinish(result: ActivityResult) {
                if (result.data?.data == null) {
                    return
                }
                cropResultUri = photoUtil.cropImage(
                    this@PhotoUtilActivity,
                    result.data?.data!!,
                    object : PhotoUtil.DoOnFinishListener {
                        override fun doOnFinish(result: ActivityResult) {
                            if (result.resultCode != RESULT_OK){
                                photoUtil.deleteUri(this@PhotoUtilActivity, cropResultUri)
                                return
                            }
                            loadPicture(cropResultUri)
                        }
                    }
                )
            }
        })
    }

    fun choosePictureAndCompress() {
        photoUtil.choosePicture(this, object : PhotoUtil.DoOnFinishListener {
            override fun doOnFinish(result: ActivityResult) {
                if (result.data?.data == null) {
                    return
                }
                val filePath =
                    FileUtils.getContentUriFilePath(this@PhotoUtilActivity, result.data?.data)
                photoUtil.imageCompressionWithLuban(
                    this@PhotoUtilActivity,
                    filePath,
                    object : OnCompressListener {
                        override fun onStart() {
                            Log.i(TAG, "imageCompressionWithLuban start")
                        }

                        override fun onSuccess(p0: File?) {
                            Log.i(TAG, "imageCompressionWithLuban success")
                            val bitmap = BitmapFactory.decodeFile(p0?.absolutePath)
                            imageView.setImageBitmap(bitmap)
                        }

                        override fun onError(p0: Throwable?) {
                            Log.i(TAG, "imageCompressionWithLuban error: ${p0.toString()}")
                        }
                    })
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this
        );
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode != RESULT_OK) {
//            if (requestCode == CROP_BY_TAKE_PICTURE_REQUEST_CODE || requestCode == CROP_BY_CHOOSE_PICTURE_REQUEST_CODE) {
//                PhotoUtil.deleteUri(this, targetUri)
//            }
//            return
//        }
//
//        when (requestCode) {
//            TAKE_PICTURE_REQUEST_CODE -> {
//                loadPicture(photoUri)
//            }
//            TAKE_PICTURE_AND_CROP_REQUEST_CODE -> {
////                targetUri = PhotoUtil.uriFromFileName(this)
//                if (photoUri == null) {
//                    return
//                }
//                targetUri = PhotoUtil.cropImage(
//                    this,
//                    CROP_BY_TAKE_PICTURE_REQUEST_CODE,
//                    photoUri!!
//                )
//            }
//            CROP_BY_TAKE_PICTURE_REQUEST_CODE -> {
////                loadPicture(data?.data)
//                // Android5 上不会返回数据，因此不能使用data?.data
//                loadPicture(targetUri)
//            }
//
//            CHOOSE_PICTURE_REQUEST_CODE -> {
//                loadPicture(data?.data)
//            }
//            CHOOSE_PICTURE_AND_CROP_REQUEST_CODE -> {
////                targetUri = PhotoUtil.uriFromFileName(this)
//                if (data?.data == null) {
//                    return
//                }
//                targetUri = PhotoUtil.cropImage(
//                    this,
//                    CROP_BY_CHOOSE_PICTURE_REQUEST_CODE,
//                    data.data!!
//                )
//            }
//            CROP_BY_CHOOSE_PICTURE_REQUEST_CODE -> {
////                loadPicture(data?.data)
//                // Android5 上不会返回数据，因此不能使用data?.data
//                loadPicture(targetUri)
//            }
//            CHOOSE_PICTURE_AND_COMPRESS_REQUEST_CODE -> {
//                val filePath = FileUtils.getContentUriFilePath(this, data?.data)
//
//                PhotoUtil.imageCompressionWithLuban(this, filePath,
//                    object : OnCompressListener {
//                        override fun onStart() {
//                            Log.i(TAG, "imageCompressionWithLuban start")
//                        }
//
//                        override fun onSuccess(p0: File?) {
//                            Log.i(TAG, "imageCompressionWithLuban success")
//                            val bitmap = BitmapFactory.decodeFile(p0?.absolutePath)
//                            imageView.setImageBitmap(bitmap)
//                        }
//
//                        override fun onError(p0: Throwable?) {
//                            Log.i(TAG, "imageCompressionWithLuban error: ${p0.toString()}")
//                        }
//                    })
//            }
//        }
//    }

    private fun loadPicture(uri: Uri?) {
        if (uri == null) {
            Log.i(TAG, "loadPicture: uri is null")
            return
        }
        Glide.with(this).asBitmap().load(uri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }

    companion object {
        const val TAKE_PICTURE_REQUEST_CODE = 10001
        const val TAKE_PICTURE_AND_CROP_REQUEST_CODE = 10002
        const val CROP_BY_TAKE_PICTURE_REQUEST_CODE = 10006
        const val CHOOSE_PICTURE_REQUEST_CODE = 10003
        const val CHOOSE_PICTURE_AND_CROP_REQUEST_CODE = 10004
        const val CROP_BY_CHOOSE_PICTURE_REQUEST_CODE = 10007
        const val CHOOSE_PICTURE_AND_COMPRESS_REQUEST_CODE = 10005
        const val TAG = "PhotoUtilActivity"
    }
}