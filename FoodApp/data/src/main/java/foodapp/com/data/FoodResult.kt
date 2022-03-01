package foodapp.com.data

/**
 * 请求结果封装
 * @param T 泛型参数
 */
sealed class FoodResult<out T> {

    /**
     * 请求成功
     * @param T 泛型参数
     * @property data 泛型参数
     */
    data class Success<T>(val data: T) : FoodResult<T>()

    /**
     * 请求失败
     * @property throwable 异常
     */
    data class Error(val throwable: Throwable) : FoodResult<Nothing>()

    /**
     * 加载中
     */
    object Loading : FoodResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$throwable]"
            Loading -> "Loading"
        }
    }
}