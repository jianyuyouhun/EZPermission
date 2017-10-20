package com.jianyuyouhun.permission.library

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log

/**
 * 权限申请
 * Created by wangyu on 2017/10/19.
 */

class EZPermission private constructor(app: Application) {

    companion object {
        lateinit var instance: EZPermission private set
        var hasInit = false
        val TAG = "EZPermission"
        fun init(app: Application) {
            if (hasInit) {
                throw RuntimeException("EZPermission 已初始化了")
            }
            instance = EZPermission(app)
        }
    }

    val permissionManager: PermissionManager
    private val requestMap = HashMap<PRequester, OnRequestPermissionResultListener>()

    init {
        hasInit = true
        permissionManager = PermissionManager(app)
    }

    /**
     * 请求权限
     * @param activity                              activity
     * @param pRequester                            请求体
     * @param onRequestPermissionResultListener     请求回调
     */
    fun requestPermission(activity: Activity, pRequester: PRequester, onRequestPermissionResultListener: OnRequestPermissionResultListener) {
        val repeated = requestMap.keys.any { it.requestCode == pRequester.requestCode }
        if (repeated) {
            Log.d(TAG, "重复的请求码:" + pRequester.requestCode)
            return
        }
        requestMap.put(pRequester, onRequestPermissionResultListener)
        if (ActivityCompat.checkSelfPermission(activity, pRequester.permission) == PackageManager.PERMISSION_GRANTED) {
            val listener = requestMap.remove(pRequester)
            listener?.onRequestSuccess(pRequester.permission)
        } else {
            //没有权限的时候直接尝试获取权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, pRequester.permission)) {
                permissionManager.putPermissionRecord(pRequester.permission, false)
            }
            ActivityCompat.requestPermissions(activity, arrayOf(pRequester.permission), pRequester.requestCode)
        }
    }

    /**
     * 处理申请结果
     * @param activity          Activity
     * @param requestCode       请求码
     * @param permissions       权限组，暂时无用
     * @param grantResults      状态
     */
    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val requester: PRequester? = requestMap.keys.firstOrNull { requestCode == it.requestCode }
        if (requester != null) {
            val listener = requestMap.remove(requester)
            if (grantResults.isEmpty()) {
                listener?.onRequestFailed(requester.permission)
            } else {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    listener?.onRequestSuccess(requester.permission)
                } else {
                    if (permissionManager.getPermissionRecord(requester.permission)) {//如果被禁止不在询问
                        val builder = AlertDialog.Builder(activity)
                                .setTitle(requester.tips)
                                .setMessage(requester.message)
                                .setNegativeButton(requester.negativeButtonText, { dialog, _ ->
                                    dialog.dismiss()
                                    listener?.onRequestFailed(requester.permission)
                                })
                        if (requester.positiveButtonText != null) {//如果没有积极按钮文本，那么就关闭跳转设置页面功能
                            builder.setPositiveButton(requester.positiveButtonText, { dialog, _ ->
                                dialog.dismiss()
                                requestMap.put(requester, listener!!)
                                startSystemSettingActivity(activity, requester.requestCode)
                            })
                        }
                        builder.show()
                    } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, requester.permission)) {
                        permissionManager.putPermissionRecord(requester.permission, true)
                        listener?.onRequestFailed(requester.permission)
                    } else {
                        listener?.onRequestFailed(requester.permission)
                    }
                }
            }
        }
    }

    /**
     *
     * @param activity      activity
     * @param requestCode   请求码
     * @param resultCode    结果码
     * @param data          intent
     */
    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        val requester: PRequester? = requestMap.keys.firstOrNull { requestCode == it.requestCode }
        if (requester != null) {
            val listener = requestMap.remove(requester)
            if (ActivityCompat.checkSelfPermission(activity, requester.permission) == PackageManager.PERMISSION_GRANTED) {
                listener?.onRequestSuccess(requester.permission)
            } else {
                listener?.onRequestFailed(requester.permission)
            }
            synchronizePermissionsState(activity)
        }
    }

    /**
     * 同步其他权限禁用记录
     */
    private fun synchronizePermissionsState(activity: Activity) {
        val allPermissionMap = permissionManager.allPermissionMap ?: //取不到缓存
                return
        for (permission in allPermissionMap.keys) {
            if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                permissionManager.putPermissionRecord(permission, false)//授予的权限都将禁用记录置空
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                permissionManager.putPermissionRecord(permission, true)//用户喜欢在设置页面点来点去的时候就是这里了
            } else {
                permissionManager.putPermissionRecord(permission, false)//怕出错，加上这行保险
            }
        }
    }

    /**
     * 启动设置界面
     */
    private fun startSystemSettingActivity(activity: Activity, settingsRequestCode: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri

        activity.startActivityForResult(intent, settingsRequestCode)
    }
}

interface OnRequestPermissionResultListener {
    fun onRequestSuccess(permission: String)
    fun onRequestFailed(permission: String)
}

