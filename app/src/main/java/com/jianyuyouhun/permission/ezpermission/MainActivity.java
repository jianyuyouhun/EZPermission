package com.jianyuyouhun.permission.ezpermission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jianyuyouhun.permission.library.EZPermissionKt;
import com.jianyuyouhun.permission.library.listener.OnReqPermissionAtIgnoredResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testV2();
    }

    private void testV2() {
        EZPermissionKt.getEzpermission().requestPermission(this, new OnReqPermissionAtIgnoredResult(this) {
            @Override
            protected void onGranted(ArrayList<String> permissions) {
                showToast("申请成功 " + getP(permissions));
            }

            @Override
            protected void onDenied(ArrayList<String> permissions) {
                showToast("申请失败 " + getP(permissions));
                if (permissions.size() != 0) {
                    testV2();
                }
            }
        }, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private String getP(List<String> ps) {
        StringBuilder result = new StringBuilder();
        for (String p : ps) {
            result.append(p);
        }
        return result.toString();
    }
}
