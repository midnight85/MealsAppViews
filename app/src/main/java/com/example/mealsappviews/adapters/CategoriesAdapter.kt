package com.example.mealsappviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mealsappviews.databinding.CategoryItemBinding
import com.example.mealsappviews.pojo.Category

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    private var categoriesList = ArrayList<Category>()
    lateinit var onItemClick: ((Category)->Unit)
    fun setCategoryList(categoriesList: ArrayList<Category>) {
        this.categoriesList = categoriesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesViewHolder {
        return CategoriesViewHolder(
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        Glide.with(holder.itemView).load(categoriesList[position].strCategoryThumb).transition(
            DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.imgCategoryItem)
        holder.binding.tvCategoryTitle.text = categoriesList[position].strCategory
        holder.itemView.setOnClickListener {
            onItemClick(categoriesList[position])
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    inner class CategoriesViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}