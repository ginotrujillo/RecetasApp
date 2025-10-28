package com.example.recetasapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recetasapp.ui.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var ivDetailImage: ImageView
    private lateinit var tvDetailName: TextView
    private lateinit var tvDetailInstructions: TextView

    // --- AÑADE ESTO (SI NO TIENES UN PROGRESSBAR) ---
    // private lateinit var progressBar: ProgressBar
    // (Asegúrate de tener un ProgressBar en activity_detail.xml)
    // --- FIN DE AÑADIR ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        ivDetailImage = findViewById(R.id.ivDetailImage)
        tvDetailName = findViewById(R.id.tvDetailName)
        tvDetailInstructions = findViewById(R.id.tvDetailInstructions)
        // progressBar = findViewById(R.id.progressBar) // Descomenta si tienes uno

        val recipeId = intent.getStringExtra("RECIPE_ID")

        setupObservers() // Modificaremos esta función

        recipeId?.let {
            viewModel.getRecipeDetails(it)
        }
    }

    private fun setupObservers() {
        // 1. Observador para la receta (FOTO Y TÍTULO)
        // Esto sigue igual que antes
        viewModel.recipe.observe(this, Observer { recipe ->
            Glide.with(this)
                .load(recipe.thumbnail)
                .into(ivDetailImage)

            tvDetailName.text = recipe.name
            // NO TOCAMOS LAS INSTRUCCIONES AQUÍ
        })

        // --- AÑADE ESTE NUEVO OBSERVADOR ---
        // 2. Observador para las instrucciones de GPT
        viewModel.gptInstructions.observe(this, Observer { instructions ->
            // Actualiza el TextView con el texto de GPT
            tvDetailInstructions.text = instructions
        })
        // --- FIN DE AÑADIR ---

        // 3. Observador para el ProgressBar (ahora cubre ambas llamadas)
        viewModel.isLoading.observe(this, Observer { isLoading ->
            // progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }
}