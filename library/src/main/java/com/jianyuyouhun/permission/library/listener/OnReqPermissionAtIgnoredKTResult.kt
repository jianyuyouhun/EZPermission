package com.jianyuyouhun.permission.library.listener

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import com.jianyuyouhun.permission.library.EZPermission

/**
 * 考虑不在询问的权限请求,需要当前activity对象
 * Created by wangyu on 2018/9/14.
 */
class OnReqPermissionAtIgnoredKTResult(private val activity: Activity,
                                       private val title: String = "提示",
                                       private val msg: String = "该应用需要您赋予相应的权限",
                                       private val ok: String = "去设置页面开启",
                                       private val cancel: String = "取消")
    : OnReqPermissionKTResult() {

        private val ignoreds = ArrayList<String>()

    override fun onPreHandleFinished() {
        ignoreds.clear()
        if (denieds.size != 0) {//如果有被拒绝的权限组，先判断是不是被忽略了
            denieds.forEach {
                if (ignoredManager.getPermissionRecord(it)) {
                    ignoreds.add(it)
                }
            }
        }
        if (ignoreds.size != 0) {//有被忽略的权限申请，先处理被忽略的列表
            val alertDialog = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(ok, { dialog, _ ->
                        dialog.dismiss()
                        startSetting()
                    })
                    .setNegativeButton(cancel, { dialog, _ ->
                        dialog.dismiss()
                        super.onPreHandleFinished()
                    }).create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } else {//没有被忽略的申请，采用父类逻辑
            super.onPreHandleFinished()
        }
    }

    private fun startSetting() {
        EZPermission.startSettings(activity, {
            val permissions = ArrayList<String>()
            permissions.clear()
            permissions.addAll(granteds)
            permissions.addAll(denieds)
            granteds.clear()
            denieds.clear()
            permissions.forEach {
                val hasPermission = ActivityCompat.checkSelfPermission(activity, it)
                if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                    granteds.add(it)
                } else {
                    denieds.add(it)
                }
            }
            super.onPreHandleFinished()
        })
    }
}