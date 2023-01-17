package pt.ipca.smartcanteen.models.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.RetroTicketMeal

import pt.ipca.smartcanteen.models.adapters.viewHolders.OrderDetailsAdapterRecViewHolder

class OrderDetailsAdapterRec(private var orderMealsList: List<RetroTicketMeal>) :
    RecyclerView.Adapter<OrderDetailsAdapterRecViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return OrderDetailsAdapterRecViewHolder(inflater, parent.context, parent)
    }

    override fun onBindViewHolder(holder: OrderDetailsAdapterRecViewHolder, position: Int) {
        val orderMeal = orderMealsList[position]
        val amount = if (orderMeal.amount == 1) "${orderMeal.amount} dose" else "${orderMeal.amount} doses"
        val name = orderMeal.name
        val description = orderMeal.description
        val mealchanges = orderMeal.mealchanges
        val url = orderMeal.url


        holder.bindData(name, amount, description, mealchanges, url)

    }

    override fun getItemCount(): Int {
        return orderMealsList.size
    }
}