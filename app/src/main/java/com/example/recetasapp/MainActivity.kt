package com.example.recetasapp // Asegúrate que el paquete sea el tuyo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
// Ojo: usa androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.ui.adapter.OnItemClickListener
// IMPORTA TUS CLASES
import com.example.recetasapp.ui.adapter.RecipeAdapter
import com.example.recetasapp.ui.viewmodel.RecipeViewModel

class MainActivity : AppCompatActivity(), OnItemClickListener {
    // 1. OBTENER EL VIEWMODEL
    // 'by viewModels()' es la forma moderna de obtener el ViewModel.
    // Se encargará de crearlo o de darnos el existente si la app rota.
    private val viewModel: RecipeViewModel by viewModels()

    // 2. DECLARAR LAS VISTAS Y EL ADAPTADOR
    private lateinit var adapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 3. INICIALIZAR VISTAS
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        progressBar = findViewById(R.id.progressBar)

        // 4. CONFIGURAR EL RECYCLERVIEW
        setupRecyclerView()

        // 5. CONFIGURAR EL BUSCADOR
        setupSearchView()

        // 6. CONFIGURAR LOS OBSERVADORES
        setupObservers()

        // ¡¡ASEGÚRATE DE BORRAR LA BÚSQUEDA DE PRUEBA DE AQUÍ!!
        // viewModel.searchRecipes("Arrabiata") // <-- ¡BORRA ESTO!
    }


    private fun setupRecyclerView() {
        adapter = RecipeAdapter(emptyList(), this) // Empezamos con una lista vacía
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Se llama cuando el usuario presiona "Enter" o el botón de buscar
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        // Le dice al ViewModel que inicie la búsqueda
                        viewModel.searchRecipes(it)
                    }
                }
                searchView.clearFocus() // Oculta el teclado
                return true
            }

            // No lo usaremos, pero debe estar
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupObservers() {
        // Observador para la lista de recetas
        viewModel.recipes.observe(this, Observer { recipes ->
            // Cuando la lista de recetas en el ViewModel cambia,
            // esta función se activa y actualiza el adaptador.
            adapter.updateData(recipes ?: emptyList())
        })

        // Observador para el estado de carga (ProgressBar)
        viewModel.isLoading.observe(this, Observer { isLoading ->
            // Muestra u oculta el ProgressBar
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    override fun onItemClick(mealId: String) {
        // 1. Crea la intención de abrir la nueva pantalla
        val intent = Intent(this, DetailActivity::class.java)

        // 2. "Empaqueta" el ID de la receta para enviarlo
        intent.putExtra("MEAL_ID", mealId)

        // 3. ¡Lanza la nueva actividad!
        startActivity(intent)
    }
}