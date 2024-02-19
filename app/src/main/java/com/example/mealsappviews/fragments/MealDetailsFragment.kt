package com.example.mealsappviews.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mealsappviews.R
import com.example.mealsappviews.activities.MainActivity
import com.example.mealsappviews.databinding.FragmentMealDetailsBinding
import com.example.mealsappviews.db.MealDatabase
import com.example.mealsappviews.pojo.Meal
import com.example.mealsappviews.viewModel.HomeViewModel
import com.example.mealsappviews.viewModel.MealViewModel
import com.example.mealsappviews.viewModel.MealViewModelFactory
import java.util.concurrent.atomic.AtomicBoolean


class MealDetailsFragment : Fragment() {
    private lateinit var mealID: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealViewModel: MealViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var youtubeLink: String
    private lateinit var mealToSave: Meal
    private lateinit var mealDatabase: MealDatabase
    private lateinit var binding: FragmentMealDetailsBinding
    private val isFavorite = AtomicBoolean(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = (activity as MainActivity).viewModel
        mealDatabase = MealDatabase.getInstance(requireContext())
        mealViewModel =
            ViewModelProvider(
                viewModelStore,
                MealViewModelFactory(mealDatabase)
            )[MealViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getExtras()
        onLoading()
        mealViewModel.getMealDetails(mealID)
        observeMealDetailsLiveData()
        setContentInViews()
        onYoutubeClick()
        observeItemInFavoriteLiveData()

        //onFavoriteClick
        binding.btnSave.setOnClickListener {
            if (isFavorite.get()) {
                homeViewModel.deleteMeal(mealToSave)
                Toast.makeText(context, "Meal removed", Toast.LENGTH_SHORT).show()
            } else {
                homeViewModel.upsertMeal(mealToSave)
                Toast.makeText(context, "Meal saved", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun onYoutubeClick() {
        binding.imgYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink)))
        }

    }

    private fun observeMealDetailsLiveData() {
        mealViewModel.observeMealDetailsLiveData().observe(
            viewLifecycleOwner
        ) { meal ->
            mealToSave = meal
            setIngredients(meal)
            binding.tvCategoryInfo.text = "${binding.tvCategoryInfo.text} ${meal.strCategory}"
            binding.tvAreaInfo.text = "Area : ${meal.strArea}"
            binding.tvInstructionsContent.text = meal.strInstructions
            youtubeLink = meal.strYoutube.toString()
            Log.d("observeMealDetailsLiveData", meal.idMeal)
            onResponse()
        }
    }

    private fun observeItemInFavoriteLiveData() {
        homeViewModel.getItemById(mealID).observe(viewLifecycleOwner) { meal ->
            meal?.let {
                isFavorite.set(true)
                binding.btnSave.setImageResource(R.drawable.ic_favorite)

            } ?: run {
                isFavorite.set(false)
                binding.btnSave.setImageResource(R.drawable.ic_favorite_outlined)
            }

        }
    }

    // ingredients - measure
    private fun setIngredients(meal: Meal) {
        IntRange(0, meal.ingredients.size.coerceAtMost(meal.measures.size) - 1).forEach {
            if (!meal.ingredients[it].isNullOrEmpty() && !meal.measures[it].isNullOrEmpty()) {
                val tvDynamic = TextView(context)
                tvDynamic.text = "${meal.ingredients[it]} - ${meal.measures[it]}"
                binding.ingredientsListLayout.addView(tvDynamic)
            }
        }
    }

    private fun setContentInViews() {
        Glide.with(this).clear(binding.imgMealDetail)
        Glide.with(this).load(mealThumb).into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
    }

    private fun getExtras() {
        mealID = arguments?.getString("MEAL_ID").toString()
        mealName = arguments?.getString("MEAL_NAME").toString()
        mealThumb = arguments?.getString("MEAL_THUMB").toString()
    }

    private fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvCategoryInfo.visibility = View.GONE
        binding.tvAreaInfo.visibility = View.GONE
        binding.tvInstructions.visibility = View.GONE
        binding.tvInstructionsContent.visibility = View.GONE
    }

    private fun onResponse() {
        binding.progressBar.visibility = View.GONE
        binding.tvCategoryInfo.visibility = View.VISIBLE
        binding.tvAreaInfo.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvInstructionsContent.visibility = View.VISIBLE
    }

}