package com.example.recetasapp.ui.adapter
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recetasapp.R // <-- IMPORTANTE: Importa el R de tu app
import com.example.recetasapp.data.model.Meal // <-- IMPORTANTE: Importa tu modelo 'Meal'
import com.example.recetasapp.DetailActivity


// Esta será la interfaz "mensajera"
interface OnItemClickListener {
    fun onItemClick(mealId: String) // Enviará el ID de la receta
}
class RecipeAdapter(private var recipes: List<Meal>, private val listener: OnItemClickListener) :
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

        // --- CORRECCIÓN FINAL: Usar 'name' y 'thumbnail' como en el data class ---
        holder.recipeName.text = recipe.name

        // ... (dentro de onBindViewHolder)
        Glide.with(holder.itemView.context)
            .load(recipe.thumbnail)
            .into(holder.recipeImage)

// ¡AÑADE ESTO!
// Cuando se haga clic en la fila (itemView)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val recipe = recipes[position]
            val intent = Intent(context, DetailActivity::class.java) // Asegúrate que el nombre de tu Activity sea correcto

            // 2. Adjunta el ID de la receta como un "extra"
            intent.putExtra("RECIPE_ID", recipe.id) // "RECIPE_ID" es una clave que inventamos

            // 3. Inicia la nueva Activity
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = recipes.size

    fun updateData(newRecipes: List<Meal>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}