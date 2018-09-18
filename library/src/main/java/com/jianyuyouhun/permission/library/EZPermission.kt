package com.jianyuyouhun.permission.library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.jianyuyouhun.permission.library.listener.OnRequestPermissionResult
import java.lang.ref.WeakReference

/**
 * 权限请求操作类
 * Created by wangyu on 2018/9/14.
 */
private const val cacheName = "app_permissions_cache"
var EZPermission = object : EPAction() {
    private var ignoredManager: IgnoredManager? = null
    private var reqListenerWeak: WeakReference<OnRequestPermissionResult>? = null
    private var permissions = ArrayList<String>()

    /**
     * 获取申请列表
     */
    override fun getPermissions(): ArrayList<String> = permissions

    /**
     * 获取忽略记录管理器
     */
    override fun getIgnoredManager(): IgnoredManager = ignoredManager!!

    /**
     * 获取回调，调用此方法时表示明确不为空对象
     */
    fun getListener(): OnRequestPermissionResult = reqListenerWeak!!.get()!!

    override fun requestPermission(context: Context, listener: OnRequestPermissionResult, vararg permissions: String) {
        if (permissions.isEmpty()) {
            throw IllegalArgumentException("permissions为空，请传入需要申请的权限")
        }
        reqListenerWeak?.get()?.apply {
            //回调不为空表示已经在请求了，此时抛出异常
            throw RuntimeException("正在申请权限，请勿多次调用")
        } ?: saveParams(permissions, listener, {
            //为空时则保存回调，继续向下执行
            ignoredManager?.apply {
                //缓存管理器已初始化，开始申请
                doRequest(context)
            } ?: initAndLoop(context, {
                //初始化缓存管理器，然后开始申请
                doRequest(context)
            })
        })
    }

    private fun doRequest(context: Context) {
        context.startActivity(Intent(context, ReqPermissionActivity::class.java))
    }

    override fun onRequestPermissionsResult(activity: Activity, requestCode: Int, grantResults: IntArray) {
        if (grantResults.isEmpty()) {
            onResult(ReqCode.REQUEST_CANCELED, "", false)
        } else {
            permissions.forEach {
                val rationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
                if (ActivityCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED) {
                    onResult(ReqCode.PERMISSION_GRANTED, it, rationale)
                } else {
                    onResult(ReqCode.PERMISSION_DENIED, it, rationale)
                }
            }
        }
        activity.finish()
        isOver()
    }

    private fun onResult(reqCode: ReqCode, permission: String, shouldShowRationale: Boolean) {
        getListener().onResult(reqCode, permission, shouldShowRationale)
    }

    private fun isOver() {
        getListener().isOver()
        permissions.clear()
        reqListenerWeak?.clear()
    }

    /**
     * 保存参数
     */
    private fun saveParams(pers: Array<out String>, listener: OnRequestPermissionResult, goOn: () -> Unit) {
        permissions.clear()
        permissions.addAll(pers)
        reqListenerWeak = WeakReference(listener)
        goOn()
    }

    /**
     * 初始化管理器
     */
    private fun initAndLoop(context: Context, goOn: () -> Unit) {
        ignoredManager = IgnoredManager(context, cacheName)
        goOn()
    }

    private var settingListenerWeak: WeakReference<() -> Unit>? = null

    override fun startSettings(context: Context, callback: () -> Unit) {
        settingListenerWeak?.get()?.apply {
            throw RuntimeException("正在跳往设置页，请勿多次调用")
        } ?: saveSettingPara(callback, {
            context.startActivity(Intent(context, IgnoredHandleActivity::class.java))
        })
    }

    /**
     * 保存设置页面回调
     */
    private fun saveSettingPara(callback: () -> Unit, goOn: () -> Unit) {
        settingListenerWeak = WeakReference(callback)
        goOn()
    }

    override fun onSettingFinish() {
        settingListenerWeak?.get()?.invoke()
        settingListenerWeak?.clear()
    }
}

abstract class EPAction {
    abstract fun getIgnoredManager(): IgnoredManager
    abstract fun getPermissions(): ArrayList<String>
    abstract fun requestPermission(context: Context, listener: OnRequestPermissionResult, vararg permissions: String)
    abstract fun onRequestPermissionsResult(activity: Activity, requestCode: Int, grantResults: IntArray)
    abstract fun startSettings(context: Context, callback: () -> Unit)
    abstract fun onSettingFinish()
}