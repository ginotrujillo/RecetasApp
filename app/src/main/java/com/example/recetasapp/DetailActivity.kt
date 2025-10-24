package com.example.recetasapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recetasapp.ui.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    // 1. Obtener el nuevo ViewModel
    private val viewModel: DetailViewModel by viewModels()

    // 2. Declarar las vistas del XML
    private lateinit var ivDetailImage: ImageView
    private lateinit var tvDetailName: TextView
    private lateinit var tvDetailInstructions: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail) // Conecta con el XML del Paso 2

        // 3. Conectar las vistas
        ivDetailImage = findViewById(R.id.ivDetailImage)
        tvDetailName = findViewById(R.id.tvDetailName)
        tvDetailInstructions = findViewById(R.id.tvDetailInstructions)

        // 4. RECIBIR EL ID que enviamos desde el Adapter
        val recipeId = intent.getStringExtra("RECIPE_ID")

        // 5. Configurar los observadores
        setupObservers()

        // 6. Pedirle al ViewModel que busque la receta
        recipeId?.let {
            viewModel.getRecipeDetails(it)
        }
    }

    private fun setupObservers() {
        // Observador para la receta
        viewModel.recipe.observe(this, Observer { recipe ->
            // Cuando la receta llegue, actualizamos la UI

            // Cargar imagen con Glide
            Glide.with(this)
                .load(recipe.thumbnail)
                .into(ivDetailImage)

            // Poner los textos
            tvDetailName.text = recipe.name
            tvDetailInstructions.text = recipe.instructions
        })

        // (Opcional) Observador para el ProgressBar
        // viewModel.isLoading.observe(this, Observer { isLoading ->
        //    progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        // })
    }
}