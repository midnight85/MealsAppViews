package com.example.mealsappviews.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mealsappviews.pojo.CategoryMeal
import com.example.mealsappviews.pojo.CategoryMealsList
import com.example.mealsappviews.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {
    private var categoryMealsLiveData = MutableLiveData<List<CategoryMeal>>()
    fun getMealsByCategory(category: String) {
        RetrofitInstance.api.getMealsByCategory(category)
            .enqueue(object : Callback<CategoryMealsList> {
                override fun onResponse(
                    call: Call<CategoryMealsList>,
                    response: Response<CategoryMealsList>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        categoryMealsLiveData.value = response.body()!!.meals
                    }

                }

                override fun onFailure(call: Call<CategoryMealsList>, t: Throwable) {
                    Log.d("CategoryMealsActivity", t.message.toString())
                }
            })
    }

    fun observeCategoryMealsLiveData():LiveData<List<CategoryMeal>>{
        return categoryMealsLiveData
    }
}