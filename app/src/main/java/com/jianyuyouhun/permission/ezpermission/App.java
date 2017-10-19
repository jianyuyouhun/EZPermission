package com.jianyuyouhun.permission.ezpermission;

import android.app.Application;

import com.jianyuyouhun.permission.library.EZPermission;

/**
 *
 * Created by wangyu on 2017/10/19.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EZPermission.Companion.init(this);
    }
}
