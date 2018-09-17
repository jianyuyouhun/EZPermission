package com.jianyuyouhun.permission.library.v2

import android.content.Context

/**
 * 忽略记录管理器
 * Created by wangyu on 2018/9/14.
 */
class IgnoredManager(private val context: Context,
                     private val cacheName: String = "app_permissions_ignore_cache") {

    private val sp by lazy { context.getSharedPreferences(cacheName, Context.MODE_PRIVATE) }

    private val spEditor by lazy {
        val result = sp.edit()
        result.apply()
        return@lazy result
    }

    /**
     * SP中写入boolean类型value
     *
     * @param permission    权限
     * @param isIgnore      是否不再提示
     */
    fun putPermissionRecord(permission: String, isIgnore: Boolean) {
        val value = if (isIgnore) 1 else 0
        spEditor.putInt(permission, value).apply()
    }

    /**
     * 是否不再提示
     * @param permission    权限
     * @return    是否不再提示
     */
    fun getPermissionRecord(permission: String): Boolean {
        val value = sp.getInt(permission, -1)
        return value == 1
    }

    /**
     * 是否存在该记录
     */
    fun isExistKey(permission: String): Boolean {
        val value = sp.getInt(permission, -1)
        return value != -1
    }

    /**
     * 获取所有权限禁用记录
     * @return  权限记录map
     */
    @Suppress("UNCHECKED_CAST")
    fun getAllPermissionMap(): Map<String, Boolean> {
        return sp.all as Map<String, Boolean>
    }
}