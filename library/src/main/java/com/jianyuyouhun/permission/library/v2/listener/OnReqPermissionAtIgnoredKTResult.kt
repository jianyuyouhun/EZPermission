package com.jianyuyouhun.permission.library.v2.listener

import android.app.Activity

/**
 * 考虑不在询问的权限请求,需要当前activity对象
 * Created by wangyu on 2018/9/14.
 */
class OnReqPermissionAtIgnoredKTResult(activity: Activity): OnReqPermissionKTResult() {

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
    }

    override fun onCanceled() {
        cancelCallBack?.invoke()
    }
}