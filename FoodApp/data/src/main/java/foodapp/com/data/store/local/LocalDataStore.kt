package foodapp.com.data.store.local

import foodapp.com.data.model.FoodItem
import kotlinx.coroutines.flow.Flow

/**
 * 本地数据仓库
 */
interface LocalDataStore {

    /**
     * 添加食品列表
     * @param foodItems 食品列表
     * @return
     */
    fun addFoodItems(foodItems: List<FoodItem>): Flow<List<FoodItem>>

    /**
     * 根据ID获取食品列表项
     * @param id
     * @return
     */
    fun getFoodItem(id: Int): Flow<FoodItem>

    /**
     * 获取食品列表项
     * @return
     */
    fun getFoodItems(): Flow<List<FoodItem>>
}
