package com.jianyuyouhun.permission.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * 请求权限页面
 * Created by wangyu on 2018/4/16.
 */

public class RequestPermissionActivity extends AppCompatActivity {
    private static final String KEY_REQUESTER = "key_req";

    static void startActivity(Context context, PRequester requester) {
        context.startActivity(new Intent(context, RequestPermissionActivity.class)
                .putExtra(KEY_REQUESTER, requester));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.TOP | Gravity.LEFT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = 1;
        params.height = 1;
        window.setAttributes(params);
        PRequester requester = (PRequester) getIntent().getSerializableExtra(KEY_REQUESTER);
        if (requester == null) {
            finish();
            return;
        }
        if (!EZPermission.Companion.getInstance().requestPermission(this, requester)) {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EZPermission.Companion.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EZPermission.Companion.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }
}
