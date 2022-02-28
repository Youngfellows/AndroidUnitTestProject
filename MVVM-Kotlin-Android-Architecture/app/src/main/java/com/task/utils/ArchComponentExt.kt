package com.task.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * 观察数据变化
 * @param T 泛型数据
 * @param liveData 观察的数据
 * @param action 回调
 */
fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

/**
 * 观察数据变化
 * @param T 泛型数据
 * @param liveData 观察的数据
 * @param action 回调
 */
fun <T> LifecycleOwner.observeEvent(
    liveData: LiveData<SingleEvent<T>>,
    action: (t: SingleEvent<T>) -> Unit
) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}
