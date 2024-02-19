package com.example.mealsappviews.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mealsappviews.db.MealDatabase
import com.example.mealsappviews.pojo.Meal
import com.example.mealsappviews.pojo.MealList
import com.example.mealsappviews.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    val mealDatabase: MealDatabase
) : ViewModel() {
    private val mealDetailsLiveData = MutableLiveData<Meal>()



    fun getMealDetails(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val newItem =response.body()!!.meals[0]
                    mealDetailsLiveData.value=Meal.fromResponse(newItem)
                    mealDatabase.mealDao().getAllMeals().value?.forEach {
                        Log.d("observeMealDetailsLiveData1", "onResponse: ${it.idMeal}")
                    }
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())
            }
        })
    }

    fun observeMealDetailsLiveData(): LiveData<Meal> {
        return mealDetailsLiveData
    }


}
class MealViewModelFactory(private val mealDatabase: MealDatabase):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
    ): T {
        return MealViewModel(mealDatabase) as T
    }
}