package com.jianyuyouhun.permission.library.listener

import com.jianyuyouhun.permission.library.EZPermission
import com.jianyuyouhun.permission.library.ReqCode
import com.jianyuyouhun.permission.library.listener.OnRequestPermissionResult

/**
 * 请求结果处理回调kt
 * Created by wangyu on 2018/9/14.
 */
abstract class PreHandleResult : OnRequestPermissionResult {

    protected val ignoredManager by lazy { EZPermission.getIgnoredManager() }
    protected var granteds = ArrayList<String>()
    protected var denieds = ArrayList<String>()

    final override fun onResult(reqCode: ReqCode, permission: String, shouldShowRationale: Boolean) {
        when (reqCode) {
            ReqCode.PERMISSION_GRANTED -> {
                //权限申请到以后取消忽略标记，不考虑shouldShowRationale
                ignoredManager.putPermissionRecord(permission, false)
                addToGranted(permission)
            }
            ReqCode.PERMISSION_DENIED -> {
                //权限被拒绝，判断是不是第一次申请，如果是则不考虑shouldShowRationale
                //并且should为false则表示不再询问
                if (ignoredManager.isExistKey(permission)) {
                    ignoredManager.putPermissionRecord(permission, !shouldShowRationale)
                } else {
                    ignoredManager.putPermissionRecord(permission, false)
                }
                addToDenied(permission)
            }
            ReqCode.REQUEST_CANCELED -> {
                clearAll()
                onCanceled()
            }
        }
    }

    private fun clearAll() {
        granteds.clear()
        denieds.clear()
    }

    final override fun isOver() = onPreHandleFinished()

    private fun addToGranted(permission: String) {
        granteds.add(permission)
    }

    private fun addToDenied(permission: String) {
        denieds.add(permission)
    }

    abstract fun onPreHandleFinished()

    abstract fun onCanceled()
}