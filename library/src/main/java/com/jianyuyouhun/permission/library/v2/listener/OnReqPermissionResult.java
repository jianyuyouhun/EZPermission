package com.jianyuyouhun.permission.library.v2.listener;

import java.util.ArrayList;

/**
 * 权限申请回调
 * Created by wangyu on 2018/9/14.
 */

public abstract class OnReqPermissionResult extends PreHandleResult {

    @Override
    public final void onPreHandleFinished() {
        ArrayList<String> granteds = getGranteds();
        ArrayList<String> denieds = getDenieds();
        if (granteds.size() != 0) {
            onGranted(granteds);
        }
        if (denieds.size() != 0) {
            onDenied(denieds);
        }
    }

    @Override
    public void onCanceled() {

    }

    protected abstract void onGranted(ArrayList<String> permissions);

    protected abstract void onDenied(ArrayList<String> permissions);

}
