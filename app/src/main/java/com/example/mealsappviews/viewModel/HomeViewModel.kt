package com.example.mealsappviews.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mealsappviews.db.MealDatabase
import com.example.mealsappviews.pojo.Category
import com.example.mealsappviews.pojo.CategoryList
import com.example.mealsappviews.pojo.Meal
import com.example.mealsappviews.pojo.MealList
import com.example.mealsappviews.pojo.MealResponse
import com.example.mealsappviews.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    val mealDatabase: MealDatabase
) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<Meal>>()
    private var categoryListLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private val mealDetailsLiveData = MutableLiveData<Meal>()
    private val searchMealsLiveData = MutableLiveData<List<MealResponse>>()


    fun getMealDetails(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val newItem = response.body()!!.meals[0]
                    mealDetailsLiveData.value = Meal.fromResponse(newItem)
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())
            }
        })
    }

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful && response.body() != null) {
                    randomMealLiveData.value = Meal.fromResponse(response.body()!!.meals[0])
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getPopularItems() {
        val mealsList = mutableListOf<Meal>()
        for (i in 1..5) {
            RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
                override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                    if (response.isSuccessful && response.body() != null) {
                        val mealList = response.body()!!.meals
                        if (mealList.isNotEmpty()) {
                            mealsList.add(Meal.fromResponse(mealList[0]))
                        }
                        if (mealsList.size == 5) {
                            popularItemsLiveData.value = mealsList.toList()
                        }
                    }
                }

                override fun onFailure(call: Call<MealList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }
            })
        }
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.isSuccessful && response.body() != null) {
                    categoryListLiveData.value = response.body()!!.categories
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })

    }

    fun getItemById(mealId: String): LiveData<Meal?> {
        return mealDatabase.mealDao().getItemById(mealId)
    }

    fun getMealsBySearch(searchQuery: String) {
        RetrofitInstance.api.getMealsBySearch(searchQuery).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful && response.body() != null) {
                    searchMealsLiveData.postValue(response.body()!!.meals)
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })

    }

    fun upsertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun resetSearchResult(){
        searchMealsLiveData.value = emptyList()
    }

    fun observeSearchMealsLiveData(): LiveData<List<MealResponse>> {
        return searchMealsLiveData
    }

    fun observeMealDetailsLiveData(): LiveData<Meal> {
        return mealDetailsLiveData
    }

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observePopularItemsLiveData(): LiveData<List<Meal>> {
        return popularItemsLiveData
    }

    fun observeCategoryListLiveData(): LiveData<List<Category>> {
        return categoryListLiveData
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }

}

class HomeViewModelFactory(private val mealDatabase: MealDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
    ): T {
        return HomeViewModel(mealDatabase) as T
    }
}