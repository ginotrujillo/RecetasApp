package com.example.recetasapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recetasapp.DetailActivity
import com.example.recetasapp.R
import com.example.recetasapp.data.model.Meal
// IMPORTA LAS LIBRERÍAS DE FIREBASE
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class RecipeAdapter(private var recipes: List<Meal>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeName: TextView = view.findViewById(R.id.tvRecipeName)
        val recipeImage: ImageView = view.findViewById(R.id.ivRecipeImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.recipeName.text = recipe.name
        Glide.with(holder.itemView.context)
            .load(recipe.thumbnail)
            .into(holder.recipeImage)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val recipeForDetail = recipes[currentPosition]

                // --- AÑADE ESTA LÍNEA ---
                // Guardamos el clic en Firestore
                saveRecipeClick(recipeForDetail)
                // --- FIN DE AÑADIR ---

                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("RECIPE_ID", recipeForDetail.id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = recipes.size

    fun updateData(newRecipes: List<Meal>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    // --- AÑADE ESTA NUEVA FUNCIÓN ---
    /**
     * Guarda los datos del clic en la colección "clicks" de Firestore
     */
    private fun saveRecipeClick(recipe: Meal) {
        // Obtenemos una instancia de la base de datos
        val db = Firebase.firestore

        // Creamos el documento
        val clickData = hashMapOf(
            "recipeId" to recipe.id,
            "recipeName" to recipe.name,
            "timestamp" to System.currentTimeMillis()
        )

        // Lo guarda en una "colección" (tabla) llamada "clicks"
        db.collection("clicks").add(clickData)
    }
    // --- FIN DE AÑADIR ---
}