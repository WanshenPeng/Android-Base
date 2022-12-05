package com.example.myapplication.permission

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationkotlin.R
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest


class PermissionActivity : AppCompatActivity() {
    val TAG = "PermissionActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        findViewById<Button>(R.id.button).setOnClickListener {
            methodRequiresTwoPermission()
        }
    }

    // 调用EasyPermissions.onRequestPermissionsResult()方法将申请权限结果的回调交由easypermissions处理
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // @AfterPermissionGranted标识：在成功获取所需权限后会再次回调该方法，此时代码会走到处理业务的相关代码
    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private fun methodRequiresTwoPermission() {
        Log.i(TAG, "methodRequiresTwoPermission")
        val perms =
            arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // 有权限时，执行业务操作
            Log.i(TAG, "has permissions")
        } else {
            // 无权限时，请求权限
            Log.i(TAG, "no permission")
//            EasyPermissions.requestPermissions(
//                this, getString(R.string.camera_and_location_rationale),
//                RC_CAMERA_AND_LOCATION, *perms
//            )
            // 自定义对话框
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, RC_CAMERA_AND_LOCATION, *perms)
                    .setRationale(R.string.camera_and_location_rationale)
                    .setPositiveButtonText(R.string.rationale_ask_ok)
                    .setNegativeButtonText(R.string.rationale_ask_cancel)
                    .build()
            )
        }
    }

    /**
     * 申请权限的时候用户可能拒绝且选择"不再提示"，这种情况就无法再通过运行时申请权限，只能用户到设置页面主动允许该权限。
     * 有些权限被拒绝后程序的某些功能无法正常运行，若权限被用户拒绝且选择选择"不再提示"后，我们可以通过引导用户到设置页面开启
     * 1.首先调用EasyPermissions.somePermissionPermanentlyDenied()确认是否有权限被用于拒绝且选择"不再提示"永久拒绝
     * 2.若有权限被永久拒绝，调用new AppSettingsDialog.Builder(this).build().show()弹出一个对话框引导用户到设置页面开启
     * */
    fun onPermissionsDenied(requestCode: Int, perms: List<String?>) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size)

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                this,
                R.string.returned_from_app_settings_to_activity,
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    companion object {
        const val RC_CAMERA_AND_LOCATION = 1 // requestCode
    }
}