package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class ConsumerAvailableTradeMealsViewHolder(inflater: LayoutInflater, val parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_trades_menu_card_meals_item, parent, false)) {
    val mealTv = itemView.findViewById<TextView>(R.id.trades_menu_card_meal_name_tv)
    fun bindData(meal: String) {
        mealTv.text = meal
    }
}