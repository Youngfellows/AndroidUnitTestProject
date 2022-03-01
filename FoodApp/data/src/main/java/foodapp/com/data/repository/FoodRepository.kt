package foodapp.com.data.repository

import foodapp.com.data.FoodResult
import foodapp.com.data.model.FoodItem
import kotlinx.coroutines.flow.Flow

/**
 * 获取数据的仓库
 */
interface FoodRepository {


    /**
     * 获取食品列表
     * @param forceNetworkFetch
     * @return 返回食品列表
     */
    fun getFoodItems(forceNetworkFetch: Boolean): Flow<FoodResult<List<FoodItem>>>

    /**
     * 根据ID获取食品项
     * @param id
     * @return 返回食品项
     */
    fun getFoodItem(id: Int): Flow<FoodResult<FoodItem>>
}
