package com.task.ui.component.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.task.R
import com.task.RECIPE_ITEM_KEY
import com.task.data.Resource
import com.task.data.dto.recipes.RecipesItem
import com.task.databinding.DetailsLayoutBinding
import com.task.ui.base.BaseActivity
import com.task.utils.observe
import com.task.utils.toGone
import com.task.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

/**
 * 菜谱详情页
 */
@AndroidEntryPoint
class DetailsActivity : BaseActivity() {

    /**
     * 菜谱详情的ViewModel
     */
    private val viewModel: DetailsViewModel by viewModels()

    /**
     * 菜谱详情的DataBinding
     */
    private lateinit var binding: DetailsLayoutBinding
    private var menu: Menu? = null


    override fun initViewBinding() {
        binding = DetailsLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initIntentData(intent.getParcelableExtra(RECIPE_ITEM_KEY) ?: RecipesItem())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        this.menu = menu
        viewModel.isFavourites()
        return true
    }

    fun onClickFavorite(mi: MenuItem) {
        mi.isCheckable = false
        if (viewModel.isFavourite.value?.data == true) {
            viewModel.removeFromFavourites()
        } else {
            viewModel.addToFavourites()
        }
    }

    override fun observeViewModel() {
        observe(viewModel.recipeData, ::initializeView)
        observe(viewModel.isFavourite, ::handleIsFavourite)
    }

    /**
     * 函数回调
     * @param isFavourite 是否喜欢收藏数据
     */
    private fun handleIsFavourite(isFavourite: Resource<Boolean>) {
        when (isFavourite) {
            is Resource.Loading -> {
                binding.pbLoading.toVisible()
            }
            is Resource.Success -> {
                isFavourite.data?.let {
                    handleIsFavouriteUI(it)
                    menu?.findItem(R.id.add_to_favorite)?.isCheckable = true
                    binding.pbLoading.toGone()
                }
            }
            is Resource.DataError -> {
                menu?.findItem(R.id.add_to_favorite)?.isCheckable = true
                binding.pbLoading.toGone()
            }
        }
    }

    private fun handleIsFavouriteUI(isFavourite: Boolean) {
        menu?.let {
            it.findItem(R.id.add_to_favorite)?.icon =
                if (isFavourite) {
                    ContextCompat.getDrawable(this, R.drawable.ic_star_24)
                } else {
                    ContextCompat.getDrawable(this, R.drawable.ic_outline_star_border_24)
                }
        }
    }

    /**
     * 回调函数 - 函数类型
     * @param recipesItem 菜谱项数据
     */
    private fun initializeView(recipesItem: RecipesItem) {
        binding.tvName.text = recipesItem.name
        binding.tvHeadline.text = recipesItem.headline
        binding.tvDescription.text = recipesItem.description
        Picasso.get().load(recipesItem.image).placeholder(R.drawable.ic_healthy_food_small)
            .into(binding.ivRecipeImage)

    }
}
