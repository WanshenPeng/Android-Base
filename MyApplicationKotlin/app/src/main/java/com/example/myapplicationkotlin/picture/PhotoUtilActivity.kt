package com.example.myapplicationkotlin.picture

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapplicationkotlin.R
import top.zibin.luban.OnCompressListener
import java.io.File


class PhotoUtilActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    private var photoUri: Uri? = null
    private var targetUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_util)

        findViewById<Button>(R.id.btn_camera).setOnClickListener {
            takePicture(TAKE_PICTURE_REQUEST_CODE)
        }
        findViewById<Button>(R.id.btn_take_photo_crop).setOnClickListener {
            takePicture(TAKE_PICTURE_AND_CROP_REQUEST_CODE)
        }
        findViewById<Button>(R.id.btn_gallery).setOnClickListener {
            choosePicture(CHOOSE_PICTURE_REQUEST_CODE)
        }
        findViewById<Button>(R.id.btn_choose_photo_crop).setOnClickListener {
            choosePicture(CHOOSE_PICTURE_AND_CROP_REQUEST_CODE)
        }
        findViewById<Button>(R.id.btn_compress).setOnClickListener {
            choosePicture(CHOOSE_PICTURE_AND_COMPRESS_REQUEST_CODE)
        }
        imageView = findViewById(R.id.img_show)

    }

    fun takePicture(requestCode: Int) {
        photoUri = PhotoUtil.takePicture(this, requestCode)
    }

    fun choosePicture(requestCode: Int) {
        PhotoUtil.choosePicture(this, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            if (requestCode == CROP_BY_TAKE_PICTURE_REQUEST_CODE || requestCode == CROP_BY_CHOOSE_PICTURE_REQUEST_CODE) {
                PhotoUtil.deleteUri(this, targetUri)
            }
            return
        }

        when (requestCode) {
            TAKE_PICTURE_REQUEST_CODE -> {
                loadPicture(photoUri)
            }
            TAKE_PICTURE_AND_CROP_REQUEST_CODE -> {
//                targetUri = PhotoUtil.uriFromFileName(this)
                if (photoUri == null) {
                    return
                }
                targetUri = PhotoUtil.cropImage(
                    this,
                    CROP_BY_TAKE_PICTURE_REQUEST_CODE,
                    photoUri!!
                )
            }
            CROP_BY_TAKE_PICTURE_REQUEST_CODE -> {
//                loadPicture(data?.data)
                // Android5 上不会返回数据，因此不能使用data?.data
                loadPicture(targetUri)
            }

            CHOOSE_PICTURE_REQUEST_CODE -> {
                loadPicture(data?.data)
            }
            CHOOSE_PICTURE_AND_CROP_REQUEST_CODE -> {
//                targetUri = PhotoUtil.uriFromFileName(this)
                if (data?.data == null) {
                    return
                }
                targetUri = PhotoUtil.cropImage(
                    this,
                    CROP_BY_CHOOSE_PICTURE_REQUEST_CODE,
                    data.data!!
                )
            }
            CROP_BY_CHOOSE_PICTURE_REQUEST_CODE -> {
//                loadPicture(data?.data)
                // Android5 上不会返回数据，因此不能使用data?.data
                loadPicture(targetUri)
            }
            CHOOSE_PICTURE_AND_COMPRESS_REQUEST_CODE -> {
                val filePath = FileUtils.getContentUriFilePath(this, data?.data)

                PhotoUtil.imageCompressionWithLuban(this, filePath,
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
        }
    }

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