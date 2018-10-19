package com.jianyuyouhun.permission.ezpermission;

import com.swuos.mobile.jmvclibrary.app.BaseModel;
import com.swuos.mobile.jmvclibrary.app.JApp;
import com.swuos.mobile.jmvclibrary.models.HttpModel;

import java.util.List;


/**
 *
 * Created by wangyu on 2017/10/19.
 */

public class App extends JApp {
    @Override
    protected boolean setDebugMode() {
        return BuildConfig.DEBUG;
    }

    @Override
    protected void registerApi(HttpModel httpModel) {

    }

    @Override
    protected void initModels(List<BaseModel> modelList) {

    }
}
