package com.example.recetasapp.data.model

import com.google.gson.annotations.SerializedName
data class MealResponse(
    @SerializedName("meals") val meals:
    List<Meal>
)

data class Meal (
    @SerializedName("idMeal")val id: String,
    @SerializedName("strMeal")val name:
    String,
    @SerializedName("strMealThumb")val thumbnail: String
)

@SerializedName("strInstructions") val instructions:String?=null;
@SerializedName("strIngredient1") val ingredient1:String?=null;
@SerializedName("strIngredient2") val ingredient2:String?=null;       