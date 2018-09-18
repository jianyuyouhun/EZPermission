package com.jianyuyouhun.permission.library.listener;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.jianyuyouhun.permission.library.EZPermissionKt;
import com.jianyuyouhun.permission.library.IgnoredManager;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/**
 * 考虑不在询问的权限请求,需要当前activity对象
 * Created by wangyu on 2018/9/17.
 */

public abstract class OnReqPermissionAlIgnoredResult extends OnReqPermissionResult {

    private Activity activity;
    private String title, message, ok, cancel;

    private List<String> ignoreds = new ArrayList<>();

    public OnReqPermissionAlIgnoredResult(Activity activity) {
        this(activity, "提示", "该应用需要您赋予相应的权限", "去设置页面开启", "取消");
    }

    public OnReqPermissionAlIgnoredResult(Activity activity, String title, String message, String ok, String cancel) {
        this.activity = activity;
        this.title = title;
        this.message = message;
        this.ok = ok;
        this.cancel = cancel;
    }

    @Override
    public void onPreHandleFinished() {
        ignoreds.clear();
        ArrayList<String> denieds = getDenieds();
        IgnoredManager ignoredManager = getIgnoredManager();
        if (denieds.size() != 0) {//如果有被拒绝的权限组，先判断是不是被忽略了
            for (String it : denieds) {
                if (ignoredManager.getPermissionRecord(it)) {
                    ignoreds.add(it);
                }
            }
        }
        if (ignoreds.size() != 0) {//有被忽略的权限申请，先处理被忽略的列表
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startSetting();
                        }
                    })
                    .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onSuperHandleFinished();
                        }
                    }).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else {//没有被忽略的申请，采用父类逻辑
            onSuperHandleFinished();
        }
    }

    private void startSetting() {
        EZPermissionKt.getEZPermission().startSettings(activity, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                List<String> permissions = new ArrayList<>();
                permissions.clear();
                permissions.addAll(getGranteds());
                permissions.addAll(getDenieds());
                ArrayList<String> granteds = new ArrayList<>();
                ArrayList<String> denieds = new ArrayList<>();
                for (String it : permissions) {
                    int hasPermission = ActivityCompat.checkSelfPermission(activity, it);
                    if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                        granteds.add(it);
                    } else {
                        denieds.add(it);
                    }
                }
                setGranteds(granteds);
                setDenieds(denieds);
                onSuperHandleFinished();
                return null;
            }
        });
    }

    private void onSuperHandleFinished() {
        super.onPreHandleFinished();
    }
}
