package com.task.data.remote.service

import com.task.data.dto.recipes.RecipesItem
import retrofit2.Response
import retrofit2.http.GET

/**
 * 网络请求接口
 */
interface RecipesService {

    /**
     * 获取菜谱
     * @return 返回菜谱
     */
    @GET("recipes.json")
    suspend fun fetchRecipes(): Response<List<RecipesItem>>
}
