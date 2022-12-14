package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MealsAdapterRec(private var mealsList: MutableList<Meal>) :
    RecyclerView.Adapter<MealsAdapterRecViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealsAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: MealsAdapterRecViewHolder, position: Int) {
        val meal = mealsList.get(position)
        val title = meal.name
        val price = "${meal.price}€"
        val preptime = "${meal.prepTime}min"
        holder.bindData(title,preptime,price)
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}