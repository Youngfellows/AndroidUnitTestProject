package com.android.post.presentation.posts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.post.domain.model.ApiError
import com.android.post.domain.model.Post
import com.android.post.domain.usecase.GetPostsUseCase
import com.android.post.domain.usecase.base.UseCaseResponse
import kotlinx.coroutines.cancel

/**
 * 可以观察数据变化的ViewModel
 * @property getPostsUseCase 真正执行任务
 */
class PostsViewModel constructor(private val getPostsUseCase: GetPostsUseCase) : ViewModel() {

    /**
     * 可观察的数据-列表数据
     */
    val postsData = MutableLiveData<List<Post>>()

    /**
     * 可观测数据-是否显示加载
     */
    val showProgressbar = MutableLiveData<Boolean>()

    /**
     * 可观测数据-消息
     */
    val messageData = MutableLiveData<String>()

    fun getPosts() {
        //显示加载
        showProgressbar.value = true

        //获取网络响应
        getPostsUseCase.invoke(
            viewModelScope, null,
            object : UseCaseResponse<List<Post>> {
                override fun onSuccess(result: List<Post>) {
                    Log.i(TAG, "result: $result")
                    postsData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            },
        )
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    /**
     * 伴生对象
     */
    companion object {
        /**
         * 静态属性
         */
        private val TAG = PostsViewModel::class.java.name
    }

}