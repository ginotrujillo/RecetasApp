package com.example.recetasapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://www.themealdb.com/"

    // 'by lazy' significa que Retrofit se creará solo la primera vez que lo necesitemos
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Usa Gson para convertir el JSON (texto) a nuestras clases de Kotlin (MealResponse)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}