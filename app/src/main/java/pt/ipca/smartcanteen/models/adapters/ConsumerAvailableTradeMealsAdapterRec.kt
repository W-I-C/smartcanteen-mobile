package pt.ipca.smartcanteen.models.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroTicketMeal
import pt.ipca.smartcanteen.models.adapters.viewHolders.ConsumerAvailableTradeMealsViewHolder

class ConsumerAvailableTradeMealsAdapterRec(private var meals: List<RetroTicketMeal>, private var context: Context) :
    RecyclerView.Adapter<ConsumerAvailableTradeMealsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumerAvailableTradeMealsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ConsumerAvailableTradeMealsViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ConsumerAvailableTradeMealsViewHolder, position: Int) {
        val meal = meals[position]
        val mealName = "${meal.amount}x ${meal.name}"
        holder.bindData(mealName)
    }

    override fun getItemCount(): Int {
        return meals.size
    }
}