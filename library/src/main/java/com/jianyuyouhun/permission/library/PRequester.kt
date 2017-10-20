package com.jianyuyouhun.permission.library

/**
 * 权限请求体
 * Created by wangyu on 2017/10/19.
 */

data class PRequester(
        var tips: String = "提示",
        var message: String = "此应用需要获取对应的权限",
        var negativeButtonText: String = "取消",
        var positiveButtonText: String? = "去设置页面开启",
        var permission: String = "",
        var requestCode: Int = 1) {
    constructor(permission: String) : this() {
        this.permission = permission
    }
    constructor(permission: String, requestCode: Int) : this() {
        this.permission = permission
        this.requestCode = requestCode
    }
}