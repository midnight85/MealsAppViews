package com.example.mealsappviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mealsappviews.databinding.SearchResultItemBinding
import com.example.mealsappviews.pojo.MealResponse

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder>() {
    lateinit var onItemClick:((MealResponse)->Unit)
    inner class SearchAdapterViewHolder(val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<MealResponse>() {
        override fun areItemsTheSame(oldItem: MealResponse, newItem: MealResponse): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: MealResponse, newItem: MealResponse): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchAdapter.SearchAdapterViewHolder {
        return SearchAdapterViewHolder(
            SearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.imgSearchResult)
        holder.binding.tvSearchResult.text=meal.strMeal
        holder.binding.tvCategoryInfo.text=meal.strCategory
        holder.binding.tvAreaInfo.text=meal.strArea
        holder.itemView.setOnClickListener {
            onItemClick(meal)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}