package com.android.post.domain.usecase.base

import com.android.post.domain.model.ApiError

/**
 * 响应结果
 * @param Type 泛型参数
 */
interface UseCaseResponse<Type> {

    /**
     * 成功返回
     * @param result 泛型结果
     */
    fun onSuccess(result: Type)

    /**
     * 失败返回
     * @param apiError 异常信息
     */
    fun onError(apiError: ApiError?)
}

