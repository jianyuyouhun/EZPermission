package com.jianyuyouhun.permission.ezpermission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jianyuyouhun.permission.library.EZPermission;
import com.jianyuyouhun.permission.library.OnRequestPermissionResultListener;
import com.jianyuyouhun.permission.library.PRequester;
import com.jianyuyouhun.permission.library.v2.EZPermissionKt;
import com.jianyuyouhun.permission.library.v2.listener.OnReqPermissionKTResult;
import com.jianyuyouhun.permission.library.v2.listener.OnReqPermissionResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testV2();
//        requestCall();
    }

    private void testV2() {
        EZPermissionKt.getEZPermission().requestPermission(this, new OnReqPermissionResult() {

            @Override
            protected void onGranted(ArrayList<String> permissions) {

            }

            @Override
            protected void onDenied(ArrayList<String> permissions) {

            }

            @Override
            protected void onShowRationale(ArrayList<String> permissions) {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void requestCall() {
        EZPermission.Companion.getInstance().requestPermission(
                this,
                new PRequester(Manifest.permission.CALL_PHONE)
                        .setTips("提示")
                        .setMessage("该应用需要获取你的电话权限")
                        .setNegativeButtonText("取消")
                        .setPositiveButtonText("去开启"),
                new OnRequestPermissionResultListener() {
                    @Override
                    public void onRequestSuccess(@NonNull String permission) {
                        Toast.makeText(MainActivity.this, "请求成功" + permission, Toast.LENGTH_SHORT).show();
                        requestWrite();
                    }

                    @Override
                    public void onRequestFailed(@NonNull String permission) {
                        Toast.makeText(MainActivity.this, "请求失败" + permission, Toast.LENGTH_SHORT).show();
                        requestWrite();
                    }
                });
    }

    private void requestWrite() {
        EZPermission.Companion.getInstance().requestPermission(
                this,
                new PRequester(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setMessage("该应用需要获取你的存储权限，请到设置页面开启")
                        .setNegativeButtonText("我知道了")
                        .setPositiveButtonText(null),
                new OnRequestPermissionResultListener() {
                    @Override
                    public void onRequestSuccess(@NonNull String permission) {
                        Toast.makeText(MainActivity.this, "请求成功" + permission, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRequestFailed(@NonNull String permission) {
                        Toast.makeText(MainActivity.this, "请求失败" + permission, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
