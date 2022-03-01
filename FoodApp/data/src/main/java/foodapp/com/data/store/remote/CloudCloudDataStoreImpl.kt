package foodapp.com.data.store.remote

import foodapp.com.data.network.RestApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * TODO云端数据获取
 *
 * @property mRestApi 网络接口
 */
class CloudCloudDataStoreImpl @Inject constructor(private val mRestApi: RestApi) : CloudDataStore {

    @FlowPreview
    override fun getFoodItems() = flow {
        emit(mRestApi.getFoodItems().fooditems)
    }
}
