package com.example.recetasapp.data.network // Asegúrate que el paquete sea el tuyo
import com.example.recetasapp.data.model.MealResponse

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Busca recetas por nombre
    // 'suspend' le dice a Kotlin que esto se usará en una coroutine
    @GET("api/json/v1/1/search.php")
    suspend fun searchRecipes(
        @Query("s") query: String
    ): MealResponse

    // (Opcional) Busca una receta por ID
    @GET("api/json/v1/1/lookup.php")
    suspend fun getRecipeDetails(
        @Query("i") id: String
    ): MealResponse
}