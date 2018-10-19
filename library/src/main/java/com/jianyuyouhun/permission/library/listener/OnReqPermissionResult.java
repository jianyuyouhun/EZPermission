package com.jianyuyouhun.permission.library.listener;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

/**
 * 权限申请回调
 * Created by wangyu on 2018/9/14.
 */

public abstract class OnReqPermissionResult extends PreHandleResult {
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onPreHandleFinished() {
        final ArrayList<String> granteds = getGranteds();
        final ArrayList<String> denieds = getDenieds();
        if (granteds.size() != 0) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onGranted(granteds);
                }
            });
        }
        if (denieds.size() != 0) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onDenied(denieds);
                }
            });
        }
    }

    @Override
    public void onCanceled() {

    }

    protected abstract void onGranted(ArrayList<String> permissions);

    protected abstract void onDenied(ArrayList<String> permissions);

}
