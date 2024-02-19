package com.example.mealsappviews.retrofit

import com.example.mealsappviews.pojo.CategoryList
import com.example.mealsappviews.pojo.CategoryMealsList
import com.example.mealsappviews.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
//    https://www.themealdb.com/api/json/v1/1/random.php
    @GET("random.php")
    fun getRandomMeal():Call<MealList>
    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id:String):Call<MealList>
    @GET("categories.php")
    fun getCategories():Call<CategoryList>
    @GET("filter.php?")
    fun getMealsByCategory(@Query("c") category:String):Call<CategoryMealsList>
    @GET("search.php")
    fun getMealsBySearch(@Query("s") searchQuery:String):Call<MealList>
}