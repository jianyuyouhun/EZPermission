package com.jianyuyouhun.permission.ezpermission

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.jianyuyouhun.permission.library.EZPermission
import com.jianyuyouhun.permission.library.PRequester

/**
 *
 * Created by wangyu on 2017/11/23.
 */
class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestCall()
    }

    private fun requestCall() {
        EZPermission.instance.requestPermission(this,
                PRequester(Manifest.permission.CALL_PHONE)
                        .setTips("提示")
                        .setMessage("该应用需要获取你的电话权限")
                        .setNegativeButtonText("取消")
                        .setPositiveButtonText("去开启"),
                onSuccess = { permission ->
                    Toast.makeText(this, "请求成功" + permission, Toast.LENGTH_SHORT).show()
                    requestWrite()
                },
                onFailed = { permission ->
                    Toast.makeText(this, "请求失败" + permission, Toast.LENGTH_SHORT).show()
                    requestWrite()
                })
    }

    private fun requestWrite() {
        EZPermission.instance.requestPermission(
                this,
                PRequester(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setMessage("该应用需要获取你的存储权限，请到设置页面开启")
                        .setNegativeButtonText("我知道了")
                        .setPositiveButtonText(null),
                onSuccess = { permission ->
                    Toast.makeText(this, "请求成功" + permission, Toast.LENGTH_SHORT).show()
                },
                onFailed = { permission ->
                    Toast.makeText(this, "请求失败" + permission, Toast.LENGTH_SHORT).show()
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EZPermission.instance.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EZPermission.instance.onActivityResult(this, requestCode, resultCode, data)
    }
}