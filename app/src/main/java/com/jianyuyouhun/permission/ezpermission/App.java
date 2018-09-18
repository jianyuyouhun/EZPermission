package com.jianyuyouhun.permission.ezpermission;

import android.app.Application;


/**
 *
 * Created by wangyu on 2017/10/19.
 */

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
