package com.jianyuyouhun.permission.library

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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

    private val permissionManager: PermissionManager
    private val requestMap = HashMap<PRequester, OnRequestPermissionResultListener>()
    private val requestLambdaMap = HashMap<PRequester, OnRequestResultListener>()

    init {
        hasInit = true
        permissionManager = PermissionManager(app)
    }

    private fun checkRepeat(keys: Set<PRequester>, requestCode: Int): Boolean {
        val repeated = keys.any { it.requestCode == requestCode }
        if (repeated) {
            Log.d(TAG, "重复的请求码:" + requestCode)
        }
        return repeated
    }

    /**
     * 请求权限
     * @param context                               Context
     * @param pRequester                            请求体
     * @param onRequestPermissionResultListener     请求回调
     */
    fun requestPermission(context: Context, pRequester: PRequester, onRequestPermissionResultListener: OnRequestPermissionResultListener) {
        if (checkRepeat(requestMap.keys, pRequester.requestCode)) {//检测重复添加
            return
        }
        requestMap.put(pRequester, onRequestPermissionResultListener)
        RequestPermissionActivity.startActivity(context, pRequester)
    }

    /**
     * 请求权限
     * @param context                   Context
     * @param pRequester                请求体
     * @param onSuccess                 成功的回调
     * @param onFailed                  失败的回调
     */
    fun requestPermission(context: Context, pRequester: PRequester, onSuccess: (permission: String) -> Unit, onFailed: (permission: String) -> Unit) {
        if (checkRepeat(requestMap.keys, pRequester.requestCode)) {//检测重复添加
            return
        }
        requestLambdaMap.put(pRequester, OnRequestResultListener(onSuccess, onFailed))
        RequestPermissionActivity.startActivity(context, pRequester)
    }

    fun requestPermission(activity: Activity, pRequesterSeri: PRequester): Boolean {
        var pRequester = requestMap.keys.firstOrNull { it.requestCode == pRequesterSeri.requestCode }
        var isLambda = true
        if (pRequester == null) {
            pRequester = requestLambdaMap.keys.firstOrNull { it.requestCode == pRequesterSeri.requestCode }
        } else {
            isLambda = false
        }
        if (pRequester == null) {
            return false
        }
        if (!isLambda) {
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
        } else {
            if (ActivityCompat.checkSelfPermission(activity, pRequester.permission) == PackageManager.PERMISSION_GRANTED) {
                val listener = requestLambdaMap.remove(pRequester)
                listener?.onSuccess?.invoke(pRequester.permission)
            } else {
                //没有权限的时候直接尝试获取权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, pRequester.permission)) {
                    permissionManager.putPermissionRecord(pRequester.permission, false)
                }
                ActivityCompat.requestPermissions(activity, arrayOf(pRequester.permission), pRequester.requestCode)
            }
        }
        return true
    }

    /**
     * 处理申请结果
     * @param activity          Activity
     * @param requestCode       请求码
     * @param permissions       权限组，暂时无用
     * @param grantResults      状态
     */
    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //使用回调监听
        var requester: PRequester? = requestMap.keys.firstOrNull { requestCode == it.requestCode }
        if (requester != null) {
            val listener = requestMap.remove(requester)
            if (grantResults.isEmpty()) {
                listener?.onRequestFailed(requester.permission)
                activity.finish()
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listener?.onRequestSuccess(requester.permission)
                activity.finish()
            } else if (permissionManager.getPermissionRecord(requester.permission)) {//如果被禁止不在询问
                showIgnoreDialog(activity, requester,
                        {
                            listener?.onRequestFailed(requester!!.permission)
                            activity.finish()
                        },
                        {
                            requestMap.put(requester!!, listener!!)
                            startSystemSettingActivity(activity, requester!!.requestCode)
                        })
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, requester.permission)) {
                permissionManager.putPermissionRecord(requester.permission, true)
                listener?.onRequestFailed(requester.permission)
                activity.finish()
            } else {
                listener?.onRequestFailed(requester.permission)
                activity.finish()
            }
        } else {//使用lambda表达式
            requester = requestLambdaMap.keys.firstOrNull { requestCode == it.requestCode }
            if (requester != null) {
                val listener = requestLambdaMap.remove(requester)
                if (grantResults.isEmpty()) {
                    listener?.onFailed?.invoke(requester.permission)
                    activity.finish()
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    listener?.onSuccess?.invoke(requester.permission)
                    activity.finish()
                } else if (permissionManager.getPermissionRecord(requester.permission)) {//如果被禁止不在询问
                    showIgnoreDialog(activity, requester,
                            doNegative = {
                                listener?.onFailed?.invoke(requester.permission)
                                activity.finish()
                            },
                            doPositive = {
                                requestLambdaMap.put(requester, listener!!)
                                startSystemSettingActivity(activity, requester.requestCode)
                            })
                } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, requester.permission)) {
                    permissionManager.putPermissionRecord(requester.permission, true)
                    listener?.onFailed?.invoke(requester.permission)
                    activity.finish()
                } else {
                    listener?.onFailed?.invoke(requester.permission)
                    activity.finish()
                }
            }
        }
    }

    /**
     * 跳转设置页面
     * @param activity      activity
     * @param requester     请求体
     * @param doNegative    消极按钮回调
     * @param doPositive    积极按钮回调
     */
    private fun showIgnoreDialog(activity: Activity, requester: PRequester, doNegative: () -> Unit, doPositive: () -> Unit) {
        val builder = AlertDialog.Builder(activity)
                .setTitle(requester.tips)
                .setMessage(requester.message)
                .setNegativeButton(requester.negativeButtonText, { dialog, _ ->
                    dialog.dismiss()
                    doNegative()
                })
        if (requester.positiveButtonText != null) {//如果没有积极按钮文本，那么就关闭跳转设置页面功能
            builder.setPositiveButton(requester.positiveButtonText, { dialog, _ ->
                dialog.dismiss()
                doPositive()
            })
        }
        builder.show()
    }

    /**
     *
     * @param activity      activity
     * @param requestCode   请求码
     * @param resultCode    结果码
     * @param data          intent
     */
    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        //使用回调监听
        var requester: PRequester? = requestMap.keys.firstOrNull { requestCode == it.requestCode }
        if (requester != null) {
            val listener = requestMap.remove(requester)
            if (ActivityCompat.checkSelfPermission(activity, requester.permission) == PackageManager.PERMISSION_GRANTED) {
                listener?.onRequestSuccess(requester.permission)
            } else {
                listener?.onRequestFailed(requester.permission)
            }
            synchronizePermissionsState(activity)
            activity.finish()
        } else {//使用lambda表达式
            requester = requestLambdaMap.keys.firstOrNull { requestCode == it.requestCode }
            if (requester != null) {
                val listener = requestLambdaMap.remove(requester)
                if (ActivityCompat.checkSelfPermission(activity, requester.permission) == PackageManager.PERMISSION_GRANTED) {
                    listener?.onSuccess?.invoke(requester.permission)
                } else {
                    listener?.onFailed?.invoke(requester.permission)
                }
                synchronizePermissionsState(activity)
                activity.finish()
            }
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
}

private class OnRequestResultListener(
        val onSuccess: (permission: String) -> Unit,
        val onFailed: (permission: String) -> Unit)

interface OnRequestPermissionResultListener {
    fun onRequestSuccess(permission: String)
    fun onRequestFailed(permission: String)
}

