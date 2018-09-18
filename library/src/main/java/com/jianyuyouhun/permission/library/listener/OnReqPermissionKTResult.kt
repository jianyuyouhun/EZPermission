package com.jianyuyouhun.permission.library.listener

/**
 * 权限申请回调
 * Created by wangyu on 2018/9/14.
 */
open class OnReqPermissionKTResult : PreHandleResult() {


    protected var grantedCallBack: ((permissions: ArrayList<String>) -> Unit)? = null
    protected var deniedCallBack: ((permissions: ArrayList<String>) -> Unit)? = null
    protected var cancelCallBack: (() -> Unit)? = null
    override fun onPreHandleFinished() {
        granteds.apply {
            if (size != 0) grantedCallBack?.invoke(this)
        }
        denieds.apply {
            if (size != 0) deniedCallBack?.invoke(this)
        }
    }

    override fun onCanceled() {
        cancelCallBack?.invoke()
    }

    fun onGranted(callback: (permissions: ArrayList<String>) -> Unit): OnReqPermissionKTResult {
        grantedCallBack = callback
        return this
    }

    fun onDenied(callback: (permissions: ArrayList<String>) -> Unit): OnReqPermissionKTResult {
        deniedCallBack = callback
        return this
    }

    fun onCanceled(callback: () -> Unit): OnReqPermissionKTResult {
        cancelCallBack = callback
        return this
    }
}