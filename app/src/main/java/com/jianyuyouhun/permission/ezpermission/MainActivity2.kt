package com.jianyuyouhun.permission.ezpermission

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.jianyuyouhun.permission.library.ezpermission
import com.jianyuyouhun.permission.library.listener.OnReqPermissionAtIgnoredKTResult
import com.jianyuyouhun.permission.library.listener.OnReqPermissionKTResult

/**
 *
 * Created by wangyu on 2017/11/23.
 */
class MainActivity2 : AppCompatActivity() {
    private val TAG = "Permission"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testV2()
    }

    private fun testV2() {
        ezpermission.requestPermission(baseContext,
                OnReqPermissionAtIgnoredKTResult(this)
                        .onGranted {
                            var p = ""
                            it.forEach { p += "$it," }
                            Log.i(TAG, "申请成功$p")
                            showToast("申请成功$p")
                        }
                        .onDenied {
                            var p = ""
                            it.forEach { p += "$it," }
                            Log.i(TAG, "申请失败$p")
                            showToast("申请失败$p")

                        }
                        .onCanceled {
                            Log.i(TAG, "取消申请")
                            showToast("取消申请")
                        },
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE)
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}