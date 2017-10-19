## READ ME ##

　　简单的动态权限处理，使用了Kotlin语言

## 使用 ##

### 引入到工程 ###

#### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

#### Step 2. Add the dependency ####

	dependencies {
    	compile 'com.github.jianyuyouhun:ezpermission:1.0'
	}

### 初始化项目 ###

### 1、在Application中初始化本框架 ####

#### in java

	EZPermission.Companion.init(application);

#### in kotlin

	EZPermission.init(application)

### 2、在Activity中配置 ###

　　建议抽象出BaseActivity，需要对`onRequestPermissionsResult`和`onActivityResult`进行重写

#### in java ####

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EZPermission.Companion.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

#### in kotlin ####

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EZPermission.Companion.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }

### 3、使用EZPermission申请权限 ###

#### in java ####

		EZPermission.Companion.getInstance().requestPermission(this, new PRequester(Manifest.permission.CALL_PHONE), new OnRequestPermissionResultListener() {
            @Override
            public void onRequestSuccess(@NonNull String permission) {
                Toast.makeText(MainActivity.this, "请求成功" + permission, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestFailed(@NonNull String permission) {
                Toast.makeText(MainActivity.this, "请求失败" + permission, Toast.LENGTH_SHORT).show();
            }
        });

#### in kotlin ####

       EZPermission.instance.requestPermission(activity, PRequester(Manifest.permission.CALL_PHONE),
                onRequestPermissionResultListener = object : OnRequestPermissionResultListener {
                    override fun onRequestSuccess(permission: String) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onRequestFailed(permission: String) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })

### 4、注意事项 ###

　　请求码的设置在PRequester里面，默认为1，如果有并发请求的话，请设置不同的requestCode。