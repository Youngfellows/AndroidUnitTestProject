package foodapp.com.domain.fooditems

import foodapp.com.data.repository.FoodRepository
import javax.inject.Inject

/**
 * 食品用例
 * @property foodRepository
 */
class FoodUsecase @Inject constructor(private val foodRepository: FoodRepository) {

    /**
     * 获取食品列表
     * @param forceNetworkFetch
     * @return 返回食品列表
     */
    fun getFoodItems(forceNetworkFetch: Boolean) = foodRepository.getFoodItems(forceNetworkFetch)

    /**
     * 根据ID获取食品项
     * @param id
     * @return 返回食品项
     */
    fun getFoodItem(id: Int) = foodRepository.getFoodItem(id)
}