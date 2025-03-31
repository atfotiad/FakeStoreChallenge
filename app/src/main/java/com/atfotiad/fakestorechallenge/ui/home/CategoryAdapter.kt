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
/**
 * [CategoryAdapter] is a RecyclerView adapter that displays a list of categories.
 * @param onCategoryClick is a lambda function that is called when a category is clicked.
 * @property selectedCategory is a String object that contains the selected category.
 * */
class CategoryAdapter(
    private val onCategoryClick: (String) -> Unit
) : ListAdapter<String, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    var selectedCategory: String? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * [onCreateViewHolder] is a function that creates a new ViewHolder.
     * @param parent is a ViewGroup object that contains the parent view.
     * @param viewType is an Int object that contains the view type.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    /**
     *  [onBindViewHolder] is a function that binds the ViewHolder.
     *  @param holder is a CategoryViewHolder object that contains the ViewHolder.
     *  @param position is an Int object that contains the position of the item.
     * */
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

    /**
     *  [CategoryViewHolder] is a RecyclerView ViewHolder that displays a category.
     *  @param binding is an ItemCategoryBinding object that contains the binding for the view.
     * */
    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
            /**
             *  [bind] is a function that binds the ViewHolder.
             *  @param category is a String object that contains the category.
             * */
        fun bind(category: String) {
            binding.categoryName.text = category
            Glide.with(binding.categoryImage.context)
                .load(getImageUrlForCategory(category))
                .circleCrop()
                .into(binding.categoryImage)

            // Highlight the selected category
            if (category == selectedCategory) {
                binding.root.setBackgroundColor(Color.TRANSPARENT)
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

    /**
     *  [CategoryDiffCallback] is a DiffUtil.ItemCallback that compares two categories.
     * */
    class CategoryDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    /**
     *  [getImageUrlForCategory] is a function that returns the image URL for a given category.
     *  @param category is a String object that contains the category.
     *  @return a String object that contains the image URL.
     * */
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