package pt.ipca.smartcanteen.models.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.adapters.viewHolders.BarMenuMealsAdapterRecViewHolder

class BarMenuMealsAdapterRec(private var mealsList: List<RetroMeal>) :
    RecyclerView.Adapter<BarMenuMealsAdapterRecViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarMenuMealsAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BarMenuMealsAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: BarMenuMealsAdapterRecViewHolder, position: Int) {
        val meal = mealsList.get(position)
        val title = meal.name
        val price = "${meal.price}€"
        val preptime = "${meal.preparationtime}min"
        holder.bindData(title,preptime,price)
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}