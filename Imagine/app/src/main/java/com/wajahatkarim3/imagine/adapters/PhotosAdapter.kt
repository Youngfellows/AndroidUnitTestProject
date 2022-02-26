/*
* Copyright 2021 Wajahat Karim (https://wajahatkarim.com)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.wajahatkarim3.imagine.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wajahatkarim3.imagine.R
import com.wajahatkarim3.imagine.databinding.PhotoItemLayoutBinding
import com.wajahatkarim3.imagine.model.PhotoModel

/**
 *
 * @property onPhotoSelected 函数类型,点击回调
 */
class PhotosAdapter(val onPhotoSelected: (photo: PhotoModel, position: Int) -> Unit) :
    RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    /**
     * 数据集
     */
    private val photoItems: ArrayList<PhotoModel> = arrayListOf()

    /**
     * 创建ViewHolder并与视图绑定
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        var binding = PhotoItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    /**
     * 为视图绑定数据
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoItems[position], position)
    }

    override fun getItemCount() = photoItems.size

    fun updateItems(photosList: List<PhotoModel>) {
        photoItems.clear()
        photoItems.addAll(photosList)
        notifyDataSetChanged()
    }

    inner class PhotoViewHolder(val itemBinding: PhotoItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * 为视图绑定数据
         * @param photoModel item数据
         * @param position 位置
         */
        fun bind(photoModel: PhotoModel, position: Int) {
            itemBinding.apply {
                imgPhoto.load(photoModel.urls.thumb) {
                    placeholder(R.color.color_box_background)
                    crossfade(true)
                }

                cardPhoto.setOnClickListener {
                    onPhotoSelected(photoModel, position)
                }
            }
        }
    }
}
