package com.example.mealsappviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mealsappviews.databinding.CategoryItemBinding
import com.example.mealsappviews.pojo.CategoryMeal

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {
    private var categoryMealsList = ArrayList<CategoryMeal>()
    lateinit var onItemClick:((CategoryMeal)->Unit)
    fun setCategoryMealsList(categoryMeals: ArrayList<CategoryMeal>) {
        this.categoryMealsList = categoryMeals
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        holder.binding.imgCategoryItem.scaleType=ImageView.ScaleType.CENTER_CROP
        Glide.with(holder.itemView).load(categoryMealsList[position].strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into((holder.binding.imgCategoryItem))
        holder.binding.tvCategoryTitle.text = categoryMealsList[position].strMeal
        holder.itemView.setOnClickListener {
            onItemClick(categoryMealsList[position])
        }
    }

    override fun getItemCount(): Int {
        return categoryMealsList.size
    }

    inner class CategoryMealsViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}