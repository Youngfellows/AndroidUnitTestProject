package foodapp.com.domain.fooditems.utils

import foodapp.com.data.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

/**
 * 测试协程调度器
 * @property dispatcher 调度器
 */
@ExperimentalCoroutinesApi
class TestDispatcherProviderImpl constructor(private val dispatcher: TestCoroutineDispatcher) :
    DispatcherProvider {

    /**
     * 默认线程
     */
    override val default: CoroutineDispatcher
        get() = dispatcher

    /**
     * IO线程
     */
    override val io: CoroutineDispatcher
        get() = dispatcher

    /**
     * 主线程
     */
    override val main: CoroutineDispatcher
        get() = dispatcher
}