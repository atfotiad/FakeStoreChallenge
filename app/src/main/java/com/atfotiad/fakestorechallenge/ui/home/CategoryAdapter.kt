package com.atfotiad.fakestorechallenge.ui.home


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atfotiad.fakestorechallenge.databinding.ItemCategoryBinding
import com.bumptech.glide.Glide

class CategoryAdapter(
    private val onCategoryClick: (String) -> Unit
) : ListAdapter<String, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    var selectedCategory: String? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: String) {
            binding.categoryName.text = category
            Glide.with(binding.categoryImage.context)
                .load(getImageUrlForCategory(category))
                .circleCrop()
                .into(binding.categoryImage)

            // Highlight the selected category
            if (category == selectedCategory) {
                binding.root.setBackgroundColor(Color.LTGRAY)
                binding.categoryName.setTextColor(Color.BLACK)
            } else {
                binding.root.setBackgroundColor(Color.TRANSPARENT)
                binding.categoryName.setTextColor(Color.GRAY)
            }

            binding.root.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private fun getImageUrlForCategory(category: String): String {
        // Some Default images for each category
        return when (category) {
            "electronics" -> "https://fakestoreapi.com/img/81Zt42ioCgL._AC_SX679_.jpg"
            "jewelery" -> "https://fakestoreapi.com/img/71YAIFU48IL._AC_UL640_QL65_ML3_.jpg"
            "men's clothing" -> "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg"
            "women's clothing" -> "https://fakestoreapi.com/img/61pHAEJ4NML._AC_UX679_.jpg"
            else -> "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"
        }
    }
}