package com.task.data.remote

import com.task.data.Resource
import com.task.data.dto.recipes.Recipes

/**
 * 远程请求接口
 */
internal interface RemoteDataSource {
    /**
     * 获取菜谱
     * @return 返回菜谱
     */
    suspend fun requestRecipes(): Resource<Recipes>
}
