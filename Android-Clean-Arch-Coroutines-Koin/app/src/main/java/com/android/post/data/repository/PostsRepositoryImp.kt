package com.android.post.data.repository

import com.android.post.domain.model.Post
import com.android.post.data.source.remote.ApiService
import com.android.post.domain.repository.PostsRepository

/**
 * 获取网络响应
 * @property apiService 具体的网络接口
 */
class PostsRepositoryImp(private val apiService: ApiService) : PostsRepository {

    override suspend fun getPosts(): List<Post> {
        return apiService.getPosts()
    }
}