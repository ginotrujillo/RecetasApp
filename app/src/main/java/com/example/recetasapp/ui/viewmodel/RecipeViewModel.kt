package com.example.recetasapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetasapp.data.model.Meal // <-- IMPORTA TU MODELO
import com.example.recetasapp.data.network.RetrofitClient // <-- IMPORTA TU CLIENTE
import kotlinx.coroutines.launch
import java.lang.Exception

class RecipeViewModel : ViewModel() {

    // --- LiveData para la Lista de Recetas ---

    // 1. PRIVADO y Mutable: Solo el ViewModel puede cambiar esta lista.
    private val _recipes = MutableLiveData<List<Meal>>()

    // 2. PÚBLICO e Inmutable: La Activity solo puede LEER los datos de aquí.
    val recipes: LiveData<List<Meal>> = _recipes

    // --- LiveData para el Estado de Carga (ProgressBar) ---
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Función principal para buscar recetas.
     * Usa Coroutines (viewModelScope) para llamar a la API en un hilo secundario.
     */
    fun searchRecipes(query: String) {

        // 1. Mostrar el ProgressBar
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // 2. Llamar a la API (usando el cliente de Retrofit del Paso 3)
                val response = RetrofitClient.instance.searchRecipes(query)

                // 3. Actualizar el LiveData con los resultados
                // (Usamos ?: emptyList() por si la búsqueda no devuelve nada, la API devuelve 'null')
                _recipes.value = response.meals ?: emptyList()

            } catch (e: Exception) {
                // 4. Si hay un error (ej. sin internet), mostrar una lista vacía
                _recipes.value = emptyList()

            } finally {
                // 5. Ocultar el ProgressBar (tanto si hubo éxito como si hubo error)
                _isLoading.value = false
            }
        }
    }
}