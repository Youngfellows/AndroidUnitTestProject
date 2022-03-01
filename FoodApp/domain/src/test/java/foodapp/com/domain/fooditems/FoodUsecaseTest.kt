package foodapp.com.domain.fooditems

import foodapp.com.data.FoodResult
import foodapp.com.data.model.FoodItem
import foodapp.com.data.repository.FoodRepository
import foodapp.com.data.repository.FoodRepositoryImpl
import foodapp.com.data.store.local.LocalDataStore
import foodapp.com.data.store.remote.CloudDataStore
import foodapp.com.data.test.mock.MockFactory
import foodapp.com.domain.fooditems.utils.CoroutineTest
import foodapp.com.domain.fooditems.utils.TestDispatcherProviderImpl
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@FlowPreview
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class FoodUsecaseTest : CoroutineTest {

    /**
     * 测试协程调度器
     */
    override lateinit var dispatcher: TestCoroutineDispatcher

    /**
     * 测试协程范围
     */
    override lateinit var testScope: TestCoroutineScope

    /**
     * 本地存储-模拟对象
     */
    @MockK(relaxed = true)
    lateinit var localDataStore: LocalDataStore


    /**
     * 云端存储-模拟对象
     */
    @MockK(relaxed = true)
    lateinit var remoteCloudDataStore: CloudDataStore

    /**
     * 获取数据的仓库
     */
    private lateinit var repository: FoodRepository

    @BeforeAll
    fun setup() {
        repository = FoodRepositoryImpl(
            localDataStore, remoteCloudDataStore,
            TestDispatcherProviderImpl(dispatcher)
        )
    }

    @Test
    fun `Test usecase getFoodItems request calls repository`() = dispatcher.runBlockingTest {
        val usecase = FoodUsecase(repository)
        usecase.getFoodItems(true)
        verify(exactly = 1) { repository.getFoodItems(true) }
    }

    @Test
    fun `Test usecase getFoodItem request calls repository`() {
        every { repository.getFoodItem(any()) } returns flow {
            emit(MockFactory.generateFoodItem(exception = false) as FoodResult.Success<FoodItem>)
        }

        val usecase = FoodUsecase(repository)
        usecase.getFoodItem(1)
        verify(exactly = 1) { repository.getFoodItem(1) }
    }
}