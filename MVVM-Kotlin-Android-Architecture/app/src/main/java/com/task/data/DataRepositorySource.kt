package com.task.data

import com.task.data.dto.recipes.Recipes
import com.task.data.dto.login.LoginRequest
import com.task.data.dto.login.LoginResponse
import kotlinx.coroutines.flow.Flow


/**
 * 请求数据接口
 */
interface DataRepositorySource {

    /**
     * @return 请求菜谱
     */
    suspend fun requestRecipes(): Flow<Resource<Recipes>>

    /**
     * 登录请求
     * @param loginRequest 登录请求实体
     * @return
     */
    suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>>

    /**
     * 添加到喜欢的收藏
     * @param id
     * @return
     */
    suspend fun addToFavourite(id: String): Flow<Resource<Boolean>>

    /**
     * 移除喜欢的收藏
     * @param id
     * @return
     */
    suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>>

    /**
     * 是否是喜欢的收藏
     * @param id
     * @return
     */
    suspend fun isFavourite(id: String): Flow<Resource<Boolean>>
}
