package com.jianyuyouhun.permission.library

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity

/**
 * 权限申请页面，此页面全透明
 * Created by wangyu on 2018/9/14.
 */
class ReqPermissionActivity: AppCompatActivity() {

    private val permissions by lazy { EZPermission.getPermissions() }

    private val reqCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), reqCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == reqCode) {
            EZPermission.onRequestPermissionsResult(this, requestCode, grantResults)
        }
    }
}