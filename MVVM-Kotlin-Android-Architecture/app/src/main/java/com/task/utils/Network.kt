package com.task.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject


/**
 * 网络是否连接工具
 * @property context 上下文
 */
class Network @Inject constructor(val context: Context) : NetworkConnectivity {

    /**
     * 获取网络类型
     * @return
     */
    override fun getNetworkInfo(): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * 网络是否连接
     * @return
     */
    override fun isConnected(): Boolean {
        val info = getNetworkInfo()
        return info != null && info.isConnected
    }
}

interface NetworkConnectivity {
    /**
     * @return 获取网络类型
     */
    fun getNetworkInfo(): NetworkInfo?

    /**
     * @return 网络是否连接
     */
    fun isConnected(): Boolean
}