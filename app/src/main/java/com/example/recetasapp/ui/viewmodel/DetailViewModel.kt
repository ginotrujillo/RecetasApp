package com.example.recetasapp.ui.viewmodel // Asegúrate que sea tu paquete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetasapp.data.model.Meal
import com.example.recetasapp.data.network.RetrofitClient
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel : ViewModel() {

    // Solo necesitamos un 'Meal' (receta), no una lista
    private val _recipe = MutableLiveData<Meal>()
    val recipe: LiveData<Meal> = _recipe

    // (Opcional) Un LiveData para el estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Busca los detalles de UNA receta usando su ID.
     */
    fun getRecipeDetails(id: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Usamos la OTRA función de nuestra ApiService
                val response = RetrofitClient.instance.getRecipeDetails(id)

                // La API devuelve una lista, pero solo queremos el primer (y único) item
                response.meals?.firstOrNull()?.let {
                    _recipe.value = it
                }

            } catch (e: Exception) {
                // Manejar error si es necesario
            } finally {
                _isLoading.value = false
            }
        }
    }
}