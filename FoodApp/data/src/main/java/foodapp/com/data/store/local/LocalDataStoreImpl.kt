package foodapp.com.data.store.local

import foodapp.com.data.FoodDatabase
import foodapp.com.data.model.FoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/**
 * 本地数据仓库
 *
 * @property mFoodDatabase 数据库
 */
class LocalDataStoreImpl @Inject constructor(private val mFoodDatabase: FoodDatabase) :
    LocalDataStore {

    /**
     * 获取db保存的食品列表
     * @return
     */
    override fun getFoodItems(): Flow<List<FoodItem>> = flow {
        emit(mFoodDatabase.foodItemsDao().allFoodItems)
    }

    /**
     * 添加食品列表到db数据库
     * @param foodItems 食品列表
     * @return
     */
    override fun addFoodItems(foodItems: List<FoodItem>) = flow {
        mFoodDatabase.foodItemsDao().insertAll(foodItems)
        emit(mFoodDatabase.foodItemsDao().allFoodItems)
    }

    /**
     * 根据ID获取db数据库保存的食品项
     * @param id
     * @return
     */
    override fun getFoodItem(id: Int) = flow {
        emit(mFoodDatabase.foodItemsDao().getFoodItem(id))
    }
}
