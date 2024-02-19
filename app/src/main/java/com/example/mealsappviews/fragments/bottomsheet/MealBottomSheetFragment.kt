package com.example.mealsappviews.fragments.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mealsappviews.R
import com.example.mealsappviews.activities.MainActivity
import com.example.mealsappviews.databinding.FragmentMealBottomSheetBinding
import com.example.mealsappviews.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"


class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealId?.let { viewModel.getMealDetails(it) }
        observeMealDetailsLiveData()

    }

    private fun observeMealDetailsLiveData() {
        viewModel.observeMealDetailsLiveData().observe(viewLifecycleOwner) { meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgBsheet)
            binding.tvMealCountry.text = meal.strArea
            binding.tvMealCategory.text = meal.strCategory
            binding.tvMealNameInBtmsheet.text = meal.strMeal
            binding.root.setOnClickListener {
                val args = Bundle()
                args.putString("MEAL_ID", meal.idMeal)
                args.putString("MEAL_NAME", meal.strMeal)
                args.putString("MEAL_THUMB", meal.strMealThumb)
                findNavController().navigate(R.id.mealDetailsFragment, args)
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) = MealBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putString(MEAL_ID, param1)
            }
        }
    }
}