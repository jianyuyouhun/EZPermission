## READ ME ##

　　简单的动态权限处理，使用了Kotlin语言

## 效果 ##

<img src="GIF.gif"/>

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
    	compile 'com.github.jianyuyouhun:ezpermission:2.0.1-release'
	}

### 使用框架 ###

**2.0以后不再需要在Application中初始化框架了。**

**2.0可以一次性申请多个权限**

### 1、使用EZPermission申请权限 ###

#### in java ####

    EZPermissionKt.getEzpermission().requestPermission(context, new OnReqPermissionResult() {
            @Override
            protected void onGranted(ArrayList<String> permissions) {

            }

            @Override
            protected void onDenied(ArrayList<String> permissions) {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE);//申请多个权限

#### in kotlin ####

    ezpermission.requestPermission(context, OnReqPermissionKTResult()
            .onGranted {

            }.onDenied {

            },
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE)

### 忽略处理 ###

如果想在用户选择禁止不在询问以后要求用户去往设置页的话，可以传入
OnReqPermissionAlIgnoredResult或者OnReqPermissionAlIgnoredKTResult进行处理

**in java**

    EZPermissionKt.getEzpermission().requestPermission(context, new OnReqPermissionAlIgnoredResult(activity) {
        @Override
        protected void onGranted(ArrayList<String> permissions) {
     
		}

        @Override
        protected void onDenied(ArrayList<String> permissions) {
     
		}
    }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE);


**in kotlin**

	ezpermission.requestPermission(context,OnReqPermissionAtIgnoredKTResult(activity)
        .onGranted {
        
		}.onDenied {
        
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE)

**OnReqPermissionAlIgnoredResult  、 OnReqPermissionAlIgnoredKTResult可选择传入更多参数，用于配置引导用户跳往设置页面的弹窗文案，具体可查看源码**
