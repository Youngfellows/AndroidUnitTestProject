package com.android.post.domain.usecase

import com.android.post.domain.model.Post
import com.android.post.domain.repository.PostsRepository
import com.android.post.domain.usecase.base.UseCase

/**
 * 获取网络响应
 * @property postsRepository 具体执行任务
 */
class GetPostsUseCase constructor(
    private val postsRepository: PostsRepository
) : UseCase<List<Post>, Any?>() {

    /**
     * 获取网络响应
     * @param params 请求参数
     * @return
     */
    override suspend fun run(params: Any?): List<Post> {
        return postsRepository.getPosts()
    }

}