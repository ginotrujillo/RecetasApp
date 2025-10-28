package com.example.recetasapp.ui.viewmodel // Asegúrate que sea tu paquete


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetasapp.data.model.Meal
import com.example.recetasapp.data.network.RetrofitClient
// IMPORTA LAS LIBRERÍAS DE FIREBASE FUNCTIONS
import com.google.firebase.ktx.Firebase
import com.google.firebase.functions.ktx.functions
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel : ViewModel() {

    private val _recipe = MutableLiveData<Meal>()
    val recipe: LiveData<Meal> = _recipe

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // --- AÑADE ESTO ---
    // Un nuevo LiveData solo para las instrucciones de GPT
    private val _gptInstructions = MutableLiveData<String>()
    val gptInstructions: LiveData<String> = _gptInstructions
    // --- FIN DE AÑADIR ---

    fun getRecipeDetails(id: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // 1. Aún buscamos la receta en TheMealDB (para la foto y el título)
                val response = RetrofitClient.instance.getRecipeDetails(id)
                response.meals?.firstOrNull()?.let { meal ->
                    _recipe.value = meal // Actualiza la UI (foto y título)

                    // 2. ¡NUEVO! Ahora, llamamos a la Cloud Function
                    fetchGptInstructions(meal.name)
                }
            } catch (e: Exception) {
                _isLoading.value = false // Oculta el spinner si TheMealDB falla
            }
        }
    }

    // --- AÑADE ESTA NUEVA FUNCIÓN ---
    /**
     * Llama a la Firebase Cloud Function "getGptRecipe"
     */
    private fun fetchGptInstructions(recipeName: String) {
        val functions = Firebase.functions

        // Prepara los datos para enviar a la función (el nombre de la receta)
        val data = hashMapOf("recipeName" to recipeName)

        functions
            .getHttpsCallable("getGptRecipe") // Nombre de tu función en la nube
            .call(data)
            .addOnSuccessListener { result ->
                // Asumimos que la función devuelve un JSON como { "instructions": "..." }
                val instructions = (result.data as? Map<*, *>)?.get("instructions") as? String
                _gptInstructions.value = instructions ?: "No se pudieron generar instrucciones."
                _isLoading.value = false // Oculta el spinner
            }
            .addOnFailureListener { exception ->
                _gptInstructions.value = "Error al contactar IA: ${exception.message}"
                _isLoading.value = false // Oculta el spinner
            }
    }
    // --- FIN DE AÑADIR ---
}