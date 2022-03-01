package com.android.post.di

import com.android.post.presentation.posts.PostsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * 核心模块
 */
val AppModule = module {

    /**
     * 创建PostsViewModel,依赖于GetPostsUseCase
     */
    viewModel { PostsViewModel(get()) }

    /**
     * 创建GetPostsUseCase,依赖于PostsRepository
     */
    single { createGetPostsUseCase(get()) }

    /**
     * 创建PostsRepository,依赖于ApiService,ApiService通过NetworkModule加载
     */
    single { createPostRepository(get()) }
}