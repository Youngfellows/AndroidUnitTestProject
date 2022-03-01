package foodapp.com.foodapp.ui.list.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * 观察数据变化
 * @param T
 * @param block
 */
fun <T> LiveData<T>.observeForTesting(block: (ValuesObserver<T>) -> Unit) {
    val observer = ValuesObserver<T>()
    try {
        observeForever(observer)
        block(observer)
    } finally {
        removeObserver(observer)
    }
}


/**
 * 观察数据变化
 * @param T
 */
class ValuesObserver<T> : Observer<T> {

    val values = mutableListOf<T>()

    override fun onChanged(t: T) {
        values.add(t)
    }
}