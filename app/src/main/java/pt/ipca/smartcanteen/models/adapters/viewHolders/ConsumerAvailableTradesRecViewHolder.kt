package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroTicketMeal
import pt.ipca.smartcanteen.models.adapters.ConsumerAvailableTradeMealsAdapterRec
import pt.ipca.smartcanteen.models.adapters.OrderDetailsMealsChangesAdapterRec

class ConsumerAvailableTradesRecViewHolder(inflater: LayoutInflater, private val context: Context, val parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_trades_menu_card, parent, false)) {
    val consumerNameTv = itemView.findViewById<TextView>(R.id.trades_menu_card_consumer_name_tv)
    val priceTv = itemView.findViewById<TextView>(R.id.trades_menu_card_meal_price_tv)
    val stateTv = itemView.findViewById<TextView>(R.id.trades_menu_card_meal_state_tv)
    val mealsRv = itemView.findViewById<RecyclerView>(R.id.trades_menu_card_meals_rv)

    fun bindData(owner: String, price: String, state: String, meals: List<RetroTicketMeal>) {
        consumerNameTv.text = owner
        priceTv.text = price
        stateTv.text = state
        if(state == "Não Iniciado" || state == "Atraso"){
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.redLogout))
        } else if(state == "Pronto" || state == "Entregue"){
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.background_color))
        } else if(state == "Em Preparação") {
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
        }

        val ordersAdapter = ConsumerAvailableTradeMealsAdapterRec(meals, context)
        val orderMealsLinearLayoutManager = LinearLayoutManager(parent.context)
        orderMealsLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mealsRv.layoutManager = orderMealsLinearLayoutManager
        mealsRv.itemAnimator = DefaultItemAnimator()
        mealsRv.adapter = ordersAdapter
    }
}