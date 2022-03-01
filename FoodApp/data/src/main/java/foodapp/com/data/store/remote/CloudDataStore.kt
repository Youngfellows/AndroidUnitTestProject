package foodapp.com.data.store.remote

import foodapp.com.data.model.FoodItem
import kotlinx.coroutines.flow.Flow

/**
 * 云端数据获取
 */
interface CloudDataStore {

    /**
     * 获取食品列表
     * @return
     */
    fun getFoodItems(): Flow<List<FoodItem>>
}
