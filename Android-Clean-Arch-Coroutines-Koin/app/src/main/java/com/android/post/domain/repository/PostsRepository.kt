package com.android.post.domain.repository

import com.android.post.domain.model.Post

/**
 * 网络请求接口
 */
interface PostsRepository {

    /**
     * 获取响应结果
     * @return
     */
    suspend fun getPosts(): List<Post>
}