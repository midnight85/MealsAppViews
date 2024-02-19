package com.example.mealsappviews.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.mealsappviews.pojo.Meal

@Dao
interface MealDao {
    @Upsert
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealInfo")
    fun getAllMeals(): LiveData<List<Meal>>

    @Query("SELECT * FROM mealInfo WHERE idMeal = :idMeal")
    fun getItemById(idMeal: String): LiveData<Meal?>
}