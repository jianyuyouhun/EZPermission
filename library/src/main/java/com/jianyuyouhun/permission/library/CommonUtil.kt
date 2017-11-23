package com.jianyuyouhun.permission.library

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * 启动设置界面
 */
fun startSystemSettingActivity(activity: Activity, settingsRequestCode: Int) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", activity.packageName, null)
    intent.data = uri
    activity.startActivityForResult(intent, settingsRequestCode)
}