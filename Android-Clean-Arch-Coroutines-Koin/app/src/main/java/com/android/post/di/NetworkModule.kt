package com.android.post.di

import com.android.post.BuildConfig
import com.android.post.data.repository.PostsRepositoryImp
import com.android.post.data.source.remote.ApiService
import com.android.post.domain.repository.PostsRepository
import com.android.post.domain.usecase.GetPostsUseCase
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/**
 * 网络请求模块
 */
private const val TIME_OUT = 30L

val NetworkModule = module {

    /**
     * ApiService依赖于Retrofit
     */
    single { createService(get()) }

    /**
     * Retrofit依赖于OkHttpClient
     */
    single { createRetrofit(get(), BuildConfig.BASE_URL) }

    /**
     * 创建OkHttpClient
     */
    single { createOkHttpClient() }

    /**
     * 创建MoshiConverterFactory
     */
    single { MoshiConverterFactory.create() }

    /**
     * 创建Moshi
     */
    single { Moshi.Builder().build() }

}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor).build()
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create()).build()
}

fun createService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun createPostRepository(apiService: ApiService): PostsRepository {
    return PostsRepositoryImp(apiService)
}

fun createGetPostsUseCase(postsRepository: PostsRepository): GetPostsUseCase {
    return GetPostsUseCase(postsRepository)
}
