package foodapp.com.foodapp.ui.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import foodapp.com.data.FoodResult
import foodapp.com.data.model.FoodItem
import foodapp.com.data.network.utils.ErrorUtils
import foodapp.com.domain.fooditems.FoodUsecase
import foodapp.com.foodapp.ErrorType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FoodViewModel @ViewModelInject constructor(
    private val usecase: FoodUsecase
) : ViewModel() {

    /**
     * 被观察数据-食品列表
     */
    private var _foodItemsLiveData = MutableLiveData<FoodResult<List<FoodItem>>>()

    val foodItemsLiveData: LiveData<FoodResult<List<FoodItem>>>
        get() = _foodItemsLiveData

    /**
     * 被观察数据-食品列表项目
     */
    private var _foodItemLiveData = MutableLiveData<FoodResult<FoodItem>>()
    val foodItemLiveData: LiveData<FoodResult<FoodItem>>
        get() = _foodItemLiveData

    /**
     * 获取食品列表
     * @param forceFetch
     */
    @ExperimentalCoroutinesApi
    fun getFoodItems(forceFetch: Boolean) {
        viewModelScope.launch {
            usecase.getFoodItems(forceFetch)
                .onStart {
                    //加载中
                    _foodItemsLiveData.value = FoodResult.Loading
                }.catch {
                    //请求异常
                    _foodItemsLiveData.value = FoodResult.Error(it)
                }.collect {
                    //获取结果
                    _foodItemsLiveData.value = it
                }
        }
    }

    /**
     * 根据ID获取食品列表项
     * @param id
     */
    fun getFoodItem(id: Int) {
        viewModelScope.launch {
            usecase.getFoodItem(id)
                .onStart {
                    //加载中
                    _foodItemLiveData.value = FoodResult.Loading
                }.catch {
                    //请求异常
                    _foodItemLiveData.value = FoodResult.Error(it)
                }.collect {
                    //获取结果
                    _foodItemLiveData.value = it
                }
        }
    }

    fun parseError(throwable: Throwable) = when {
        ErrorUtils.isTimeOut(throwable) -> {
            ErrorType.TIMEOUT_ERROR
        }
        ErrorUtils.isConnectionError(throwable) -> {
            ErrorType.CONNECTION_ERROR
        }
        else -> {
            ErrorType.GENERIC_ERROR
        }
    }
}
