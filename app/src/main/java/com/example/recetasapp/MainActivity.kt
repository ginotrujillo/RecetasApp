package com.example.recetasapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.ui.adapter.RecipeAdapter
import com.example.recetasapp.ui.viewmodel.RecipeViewModel
// IMPORTA LAS LIBRERÍAS DE ADMOB
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    // 1. OBTENER EL VIEWMODEL
    private val viewModel: RecipeViewModel by viewModels()

    // 2. DECLARAR LAS VISTAS
    private lateinit var adapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var adView: AdView
    // Variable para el anuncio


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- INICIALIZA ADMOB ---
        MobileAds.initialize(this) {}
        // --- FIN DE ADMOB ---

        // 3. INICIALIZAR VISTAS
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        progressBar = findViewById(R.id.progressBar)

        // --- CARGAR EL ANUNCIO ---
        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        // --- FIN DE CARGAR ANUNCIO ---

        // 4. CONFIGURAR EL RECYCLERVIEW
        setupRecyclerView()

        // 5. CONFIGURAR EL BUSCADOR
        setupSearchView()

        // 6. CONFIGURAR LOS OBSERVADORES
        setupObservers()
    }


    private fun setupRecyclerView() {
        // ARREGLADO: El constructor del Adapter solo necesita la lista
        adapter = RecipeAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        viewModel.searchRecipes(it)
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupObservers() {
        // Observador para la lista de recetas
        viewModel.recipes.observe(this, Observer { recipes ->
            adapter.updateData(recipes ?: emptyList())
        })

        // Observador para el estado de carga (ProgressBar)
        viewModel.isLoading.observe(this, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    // BORRADO: La función "onItemClick" se eliminó
    // porque los clics se manejan en el Adapter.
}