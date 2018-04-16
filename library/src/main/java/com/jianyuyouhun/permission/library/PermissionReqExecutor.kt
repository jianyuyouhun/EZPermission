package com.jianyuyouhun.permission.library

import android.util.Log

/**
 * 执行请求
 * Created by wangyu on 2018/4/16.
 */
class PermissionReqExecutor(private val permissionManager: PermissionManager) {

    private val requestMap = HashMap<PRequester, OnRequestPermissionResultListener>()
    private val requestLambdaMap = HashMap<PRequester, OnRequestResultListener>()

    private class OnRequestResultListener(
            val onSuccess: (permission: String) -> Unit,
            val onFailed: (permission: String) -> Unit)

    private fun checkRepeat(keys: Set<PRequester>, requestCode: Int): Boolean {
        val repeated = keys.any { it.requestCode == requestCode }
        if (repeated) {
            Log.d(EZPermission.TAG, "重复的请求码:" + requestCode)
        }
        return repeated
    }

}