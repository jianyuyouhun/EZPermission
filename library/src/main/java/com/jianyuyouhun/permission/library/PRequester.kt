package com.jianyuyouhun.permission.library

import java.io.Serializable

/**
 * 权限请求体
 * Created by wangyu on 2017/10/19.
 */

class PRequester: Serializable {
    var tips: String = "提示"
        private set
    var message: String = "此应用需要获取对应的权限"
        private set
    var negativeButtonText: String = "取消"
        private set
    var positiveButtonText: String? = "去设置页面开启"
        private set
    var permission: String = ""
        private set
    var requestCode: Int = 1
        private set

    constructor(permission: String) {
        this.permission = permission
    }
    constructor(permission: String, requestCode: Int) {
        this.permission = permission
        this.requestCode = requestCode
    }
    fun setTips(tips: String): PRequester {
        this.tips = tips
        return this
    }
    fun setMessage(message: String): PRequester {
        this.message = message
        return this
    }
    fun setNegativeButtonText(negativeButtonText: String): PRequester {
        this.negativeButtonText = negativeButtonText
        return this
    }
    fun setPositiveButtonText(positiveButtonText: String?): PRequester {
        this.positiveButtonText = positiveButtonText
        return this
    }
    fun setPermission(permission: String): PRequester {
        this.permission = permission
        return this
    }
    fun setRequestCode(requestCode: Int): PRequester {
        this.requestCode = requestCode
        return this
    }
}