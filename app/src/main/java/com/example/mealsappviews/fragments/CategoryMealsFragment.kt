package com.example.mealsappviews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mealsappviews.R
import com.example.mealsappviews.adapters.CategoryMealsAdapter
import com.example.mealsappviews.databinding.FragmentCategoryMealsBinding
import com.example.mealsappviews.pojo.CategoryMeal
import com.example.mealsappviews.viewModel.CategoryMealsViewModel

class CategoryMealsFragment : Fragment() {
    private lateinit var binding: FragmentCategoryMealsBinding
    private lateinit var categoryMealsMvvm: CategoryMealsViewModel
    private lateinit var categoryName: String
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryMealsMvvm = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
        categoryMealsAdapter = CategoryMealsAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCategoryMealsRecyclerView()
        getExtras()
        categoryMealsMvvm.getMealsByCategory(categoryName)
        observeCategoryMealsLiveData()
        onCategoryMealClick()
    }
    private fun prepareCategoryMealsRecyclerView() {
        binding.recMealsByCategory.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }

    private fun observeCategoryMealsLiveData() {
        categoryMealsMvvm.observeCategoryMealsLiveData().observe(viewLifecycleOwner) { categoryMeals ->
            categoryMealsAdapter.setCategoryMealsList(categoryMeals = categoryMeals as ArrayList<CategoryMeal>)
            binding.tvMealsCount.text = "$categoryName: ${categoryMeals.size}"
        }
    }

    private fun getExtras() {
        categoryName = arguments?.getString("CATEGORY_NAME").toString()
    }

    private fun onCategoryMealClick() {
        categoryMealsAdapter.onItemClick = {
            val args = Bundle()
            args.putString("MEAL_ID", it.idMeal)
            args.putString("MEAL_NAME", it.strMeal)
            args.putString("MEAL_THUMB", it.strMealThumb)
            findNavController().navigate(R.id.mealDetailsFragment, args)

        }
    }
}