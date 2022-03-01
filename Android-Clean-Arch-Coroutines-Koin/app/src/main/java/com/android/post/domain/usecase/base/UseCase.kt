package com.android.post.domain.usecase.base

import com.android.post.domain.exception.traceErrorException
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

/**
 * 用例
 * @param Type 返回响应结果类型
 * @param Params 请求参数
 */
abstract class UseCase<Type, in Params>() where Type : Any {

    /**
     * 抽象函数,交给子类执行
     * @param params 请求参数
     * @return 响应结果
     */
    abstract suspend fun run(params: Params? = null): Type

    /**
     * 异步执行
     * @param scope 协程上下文
     * @param params 请求参数
     * @param onResult 回调结果
     */
    fun invoke(scope: CoroutineScope, params: Params?, onResult: UseCaseResponse<Type>?) {

        scope.launch {
            try {
                val result = run(params)
                onResult?.onSuccess(result)
            } catch (e: CancellationException) {
                e.printStackTrace()
                onResult?.onError(traceErrorException(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResult?.onError(traceErrorException(e))
            }
        }
    }

}