package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.views.activities.ConsumerTradeActivity

class OrdersAdapterRecViewHolder(
    val progressBar: ProgressBar,
    val textProgress: TextView,
    val linearLayoutManager: LinearLayoutManager,
    val sp: SharedPreferences,
    val myOrdersAdapter: RecyclerView,
    inflater: LayoutInflater,
    val parent: ViewGroup,
    private val activity: Activity,
    private val context: Context
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.my_order_card, parent, false)) {
    val identifierTv = itemView.findViewById<TextView>(R.id.my_orders_card_identifier)
    val quantityTv = itemView.findViewById<TextView>(R.id.my_orders_card_quantity)
    val priceTv = itemView.findViewById<TextView>(R.id.my_orders_card_price)
    val stateTv = itemView.findViewById<TextView>(R.id.my_orders_card_state)
    val tradeButton = itemView.findViewById<Button>(R.id.my_orders_card_button_trade)

    fun setTradeClickListener(ticketId: String) {
        tradeButton.setOnClickListener {
            var intent = Intent(activity, ConsumerTradeActivity::class.java)
            // println(ticketId)
            intent.putExtra("ticketId", ticketId)
            activity.startActivity(intent)
        }
    }

    fun bindData(identifierText: Int, quantityText: Int, priceText: Float, stateText: String) {
        identifierTv.text = identifierText.toString()
        quantityTv.text = quantityText.toString()
        priceTv.text = priceText.toString()
        stateTv.text = stateText

        if (stateText == "Não Iniciado" || stateText == "Atraso") {
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.redLogout))
        } else if (stateText == "Pronto" || stateText == "Entregue") {
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.background_color))
        } else if (stateText == "Em Preparação") {
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
        }

        if (stateText == "Entregue") {
            tradeButton.visibility = Button.GONE
        } else {
            tradeButton.visibility = Button.VISIBLE
        }
    }
}