package com.example.mealsappviews.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mealInfo")
data class Meal(
    val dateModified: Any?,
    @PrimaryKey
    val idMeal: String,
    val strArea: String?,
    val strCategory: String?,
    val strCreativeCommonsConfirmed: Any?,
    val strDrinkAlternate: Any?,
    val strImageSource: Any?,
    val ingredients: List<String?>,
    val measures: List<String?>,
    val strInstructions: String?,
    val strMeal: String?,
    val strMealThumb: String?,
    val strSource: String?,
    val strTags: String?,
    val strYoutube: String?
) {
    companion object {
        fun fromResponse(mealResponse: MealResponse): Meal {
            val ingredients = mutableListOf<String?>()
            ingredients.add(mealResponse.strIngredient1)
            ingredients.add(mealResponse.strIngredient2)
            ingredients.add(mealResponse.strIngredient3)
            ingredients.add(mealResponse.strIngredient4)
            ingredients.add(mealResponse.strIngredient5)
            ingredients.add(mealResponse.strIngredient6)
            ingredients.add(mealResponse.strIngredient7)
            ingredients.add(mealResponse.strIngredient8)
            ingredients.add(mealResponse.strIngredient9)
            ingredients.add(mealResponse.strIngredient10)
            ingredients.add(mealResponse.strIngredient11)
            ingredients.add(mealResponse.strIngredient12)
            ingredients.add(mealResponse.strIngredient13)
            ingredients.add(mealResponse.strIngredient14)
            ingredients.add(mealResponse.strIngredient15)
            ingredients.add(mealResponse.strIngredient16)
            ingredients.add(mealResponse.strIngredient17)
            ingredients.add(mealResponse.strIngredient18)
            ingredients.add(mealResponse.strIngredient19)
            ingredients.add(mealResponse.strIngredient20)
            val measures = mutableListOf<String?>()
            measures.add(mealResponse.strMeasure1)
            measures.add(mealResponse.strMeasure2)
            measures.add(mealResponse.strMeasure3)
            measures.add(mealResponse.strMeasure4)
            measures.add(mealResponse.strMeasure5)
            measures.add(mealResponse.strMeasure6)
            measures.add(mealResponse.strMeasure7)
            measures.add(mealResponse.strMeasure8)
            measures.add(mealResponse.strMeasure9)
            measures.add(mealResponse.strMeasure10)
            measures.add(mealResponse.strMeasure11)
            measures.add(mealResponse.strMeasure12)
            measures.add(mealResponse.strMeasure13)
            measures.add(mealResponse.strMeasure14)
            measures.add(mealResponse.strMeasure15)
            measures.add(mealResponse.strMeasure16)
            measures.add(mealResponse.strMeasure17)
            measures.add(mealResponse.strMeasure18)
            measures.add(mealResponse.strMeasure19)
            measures.add(mealResponse.strMeasure20)

            return Meal(
                dateModified = mealResponse.dateModified,
                idMeal = mealResponse.idMeal,
                strArea = mealResponse.strArea,
                strCategory = mealResponse.strCategory,
                strCreativeCommonsConfirmed = mealResponse.strCreativeCommonsConfirmed,
                strDrinkAlternate = mealResponse.strDrinkAlternate,
                strImageSource = mealResponse.strImageSource,
                ingredients = ingredients.filter { !it.isNullOrEmpty() },
                measures = measures.filter{ !it.isNullOrEmpty() },
                strInstructions = mealResponse.strInstructions,
                strMeal = mealResponse.strMeal,
                strMealThumb = mealResponse.strMealThumb,
                strSource = mealResponse.strSource,
                strTags = mealResponse.strTags,
                strYoutube = mealResponse.strYoutube
            )
        }
    }
}

