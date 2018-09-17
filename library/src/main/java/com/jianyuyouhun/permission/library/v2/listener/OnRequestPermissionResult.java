package com.jianyuyouhun.permission.library.v2.listener;

import android.support.annotation.NonNull;

import com.jianyuyouhun.permission.library.v2.ReqCode;

/**
 * 权限申请结果回调
 * Created by wangyu on 2018/9/14.
 */

public interface OnRequestPermissionResult {
    /**
     * 回调
     *
     * @param reqCode               状态码
     * @param permission            权限
     * @param shouldShowRationale   是否应该显示用途
     */
    void onResult(@NonNull ReqCode reqCode, @NonNull String permission, boolean shouldShowRationale);

    /**
     * 申请结束，开始后续处理
     */
    void isOver();
}
