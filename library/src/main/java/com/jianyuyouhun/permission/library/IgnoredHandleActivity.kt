package com.jianyuyouhun.permission.library

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * 不再询问权限处理页面
 * Created by wangyu on 2018/9/14.
 */
class IgnoredHandleActivity : AppCompatActivity() {

    private val reqCode = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//设置竖屏模式
        startSystemSettingActivity(this, reqCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqCode) {
            ezpermission.onSettingFinish()
            finish()
        }
    }
}