package foodapp.com.foodapp.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import foodapp.com.data.model.FoodItem
import foodapp.com.foodapp.R
import foodapp.com.foodapp.databinding.RowFoodItemBinding

/**
 * 食品列表适配器
 * @property listener 点击事件监听回调
 */
class FoodListAdapter(private val listener: (FoodItem, ImageView, ImageView) -> Unit) :
    RecyclerView.Adapter<FoodListAdapter.FoodViewHolder>() {

    /**
     * 数据列表
     */
    private var foodItems: ArrayList<FoodItem> = arrayListOf()

    /**
     * 创建与视图绑定的ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = RowFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun getItemCount(): Int = foodItems.size

    /**
     * 为视图绑定数据
     */
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foodItems[position], listener)
    }

    /**
     * 设置数据集
     * @param foodItems
     */
    fun setFoodItems(foodItems: List<FoodItem>) {
        this.foodItems.clear()
        this.foodItems.addAll(foodItems)
        notifyDataSetChanged()
    }

    class FoodViewHolder(private val binding: RowFoodItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * 绑定数据
         * @param foodItem 数据项
         * @param listener 点击回调
         */
        fun bind(foodItem: FoodItem, listener: (FoodItem, ImageView, ImageView) -> Unit) =
            with(itemView) {

                binding.profileImageView.load(foodItem.profileImage, builder = {
                    transformations(CircleCropTransformation())
                })

                binding.foodImageView.load(foodItem.heroImage, builder = {
                    placeholder(R.drawable.ic_svg_food)
                })

                binding.foodDescTextView.text = foodItem.foodDescription
                binding.nameTextView.text = foodItem.name
                binding.dateTextView.text = foodItem.date
                binding.votesTextView.text = "${foodItem.votes}"

                binding.foodImageView.transitionName = foodItem.heroImage
                binding.heartImageView.transitionName = foodItem.votes.toString()

                //点击事件回调
                setOnClickListener {
                    listener(foodItem, binding.foodImageView, binding.heartImageView)
                }
            }
    }
}