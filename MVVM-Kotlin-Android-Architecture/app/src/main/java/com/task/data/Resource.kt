package com.task.data

/**
 * A generic class that contains data and status about loading this data.
 * 包含有关加载此数据的数据和状态的泛型类。
 * @param T 泛型
 * @property data 泛型数据
 * @property errorCode 错误信息
 */
sealed class Resource<T>(
    val data: T? = null,
    val errorCode: Int? = null
) {
    /**
     * 成功的数据
     * @param T 泛型
     * @constructor
     * TODO
     *
     * @param data 泛型数据
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     *  加载中的数据
     * @param T 泛型
     * @constructor
     * TODO
     *
     * @param data 泛型数据
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /**
     * 异常的数据
     * @param T 泛型
     * @constructor
     * TODO
     *
     * @param errorCode 错误码
     */
    class DataError<T>(errorCode: Int) : Resource<T>(null, errorCode)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is DataError -> "Error[exception=$errorCode]"
            is Loading<T> -> "Loading"
        }
    }
}
