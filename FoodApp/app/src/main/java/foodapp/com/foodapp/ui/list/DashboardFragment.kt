package foodapp.com.foodapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import foodapp.com.data.FoodResult
import foodapp.com.data.model.FoodItem
import foodapp.com.foodapp.R
import foodapp.com.foodapp.base.BaseFragment
import foodapp.com.foodapp.databinding.FragmentDashboardBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * 食品列表页面
 */
@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    /**
     * 食品列表数据加载的ViewModel
     */
    private val viewModel: FoodViewModel by viewModels()

    private fun showLoading() {
        binding.foodListRecyclerView.visibility = View.GONE
        binding.errorView.visibility = View.VISIBLE
        binding.errorView.showProgress()
    }

    private fun hideLoading() {
        binding.errorView.showProgress(false)
    }

    @ExperimentalCoroutinesApi
    private fun onError(throwable: Throwable) {
        binding.foodListRecyclerView.visibility = View.GONE
        binding.errorView.showEmptyView()
        val errorType = viewModel.parseError(throwable)
        binding.errorView.setContents(
            errorType.icon,
            errorType.title,
            errorType.subtitle,
            R.string.inv_try_again
        ) {
            //重新获取食品列表
            viewModel.getFoodItems(true)
        }
    }

    /**
     * 更新食品列表
     * @param foodItems 食品列表
     */
    private fun onLoadFoodItems(foodItems: List<FoodItem>) {
        binding.errorView.showEmptyView(false)
        binding.errorView.visibility = View.GONE
        binding.foodListRecyclerView.visibility = View.VISIBLE

        //刷新食品列表
        if (binding.foodListRecyclerView.adapter != null) {
            (binding.foodListRecyclerView.adapter as FoodListAdapter).setFoodItems(foodItems)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        viewModel.foodItemsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                FoodResult.Loading -> {
                    showLoading()
                }
                is FoodResult.Error -> {
                    hideLoading()
                    onError(it.throwable)
                    startPostponedEnterTransition()
                }
                is FoodResult.Success -> {
                    hideLoading()
                    onLoadFoodItems(it.data)
                    startPostponedEnterTransition()
                }
            }
        })

        //获取食品列表
        viewModel.getFoodItems(false)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    /**
     * 设置食品列表数据适配器
     */
    private fun setupAdapter() {
        binding.foodListRecyclerView.adapter = FoodListAdapter(
            listener = { foodItem: FoodItem,
                         foodImageView: ImageView,
                         heartImageView: ImageView ->

                //页面跳转，传递参数
                val action = DashboardFragmentDirections.dashboardToDetail(foodItem.id)
                val extras: FragmentNavigator.Extras = FragmentNavigatorExtras(
                    foodImageView to foodItem.votes.toString()
                )
                findNavController().navigate(action, extras)

                exitTransition = MaterialElevationScale(false).apply {
                    duration = 300
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = 300
                }
            })
    }

    /**
     * 为页面绑定ViewBindding
     * @param inflater
     * @param container
     * @param bundle
     * @return
     */
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?) =
        FragmentDashboardBinding.inflate(inflater, container, false)
}
