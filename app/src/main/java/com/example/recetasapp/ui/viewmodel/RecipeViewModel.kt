package com.example.recetasapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetasapp.data.model.Meal
import com.example.recetasapp.data.network.RetrofitClient
// IMPORTA LAS LIBRERÍAS DE FIREBASE
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.launch
import java.lang.Exception

class RecipeViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Meal>>()
    val recipes: LiveData<List<Meal>> = _recipes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // --- AÑADE ESTO ---
    // Obtenemos una instancia de la base de datos de Firestore
    private val db = Firebase.firestore
    // --- FIN DE AÑADIR ---

    fun searchRecipes(query: String) {

        // --- AÑADE ESTA LÍNEA ---
        // Primero, guardamos la búsqueda
        saveSearchQuery(query)
        // --- FIN DE AÑADIR ---

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.searchRecipes(query)
                _recipes.value = response.meals ?: emptyList()
            } catch (e: Exception) {
                _recipes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- AÑADE ESTA NUEVA FUNCIÓN ---
    /**
     * Guarda el término de búsqueda en la colección "searches" de Firestore
     */
    private fun saveSearchQuery(query: String) {
        // Crea un "documento" simple
        val searchData = hashMapOf(
            "term" to query.lowercase(), // Guardamos en minúsculas para un mejor análisis
            "timestamp" to System.currentTimeMillis() // Guardamos la fecha
        )

        // Lo guarda en una "colección" (tabla) llamada "searches"
        // Si no existe, Firestore la crea automáticamente
        db.collection("searches")
            .add(searchData)
        // Opcional: puedes añadir .addOnSuccessListener y .addOnFailureListener
        // para saber si se guardó correctamente, pero no es necesario.
    }
    // --- FIN DE AÑADIR ---
}