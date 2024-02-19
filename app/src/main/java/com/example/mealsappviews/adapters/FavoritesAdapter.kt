package com.example.mealsappviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mealsappviews.databinding.CategoryItemBinding
import com.example.mealsappviews.pojo.Meal

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoritesAdapterViewHolder>() {
    lateinit var onItemClick: ((Meal)->Unit)

    inner class FavoritesAdapterViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
     val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapterViewHolder {
        return FavoritesAdapterViewHolder(
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun onBindViewHolder(holder: FavoritesAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]
        holder.binding.imgCategoryItem.scaleType= ImageView.ScaleType.CENTER_CROP
        Glide.with(holder.itemView).load(meal.strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.imgCategoryItem)
        holder.binding.tvCategoryTitle.text=meal.strMeal
        holder.itemView.setOnClickListener { onItemClick(meal) }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}