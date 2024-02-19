package com.example.mealsappviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mealsappviews.databinding.PopularItemsBinding
import com.example.mealsappviews.pojo.Meal

class MostPopularAdapter : RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {
    private var mealsList = ArrayList<Meal>()
    lateinit var onItemClick: ((Meal) -> Unit)
    lateinit var onItemLongClick: ((Meal) -> Unit)

    fun setMealsList(mealsList: ArrayList<Meal>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(
            PopularItemsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealsList[position].strMealThumb).transition(
            DrawableTransitionOptions.withCrossFade()
        )
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick(mealsList[position])
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(mealsList[position])
            true
        }

    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    inner class PopularMealViewHolder(val binding: PopularItemsBinding) :
        RecyclerView.ViewHolder(binding.root)
}