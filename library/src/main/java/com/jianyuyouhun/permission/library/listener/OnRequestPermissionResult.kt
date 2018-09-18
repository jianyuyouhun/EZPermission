package com.jianyuyouhun.permission.library.listener

import com.jianyuyouhun.permission.library.ReqCode

/**
 * 权限申请结果回调
 * Created by wangyu on 2018/9/14.
 */

interface OnRequestPermissionResult {
    /**
     * 回调
     *
     * @param reqCode               状态码
     * @param permission            权限
     * @param shouldShowRationale   是否应该显示用途
     */
    fun onResult(reqCode: ReqCode, permission: String, shouldShowRationale: Boolean)

    /**
     * 申请结束，开始后续处理
     */
    fun isOver()
}
