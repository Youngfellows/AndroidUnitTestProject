package foodapp.com.data.repository

import foodapp.com.data.FoodResult
import foodapp.com.data.dispatcher.DispatcherProvider
import foodapp.com.data.model.FoodItem
import foodapp.com.data.store.local.LocalDataStore
import foodapp.com.data.store.remote.CloudDataStore
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * 获取食品数据的仓库
 * @property localDataStore 本地存储
 * @property remoteCloudDataStore 网络请求
 * @property dispatcher 协程分发
 */
class FoodRepositoryImpl @Inject
constructor(
    private val localDataStore: LocalDataStore,
    private val remoteCloudDataStore: CloudDataStore,
    private val dispatcher: DispatcherProvider
) : FoodRepository {

    /**
     * 根据ID获取食品项
     * @param id
     * @return 返回食品项
     */
    override fun getFoodItem(id: Int) = localDataStore
        .getFoodItem(id)
        .map { FoodResult.Success((it)) }
        .flowOn(dispatcher.io)


    /**
     * 获取食品列表
     * @param forceNetworkFetch
     * @return 返回食品列表
     */
    @FlowPreview
    override fun getFoodItems(forceNetworkFetch: Boolean): Flow<FoodResult<List<FoodItem>>> {
        //获取云端数据
        val networkThenDb = remoteCloudDataStore.getFoodItems().flatMapConcat {
            //保存云端数据到本地
            localDataStore.addFoodItems(it).flatMapConcat {
                flow { emit(FoodResult.Success(it)) }
            }
        }.flowOn(dispatcher.io)

        return if (forceNetworkFetch) {
            networkThenDb
        } else {
            //本地获取
            localDataStore.getFoodItems().flatMapConcat {
                if (it.count() > 0) {
                    flow { emit(FoodResult.Success(it)) }
                } else {
                    networkThenDb
                }
            }.flowOn(dispatcher.io)
        }
    }
}
